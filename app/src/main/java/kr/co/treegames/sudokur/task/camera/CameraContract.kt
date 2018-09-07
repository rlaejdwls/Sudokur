package kr.co.treegames.sudokur.task.camera

import kr.co.treegames.sudokur.task.BasePresenter
import kr.co.treegames.sudokur.task.BaseView

/**
 * Created by Hwang on 2018-09-06.
 *
 * Description :
 */
interface CameraContract {
    interface View: BaseView<Presenter> {
        fun showMessage(message: String)
    }
    interface Presenter: BasePresenter {

    }
}