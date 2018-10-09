package kr.co.treegames.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import kr.co.treegames.core.manage.Debugger;
import kr.co.treegames.core.manage.ExceptionHandler;

public class AppCore extends Application implements LifecycleObserver {
    private static AppCore appCore = null;

    private Point point = new Point();
    private float density;
    private int statusBarHeight;

    public AppCore() {
        super();
    }
    public synchronized static AppCore getApplication() {
        return appCore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();

        listenForForeground();
    }
    public void initApplication() {
        //Application 싱글톤 저장
        AppCore.appCore = this;
        //어플리케이션 생명 주기
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        //디버거 초기화
        Debugger.initialize(this);
        //전역 예외 핸들러 선언
        if (Debugger.DEBUG) {
            new ExceptionHandler(this);
        }
        //전역 정보
        WindowManager manager = ((WindowManager)getSystemService(Context.WINDOW_SERVICE));
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getSize(point);
            display.getMetrics(outMetrics);
            density = outMetrics.density;
        }
        statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void listenForForeground() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) { appCore.onActivityCreated(activity, savedInstanceState); }
            @Override
            public void onActivityStarted(Activity activity) { appCore.onActivityStarted(activity); }
            @Override
            public void onActivityResumed(Activity activity) { appCore.onActivityResumed(activity); }
            @Override
            public void onActivityPaused(Activity activity) { appCore.onActivityPaused(activity); }
            @Override
            public void onActivityStopped(Activity activity) { appCore.onActivityStopped(activity); }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) { appCore.onActivitySaveInstanceState(activity, outState); }
            @Override
            public void onActivityDestroyed(Activity activity) { appCore.onActivityDestroyed(activity); }
        });
    }

    protected void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
    protected void onActivityStarted(Activity activity) {}
    protected void onActivityResumed(Activity activity) {}
    protected void onActivityPaused(Activity activity) {}
    protected void onActivityStopped(Activity activity) {}
    protected void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
    protected void onActivityDestroyed(Activity activity) {}

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void notifyForeground() {
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void notifyBackground() {
    }

    public static int getScreenWidth() {
        return appCore.point.x;
    }
    public static int getScreenHeight() {
        return appCore.point.y;
    }
    public static float getDensity() { return appCore.density; }
    public static int getStatusBarHeight() { return appCore.statusBarHeight; }
}
