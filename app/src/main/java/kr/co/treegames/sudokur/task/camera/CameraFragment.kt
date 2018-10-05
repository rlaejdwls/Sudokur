package kr.co.treegames.sudokur.task.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.Toast
import kr.co.treegames.sudokur.R
import kr.co.treegames.sudokur.task.DefaultFragment
import java.io.File
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

/**
 * Created by Hwang on 2018-09-06.
 *
 * Description :
 */
class CameraFragment: DefaultFragment(), CameraContract.View {
    override lateinit var presenter: CameraContract.Presenter

    private lateinit var textureView: AutoFitTextureView
    private lateinit var cameraId: String
    private lateinit var file: File
    private lateinit var previewSize: Size
    private lateinit var previewRequestBuilder: CaptureRequest.Builder
    private lateinit var previewRequest: CaptureRequest
    private var cameraDevice: CameraDevice? = null
    private var backgroundHandler: Handler? = null
    private var imageReader: ImageReader? = null
    private var captureSession: CameraCaptureSession? = null
    private val cameraOpenCloseLock = Semaphore(1)
    private var sensorOrientation = 0
    private var flashSupported = false

    private val listener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            openCamera(width, height)
        }
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            configureTransform(width, height)
        }
        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) = Unit
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean = true
    }
    private val callback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice?) {
            cameraOpenCloseLock.release()
            this@CameraFragment.cameraDevice = camera
            createCameraPreviewSession()
        }
        override fun onDisconnected(camera: CameraDevice?) {
            cameraOpenCloseLock.release()
            camera?.close()
            this@CameraFragment.cameraDevice = null
        }
        override fun onError(camera: CameraDevice?, error: Int) {
            onDisconnected(camera)
            this@CameraFragment.activity?.finish()
        }
    }
    private val onImageAvailableListener = ImageReader.OnImageAvailableListener {
        backgroundHandler?.post(ImageSaver(it.acquireNextImage(), file))
    }
    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {

    }

    private fun createCameraPreviewSession() {
        try {
            val texture = textureView.surfaceTexture

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(previewSize.width, previewSize.height)

            // This is the output Surface we need to start preview.
            val surface = Surface(texture)

            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW
            )
            previewRequestBuilder.addTarget(surface)

            // Here, we create a CameraCaptureSession for camera preview.
            cameraDevice?.createCaptureSession(Arrays.asList(surface, imageReader?.surface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            // The camera is already closed
                            if (cameraDevice == null) return

                            // When the session is ready, we start displaying the preview.
                            captureSession = cameraCaptureSession
                            try {
                                // Auto focus should be continuous for camera preview.
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                // Flash is automatically enabled when necessary.
                                setAutoFlash(previewRequestBuilder)

                                // Finally, we start displaying the camera preview.
                                previewRequest = previewRequestBuilder.build()
                                captureSession?.setRepeatingRequest(previewRequest,
                                        captureCallback, backgroundHandler)
                            } catch (e: CameraAccessException) {
                            }

                        }
                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            showMessage("Failed")
                        }
                    }, null)
        } catch (e: CameraAccessException) {
        }
    }
    private fun setAutoFlash(requestBuilder: CaptureRequest.Builder) {
        if (flashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
        }
    }

    private fun openCamera(width: Int, height: Int) {
        activity?.let {
            val permission = ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA)
            if (permission != PackageManager.PERMISSION_GRANTED) {
//                requestCameraPermission()
                return
            }
            setUpCameraOutputs(width, height)
            configureTransform(width, height)
            val manager = it.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                // Wait for camera to open - 2.5 seconds is sufficient
                if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                    throw RuntimeException("Time out waiting to lock camera opening.")
                }
                manager.openCamera(cameraId, callback, backgroundHandler)
            } catch (e: CameraAccessException) {
            } catch (e: InterruptedException) {
                throw RuntimeException("Interrupted while trying to lock camera opening.", e)
            }
        }
    }
    private fun setUpCameraOutputs(width: Int, height: Int) {
        activity?.let {
            val manager = it.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                for (cameraId in manager.cameraIdList) {
                    val characteristics = manager.getCameraCharacteristics(cameraId)

                    // We don't use a front facing camera in this sample.
                    val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
                    if (cameraDirection != null &&
                            cameraDirection == CameraCharacteristics.LENS_FACING_FRONT) {
                        continue
                    }

                    val map = characteristics.get(
                            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: continue

                    // For still image captures, we use the largest available size.
                    val largest = Collections.max(
                            Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)),
                            CompareSizesByArea())
                    imageReader = ImageReader.newInstance(largest.width, largest.height,
                            ImageFormat.JPEG, /*maxImages*/ 2).apply {
                        setOnImageAvailableListener(onImageAvailableListener, backgroundHandler)
                    }

                    // Find out if we need to swap dimension to get the preview size relative to sensor
                    // coordinate.
                    val displayRotation = it.windowManager.defaultDisplay.rotation

                    sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
                    val swappedDimensions = areDimensionsSwapped(displayRotation)

                    val displaySize = Point()
                    it.windowManager.defaultDisplay.getSize(displaySize)
                    val rotatedPreviewWidth = if (swappedDimensions) height else width
                    val rotatedPreviewHeight = if (swappedDimensions) width else height
                    var maxPreviewWidth = if (swappedDimensions) displaySize.y else displaySize.x
                    var maxPreviewHeight = if (swappedDimensions) displaySize.x else displaySize.y

                    if (maxPreviewWidth > MAX_PREVIEW_WIDTH) maxPreviewWidth = MAX_PREVIEW_WIDTH
                    if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) maxPreviewHeight = MAX_PREVIEW_HEIGHT

                    // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
                    // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                    // garbage capture data.
                    previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java),
                            rotatedPreviewWidth, rotatedPreviewHeight,
                            maxPreviewWidth, maxPreviewHeight,
                            largest)

                    // We fit the aspect ratio of TextureView to the size of preview we picked.
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        textureView.setAspectRatio(previewSize.width, previewSize.height)
                    } else {
                        textureView.setAspectRatio(previewSize.height, previewSize.width)
                    }

                    // Check if the flash is supported.
                    flashSupported = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true

                    this.cameraId = cameraId

                    // We've found a viable camera and finished setting up member variables,
                    // so we don't need to iterate through other available cameras.
                    return
                }
            } catch (e: CameraAccessException) {
            } catch (e: NullPointerException) {
                // Currently an NPE is thrown when the Camera2API is used but not supported on the
                // device this code runs.
            }
        }
    }
    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
        activity?.let {
            val rotation = it.windowManager.defaultDisplay.rotation
            val matrix = Matrix()
            val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
            val bufferRect = RectF(0f, 0f, previewSize.height.toFloat(), previewSize.width.toFloat())
            val centerX = viewRect.centerX()
            val centerY = viewRect.centerY()

            if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
                bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
                val scale = Math.max(
                        viewHeight.toFloat() / previewSize.height,
                        viewWidth.toFloat() / previewSize.width)
                with(matrix) {
                    setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
                    postScale(scale, scale, centerX, centerY)
                    postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
                }
            } else if (Surface.ROTATION_180 == rotation) {
                matrix.postRotate(180f, centerX, centerY)
            }
            textureView.setTransform(matrix)
        }
    }
    private fun areDimensionsSwapped(displayRotation: Int): Boolean {
        var swappedDimensions = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    swappedDimensions = true
                }
            }
            Surface.ROTATION_90, Surface.ROTATION_270 -> {
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    swappedDimensions = true
                }
            }
            else -> {
            }
        }
        return swappedDimensions
    }
    private fun closeCamera() {
        try {
            cameraOpenCloseLock.acquire()
            captureSession?.close()
            captureSession = null
            cameraDevice?.close()
            cameraDevice = null
            imageReader?.close()
            imageReader = null
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }
    }
    private fun stopBackgroundThread() {
//        backgroundThread?.quitSafely()
        try {
//            backgroundThread?.join()
//            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
        }
    }

    companion object {
        private val MAX_PREVIEW_WIDTH = 1920
        private val MAX_PREVIEW_HEIGHT = 1080

        @JvmStatic
        private fun chooseOptimalSize(
                choices: Array<Size>,
                textureViewWidth: Int,
                textureViewHeight: Int,
                maxWidth: Int,
                maxHeight: Int,
                aspectRatio: Size
        ): Size {

            // Collect the supported resolutions that are at least as big as the preview Surface
            val bigEnough = ArrayList<Size>()
            // Collect the supported resolutions that are smaller than the preview Surface
            val notBigEnough = ArrayList<Size>()
            val w = aspectRatio.width
            val h = aspectRatio.height
            for (option in choices) {
                if (option.width <= maxWidth && option.height <= maxHeight &&
                        option.height == option.width * h / w) {
                    if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                        bigEnough.add(option)
                    } else {
                        notBigEnough.add(option)
                    }
                }
            }

            // Pick the smallest of those big enough. If there is no one big enough, pick the
            // largest of those not big enough.
            if (bigEnough.size > 0) {
                return Collections.min(bigEnough, CompareSizesByArea())
            } else if (notBigEnough.size > 0) {
                return Collections.max(notBigEnough, CompareSizesByArea())
            } else {
                return choices[0]
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textureView = view.findViewById(R.id.camera)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { file = File(it.getExternalFilesDir(null), "pic.jpg") }
    }
    override fun onResume() {
        super.onResume()
        presenter.start()

        if (textureView.isAvailable) {
            openCamera(textureView.width, textureView.height)
        } else {
            textureView.surfaceTextureListener = listener
        }
    }
    override fun onPause() {
        super.onPause()
        closeCamera()
        stopBackgroundThread()
    }

    override fun showMessage(message: String) {
        Handler(Looper.getMainLooper()).post { Toast.makeText(activity, message, Toast.LENGTH_LONG).show() }
    }
}