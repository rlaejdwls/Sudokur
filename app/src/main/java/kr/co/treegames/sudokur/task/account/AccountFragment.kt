package kr.co.treegames.sudokur.task.account

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_account.*
import kr.co.treegames.sudokur.R
import kr.co.treegames.sudokur.data.model.Account
import kr.co.treegames.sudokur.task.DefaultFragment
import kr.co.treegames.sudokur.task.main.MainActivity

/**
 * Created by Hwang on 2018-09-03.
 *
 * Description :
 */
class AccountFragment : DefaultFragment(), AccountContract.View {
    override lateinit var presenter: AccountContract.Presenter

    private var isSignInProcess: Boolean = false

    private val onClick = fun(view: View) {
        when(view.id) {
            R.id.btn_action -> {
                when(isSignInProcess) {
                    true -> presenter.signIn(Account(edt_email.text.toString(), edt_password.text.toString()))
                    false -> presenter.signUp(Account(edt_email.text.toString(), edt_password.text.toString()))
                }
            }
            R.id.btn_google_sign_in -> {
                isSignInProcess = !isSignInProcess
                btn_action.text = if(isSignInProcess) "Sign In" else "Sign Up"
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_action.setOnClickListener(onClick)
        btn_google_sign_in.setOnClickListener(onClick)
    }
    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun setLoadingIndicator(isShow: Boolean) {
        Handler(Looper.getMainLooper()).post { progress_bar.visibility = if(isShow) View.VISIBLE else View.GONE }
    }
    override fun showMessage(message: String) {
        Handler(Looper.getMainLooper()).post { Toast.makeText(activity, message, Toast.LENGTH_LONG).show() }
    }
    override fun startMainActivity() {
        startActivity(Intent(activity, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}