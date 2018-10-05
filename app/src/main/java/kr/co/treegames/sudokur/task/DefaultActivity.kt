package kr.co.treegames.sudokur.task

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Hwang on 2018-09-03.
 *
 * Description :
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes resId: Int) {
    supportFragmentManager.transact {
        replace(resId, fragment)
    }
}
fun AppCompatActivity.setToolbar(@IdRes resId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(resId))
    supportActionBar?.run(action)
}
fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply(action).commit()
}

open class DefaultActivity : AppCompatActivity()