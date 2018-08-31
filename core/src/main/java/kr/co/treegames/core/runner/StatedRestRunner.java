package kr.co.treegames.core.runner;

import kr.co.treegames.core.runner.event.OnRunningRestEventListener;

/**
 * Created by Hwang on 2016-11-14.
 * 작성자 : 황의택
 * 내용 : 지정한 시간마다 쉬어가는 러너
 */
public class StatedRestRunner extends Thread {
    private OnRunningRestEventListener onRunningRestEventListener;
    private boolean isRunning = false;

    private final int RUN_SECOND;
    private int currentSecond = 0;

    public StatedRestRunner(int runningTime) {
        super();
        this.RUN_SECOND = runningTime;
    }
    public StatedRestRunner(int runningTime, OnRunningRestEventListener onRunningRestEventListener) {
        super();
        this.RUN_SECOND = runningTime;
        this.onRunningRestEventListener = onRunningRestEventListener;
    }

    public boolean isRunning() {
        return isRunning;
    }
    public void startRun() {
        this.isRunning = true;
        this.start();
    }
    public void stopRun() {
        this.isRunning = false;
    }

    @Override
    public void run() {
        super.run();

        while(isRunning) {
            currentSecond++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (currentSecond < RUN_SECOND) {
                continue;
            } else {
                if (this.onRunningRestEventListener != null) {
                    this.onRunningRestEventListener.onRunningRestEvent();
                }
                currentSecond = 0;
            }
        }
    }

    public void setOnRunningRestEventListener(OnRunningRestEventListener listener) {
        this.onRunningRestEventListener = listener;
    }
}
