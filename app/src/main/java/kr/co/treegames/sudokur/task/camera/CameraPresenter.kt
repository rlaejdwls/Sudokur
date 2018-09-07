package kr.co.treegames.sudokur.task.camera

/**
 * Created by Hwang on 2018-09-06.
 *
 * Description :
 */
class CameraPresenter(val view: CameraContract.View): CameraContract.Presenter {
    init {
        view.presenter = this
    }

    override fun start() {
        view.showMessage("resume camera")
    }
}