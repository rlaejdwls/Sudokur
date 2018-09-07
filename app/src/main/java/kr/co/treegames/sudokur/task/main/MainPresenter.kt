package kr.co.treegames.sudokur.task.main

import kr.co.treegames.sudokur.data.model.Key
import kr.co.treegames.sudokur.data.source.SharedPreferencesRepository
import org.opencv.core.Mat

/**
 * Created by Hwang on 2018-09-05.
 *
 * Description :
 */
class MainPresenter(private val preferences: SharedPreferencesRepository,
                    val view: MainContract.View)
    : MainContract.Presenter {
    companion object {
        init {
            System.loadLibrary("opencv_java3")
            System.loadLibrary("native-lib")
        }
    }
    init {
        view.presenter = this
    }

    private external fun welcome(): String
    private external fun detect(input: Long, result: Long): Boolean

    override fun start() {
        view.showMessage(welcome() + ":resume main view:uuid:" + preferences.getString(Key.SharedPreferences.UUID))
    }

    override fun detectShape(input: Mat, success: (result: Mat) -> Unit, failure: () -> Unit) {
        val result = Mat(input.rows(), input.cols(), input.type())
        when(detect(input.nativeObjAddr, result.nativeObjAddr)) {
            true -> success(result)
            false -> failure()
        }
    }
}