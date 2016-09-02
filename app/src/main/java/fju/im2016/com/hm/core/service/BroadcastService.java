package fju.im2016.com.hm.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import fju.im2016.com.hm.core.entity.CountDown;

public class BroadcastService extends Service {

    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "fju_im2016_com_hm_core_service_countdown";
    public static final String FINISH_BR = "fju_im2016_com_hm_core_service_finish";
    private Intent bi = new Intent(COUNTDOWN_BR);
    private Intent biFinish = new Intent(FINISH_BR);
    private int startTime;
    private CountDownTimer count = null;

    @Override
    public void onCreate() {
        super.onCreate();


        Log.i(TAG, "Starting timer...");


    }

    @Override
    public void onDestroy() {

        count.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getAction() != null) {
            startTime = intent.getIntExtra("startTime", startTime);
        }

        this.count = new CountDown(startTime, 1000, new CountDown.OnFinishCallBack() {
            @Override
            public void onFinish() {
                sendBroadcast(biFinish);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
//                bi.putExtra("StartTime", startTime);
                sendBroadcast(bi);
            }
        });

        count.start();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}