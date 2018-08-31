package kr.co.treegames.core.event;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by Hwang on 2016-11-07.
 * 작성자 : 황의택
 * 내용 : 사용자가 빠르게 버튼을 연타 할 경우 후에 발생되는 클릭 이벤트를 막기 위한 클래스
 */
public abstract class OnSingleClickListener implements View.OnClickListener {
    private static final long CLICK_INTERVAL = 600;         //다음 클릭까지의 최소 간격

    private long lastClickTime;

    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - lastClickTime;
        lastClickTime = currentClickTime;

        if(elapsedTime < CLICK_INTERVAL){
            return;
        }
        onSingleClick(v);
    }
}
