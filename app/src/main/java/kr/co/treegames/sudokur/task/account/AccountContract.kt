package kr.co.treegames.sudokur.task.account

import kr.co.treegames.sudokur.data.model.Account
import kr.co.treegames.sudokur.task.BasePresenter
import kr.co.treegames.sudokur.task.BaseView

/**
 * Created by Hwang on 2018-09-03.
 *
 * Description :
 */
interface AccountContract {
    interface View: BaseView<Presenter> {
        fun setLoadingIndicator(isShow: Boolean)
        fun showMessage(message: String)
        fun startMainActivity()
    }
    interface Presenter: BasePresenter {
        fun signIn(account: Account)
        fun signUp(account: Account)
    }
}