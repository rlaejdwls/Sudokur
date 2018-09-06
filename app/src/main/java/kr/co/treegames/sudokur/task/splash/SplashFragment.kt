package kr.co.treegames.sudokur.task.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_account.*
import kr.co.treegames.sudokur.task.DefaultFragment
import kr.co.treegames.sudokur.task.account.AccountActivity
import kr.co.treegames.sudokur.task.main.MainActivity

/**
 * Created by Hwang on 2018-08-31.
 *
 * Description :
 */
class SplashFragment: DefaultFragment(), SplashContract.View {
    companion object {
        private const val SPLASH_MILLISECOND: Long = 500
    }

    override lateinit var presenter: SplashContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.automatic()
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
        Handler().postDelayed({
            startActivity(Intent(activity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }, SPLASH_MILLISECOND)
    }
    override fun startAccountActivity() {
        Handler().postDelayed({
            startActivity(Intent(activity, AccountActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }, SPLASH_MILLISECOND)
    }
}