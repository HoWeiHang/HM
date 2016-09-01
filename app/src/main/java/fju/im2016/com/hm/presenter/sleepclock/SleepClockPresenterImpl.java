//package fju.im2016.com.hm.presenter.sleepclock;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.CountDownTimer;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.TimeZone;
//
//import fju.im2016.com.hm.core.entity.CountDown;
//import fju.im2016.com.hm.core.service.BroadcastService;
//import fju.im2016.com.hm.ui.sleepclock.SleepClockView;
//
//public class SleepClockPresenterImpl implements SleepClockPresenter {
//    private SleepClockView sleepClockView;
//    private CountDownTimer count ;
//    private  String time;
//
//    public SleepClockPresenterImpl(SleepClockView sleepClockView) {
//        this.sleepClockView = sleepClockView;
//    }
//
//    private void formatTime(long ms) {
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//        df.setTimeZone(TimeZone.getTimeZone("GMT"));
//        this.time = df.format(new Date(ms));
//    }
//
//    private int minutesToMs(int minutes) {
//        return minutes * 60 * 1000;
//    }
//
//    private int minutesToDegrees(int minutes) {
//        return minutes * 6;
//    }
//
//    @Override
//    public void newCountDownTimer(){
//        this.count = new CountDown(this.minutesToMs(Integer.parseInt(this.sleepClockView.getEditText())), 1000, new CountDown.OnFinishCallBack() {
//            @Override
//            public void onFinish() {
//                sleepClockView.onFinish();
//            }
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                formatTime(millisUntilFinished);
//                sleepClockView.setCountdownTime(time);
//            }
//        });
//    }
//
//    @Override
//    public void start() {
//        this.sleepClockView.runClockAnim();
//        if(this.count != null) {
//            this.count.cancel();
//        }
//        this.newCountDownTimer();
//        this.count.start();
//    }
//
//    @Override
//    public int getDegrees() {
//        return this.minutesToDegrees(Integer.parseInt(this.sleepClockView.getEditText()));
//    }
//
//    @Override
//    public int getDuration() {
//        return this.minutesToMs(Integer.parseInt(this.sleepClockView.getEditText()));
//    }
//
//
//}
