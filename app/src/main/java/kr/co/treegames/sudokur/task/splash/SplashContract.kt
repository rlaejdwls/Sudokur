package kr.co.treegames.sudokur.task.splash

import kr.co.treegames.sudokur.task.BasePresenter
import kr.co.treegames.sudokur.task.BaseView

/**
 * Created by Hwang on 2018-09-05.
 *
 * Description :
 */
interface SplashContract {
    interface View: BaseView<Presenter> {
        fun setLoadingIndicator(isShow: Boolean)
        fun showMessage(message: String)
        fun startMainActivity()
        fun startAccountActivity()
    }
    interface Presenter: BasePresenter {
        fun automatic()
    }
}