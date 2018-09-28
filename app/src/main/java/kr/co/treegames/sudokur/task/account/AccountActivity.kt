package kr.co.treegames.sudokur.task.account

import android.os.Bundle
import kr.co.treegames.sudokur.Injection
import kr.co.treegames.sudokur.R
import kr.co.treegames.sudokur.task.DefaultActivity
import kr.co.treegames.sudokur.task.replaceFragmentInActivity

/**
 * Created by Hwang on 2018-09-03.
 *
 * Description :
 */
class AccountActivity: DefaultActivity() {
    private lateinit var presenter: AccountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val fragment: AccountFragment = supportFragmentManager.findFragmentById(R.id.content) as AccountFragment?
                ?: Injection.provideFragment(AccountFragment::class.java).also {
                    replaceFragmentInActivity(it, R.id.content)
                }

        presenter = AccountPresenter(
                Injection.provideSharedPreferences(applicationContext),
                Injection.provideAccountRepository(),
                fragment
        )
    }
}