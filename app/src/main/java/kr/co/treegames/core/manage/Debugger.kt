package kr.co.treegames.core.manage

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

/**
 * Created by Hwang on 2018-09-04.
 *
 * Description :
 */
//class Debugger {
//    companion object {
//        var DEBUG: Boolean = false
//    }
//    /**
//     * 초기화 함수
//     * @param context Application Context
//     */
//    fun initialize(context: Context) {
//        DEBUG = isDebuggable(context)
//    }
//    /**
//     * Application Context를 넘겨주면 debug build인가를 판단해서 반환하는 함수
//     * @param context Application Context
//     * @return debug:true, release:false
//     */
//    private fun isDebuggable(context: Context): Boolean {
//        var debuggable = false
//
//        val packageManager = context.packageManager
//        try {
//            val appInfo = packageManager.getApplicationInfo(context.packageName, 0)
//            debuggable = 0 != appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
//        return debuggable
//    }
//}