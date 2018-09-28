package kr.co.treegames.sudokur.task.main

import android.os.Bundle
import kr.co.treegames.sudokur.Injection
import kr.co.treegames.sudokur.R
import kr.co.treegames.sudokur.task.DefaultActivity
import kr.co.treegames.sudokur.task.replaceFragmentInActivity
import kr.co.treegames.sudokur.task.setToolbar

/**
 * Created by Hwang on 2018-09-05.
 *
 * Description :
 */
class MainActivity: DefaultActivity() {
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.app_name)
        }

        val fragment: MainFragment = supportFragmentManager.findFragmentById(R.id.content) as MainFragment?
                ?: Injection.provideFragment(MainFragment::class.java).also {
                    replaceFragmentInActivity(it, R.id.content)
                }

        presenter = MainPresenter(
                Injection.provideSharedPreferences(applicationContext),
                fragment
        )
    }
}