package kr.co.treegames.core.runner;

/**
 * Created by Hwang on 2017-04-26.
 * 작성자 : 황의택
 * 내용 : 쓰레드를 사용하기 위한 베이스 클래스
 */
public class Runner extends Thread {
    protected boolean isRunning = false;
    protected boolean isRest = false;

    public void startRunning() {
        isRunning = true;
        this.start();
    }
    public void stopRunning() {
        isRunning = false;
    }
    public void pauseRunning() {
        isRest = true;
    }
    public void resumeRunning() {
        isRest = false;
    }
}
