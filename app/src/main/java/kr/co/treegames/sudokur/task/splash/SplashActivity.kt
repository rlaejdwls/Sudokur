package kr.co.treegames.sudokur.task.splash

import android.os.Bundle
import kr.co.treegames.sudokur.DefaultFragment
import kr.co.treegames.sudokur.R
import kr.co.treegames.sudokur.task.DefaultActivity

/**
 * Created by Hwang on 2018-08-31.
 *
 * Description :
 */
class SplashActivity : DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        val fragment: SplashFragment? = supportFragmentManager.findFragmentById(R.id.content) as SplashFragment
//        if (fragment == null) {
//            addFragment(DefaultFragment.create(SplashFragment::class.java), R.id.content)
//        }
    }
}