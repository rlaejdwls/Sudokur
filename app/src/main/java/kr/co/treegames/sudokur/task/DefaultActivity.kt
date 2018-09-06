package kr.co.treegames.sudokur.task

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity

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