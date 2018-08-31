package kr.co.treegames.sudokur.task

import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by Hwang on 2018-08-31.
 *
 * Description :
 */
open class DefaultActivity : AppCompatActivity() {
    fun addFragment(fragment: Fragment, frameId: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }
}