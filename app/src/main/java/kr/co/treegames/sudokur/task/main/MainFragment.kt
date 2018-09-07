package kr.co.treegames.sudokur.task.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmarkDetector
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import kotlinx.android.synthetic.main.fragment_main.*
import kr.co.treegames.core.manage.Logger
import kr.co.treegames.sudokur.R
import kr.co.treegames.sudokur.task.DefaultFragment
import kr.co.treegames.sudokur.task.camera.CameraActivity
import org.opencv.android.Utils
import org.opencv.core.Mat


/**
 * Created by Hwang on 2018-09-05.
 *
 * Description :
 */
class MainFragment: DefaultFragment(), MainContract.View {
    override lateinit var presenter: MainContract.Presenter

    private val onClick = fun(view: View) {
        when(view.id) {
            R.id.btn_ml_kit_ocr -> {
                progress_bar.visibility = View.VISIBLE
                img_test_data.visibility = View.VISIBLE
                img_test_data.setImageDrawable(resources.getDrawable(R.drawable.test_img_ml_kit_ocr_example_en, null))
                val data: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_img_ml_kit_ocr_example_en)
                val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(data)
                val recognizer: FirebaseVisionTextRecognizer = FirebaseVision.getInstance()
                        .onDeviceTextRecognizer

                recognizer.processImage(image).addOnSuccessListener {
                    txt_test_data.text = it.text
                    progress_bar.visibility = View.GONE
                }.addOnFailureListener {
                    progress_bar.visibility = View.GONE
                    Logger.e("ML Kit OCR Fail", it)
                }
            }
            R.id.btn_ml_kit_landmark -> {
                progress_bar.visibility = View.VISIBLE
                img_test_data.visibility = View.VISIBLE
                img_test_data.setImageDrawable(resources.getDrawable(R.drawable.test_img_ml_kit_landmark_example_tokyo_sukaitsuri, null))
                val data: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_img_ml_kit_landmark_example_tokyo_sukaitsuri)
                val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(data)
                val detector: FirebaseVisionCloudLandmarkDetector = FirebaseVision.getInstance()
                        .visionCloudLandmarkDetector
                detector.detectInImage(image)
                        .addOnSuccessListener {
                            progress_bar.visibility = View.GONE
                            val builder = StringBuilder()
                            it.forEach { landmark ->
                                builder.append("---------------------------------------------------").append("\n")
                                builder.append("Landmark Name:").append(landmark.landmark).append("\n")
                                builder.append("Landmark Entity ID:").append(landmark.entityId).append("\n")
                                builder.append("Landmark Coordinates:")
                                landmark.locations.forEach { location ->
                                    builder.append(location.latitude).append(",").append(location.longitude).append("\n")
                                }
                                builder.append("---------------------------------------------------").append("\n")
                            }
                            txt_test_data.text = builder.toString()
                        }.addOnFailureListener {
                            progress_bar.visibility = View.GONE
                            Logger.e("ML Kit Landmark Fail", it)
                        }
            }
            R.id.btn_test_data_clear -> {
                img_test_data.visibility = View.GONE
                img_test_data.setImageDrawable(null)
                txt_test_data.text = ""
            }
            R.id.btn_start_camera -> {
                startActivity(Intent(activity, CameraActivity::class.java))
            }
            R.id.btn_detect_shape -> {
                progress_bar.visibility = View.VISIBLE
                img_test_data.visibility = View.VISIBLE
                img_test_data.setImageDrawable(resources?.getDrawable(R.drawable.test_img_open_cv_detect_example_shape, null))
                val input: Mat = Utils.loadResource(activity, R.drawable.test_img_open_cv_detect_example_shape)
                Thread {
                    presenter.detectShape(input, {                        val bitmap: Bitmap = Bitmap.createBitmap(it.cols(), it.rows(), Bitmap.Config.ARGB_8888)
                        Utils.matToBitmap(it, bitmap)
                        with(Handler(Looper.getMainLooper())) {
                            post {
                                progress_bar.visibility = View.GONE
                                img_test_data.setImageBitmap(bitmap)
                            }
                        }
                    }, {
                        showMessage("Detect fail")
                    })
                }.start()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_ml_kit_ocr.setOnClickListener(onClick)
        btn_ml_kit_landmark.setOnClickListener(onClick)
        btn_test_data_clear.setOnClickListener(onClick)
        btn_start_camera.setOnClickListener(onClick)
        btn_detect_shape.setOnClickListener(onClick)
    }
    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun showMessage(message: String) {
        Handler(Looper.getMainLooper()).post { Toast.makeText(activity, message, Toast.LENGTH_LONG).show() }
    }
}