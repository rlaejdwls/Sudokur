package kr.co.treegames.core.manage;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by Hwang on 2016-10-15.
 * 작성자 : 황의택
 * 내용 : 현재 디버그 상태를 관리하는 클래스
 */
public class Debugger {
    public static boolean DEBUG = false;

    /**
     * 초기화 함수
     * @param context Application Context
     */
    public static void initialize(Context context) {
        DEBUG = isDebuggable(context);
    }
    /**
     * Application Context를 넘겨주면 debug build인가를 판단해서 반환하는 함수
     * @param context Application Context
     * @return debug:true, release:false
     */
    public static boolean isDebuggable(Context context) {
        boolean debuggable = false;

        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return debuggable;
    }
}
