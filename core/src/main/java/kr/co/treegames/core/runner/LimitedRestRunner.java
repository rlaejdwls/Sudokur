package kr.co.treegames.core.runner;

import kr.co.treegames.core.runner.event.OnRunningRestEventListener;

/**
 * Created by Hwang on 2016-11-28.
 * 작성자 : 황의택
 * 내용 : 넘겨진 갯수만큼 쉬는 러너
 */
public class LimitedRestRunner extends Thread {
    private int REST_TIME = 0;
    private int REST_COUNT = 1;

    private boolean isRunning;

    private OnRunningRestEventListener onRunningRestEventListener;

    public LimitedRestRunner(int count) {
        this.REST_COUNT = count;
    }
    public LimitedRestRunner(int time, int count) {
        this.REST_TIME = time;
        this.REST_COUNT = count;
    }
    public LimitedRestRunner(int count, OnRunningRestEventListener listener) {
        this.REST_COUNT = count;
        this.onRunningRestEventListener = listener;
    }
    public LimitedRestRunner(int time, int count, OnRunningRestEventListener listener) {
        this.REST_TIME = time;
        this.REST_COUNT = count;
        this.onRunningRestEventListener = listener;
    }

    public void stopRun() {
        this.isRunning = false;
    }
    public void startRun() {
        this.isRunning = true;
        this.start();
    }

    @Override
    public void run() {
        while (isRunning) {
            if (REST_COUNT < 1) break;

            if (onRunningRestEventListener != null) {
                onRunningRestEventListener.onRunningRestEvent();
                REST_COUNT--;

                try {
                    Thread.sleep(REST_TIME * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setOnRunningRestListener(OnRunningRestEventListener listener) {
        this.onRunningRestEventListener = listener;
    }
}
