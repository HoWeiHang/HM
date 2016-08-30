package fju.im2016.com.hm.core.entity;

import android.os.CountDownTimer;

/**
 * Created by user on 2016/7/8.
 */
public class CountDown extends CountDownTimer {
    private OnFinishCallBack onFinishCallBack;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *z                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public CountDown(long millisInFuture, long countDownInterval, OnFinishCallBack callBack) {
        super(millisInFuture, countDownInterval);
        this.onFinishCallBack = callBack;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        this.onFinishCallBack.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        this.onFinishCallBack.onFinish();
    }

    public void setOnFinishCallBack(OnFinishCallBack onFinishCallBack){
        this.onFinishCallBack = onFinishCallBack;
    }

    public interface OnFinishCallBack {
        void onFinish();
        void onTick(long millisUntilFinished);
    }
}


