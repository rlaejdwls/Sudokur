package kr.co.treegames.sudokur.task.splash

import android.os.Bundle
import kr.co.treegames.sudokur.Injection
import kr.co.treegames.sudokur.R
import kr.co.treegames.sudokur.task.DefaultActivity
import kr.co.treegames.sudokur.task.replaceFragmentInActivity

/**
 * Created by Hwang on 2018-08-31.
 *
 * Description :
 */

class SplashActivity: DefaultActivity() {
    private lateinit var presenter: SplashContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fragment = supportFragmentManager.findFragmentById(R.id.content) as SplashFragment?
                ?: Injection.provideFragment(SplashFragment::class.java).also {
                    replaceFragmentInActivity(it, R.id.content)
                }

        presenter = SplashPresenter(
                Injection.provideSharedPreferences(applicationContext),
                Injection.provideAccountRepository(),
                fragment
        )
    }
}