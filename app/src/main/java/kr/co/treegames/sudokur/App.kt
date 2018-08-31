package kr.co.treegames.sudokur

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import kr.co.treegames.core.AppCore
import kr.co.treegames.core.manage.Debugger
import kr.co.treegames.core.manage.Logger

/**
 * Created by Hwang on 2018-08-31.
 *
 * Description :
 */
class App : AppCore() {
    override fun onCreate() {
        super.onCreate()
        initialize()
    }
    private fun initialize() {
        //크래시 레포트 라이브러리 초기화
        Fabric.with(Fabric.Builder(this)
                .kits(Crashlytics.Builder()
                        .core(CrashlyticsCore.Builder()
                                .disabled(!BuildConfig.CRASHLYTIS)
                                .build())
                        .build())
                .debuggable(Debugger.DEBUG)
                .build())
    }
    override fun notifyForeground() {
        super.notifyForeground()
        Logger.d("")
    }
    override fun notifyBackground() {
        super.notifyBackground()
        Logger.d("")
    }
}