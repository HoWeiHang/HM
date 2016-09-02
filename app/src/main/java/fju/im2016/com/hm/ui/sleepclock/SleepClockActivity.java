package fju.im2016.com.hm.ui.sleepclock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.service.BroadcastService;

public class SleepClockActivity extends Activity {
    private ImageView hand;
    private Button confirm, cancel;
    private TextView countdownTime, timeNumber;
    private SeekBar seekBar;
    private static final String TAG = "aaa";
    private int startTime, settingMins;
    private boolean reset = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_clock);
        hand = (ImageView) findViewById(R.id.hand);
        this.countdownTime = (TextView) findViewById(R.id.countdownTime);
        this.timeNumber = (TextView) findViewById(R.id.timenumber);
        this.iniSeekBar();
        this.iniBtnConfirm();
        this.lockBtnConfirm();
        this.iniBtnCancel();

    }

    public void clockwise(int minutes) {

        RotateAnimation handRotate = new RotateAnimation(this.minutesToDegrees(minutes), 0, RotateAnimation.RELATIVE_TO_SELF, 0.58f, RotateAnimation.RELATIVE_TO_SELF, 0.985f);
        handRotate.setDuration(this.minutesToMs(minutes));
//        handRotate.setDuration(5000);
        handRotate.setInterpolator(new LinearInterpolator());
        this.hand.startAnimation(handRotate);
    }

    private int minutesToMs(int minutes) {
        return minutes * 60 * 1000;
    }

    private int minutesToDegrees(int minutes) {
        return minutes * 6;
    }

    private void iniSeekBar() {
        this.seekBar = (SeekBar) findViewById(R.id.seekbar);
        this.seekBar.setMax(60);
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress != 0)
                        unlockBtnConfirm();
                    else
                        lockBtnConfirm();
                    settingMins = progress;
                    timeNumber.setText("" + settingMins);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (settingMins == 0) {
                    lockBtnConfirm();
                }
                timeNumber.setText("" + settingMins);
            }
        });
    }

    private void iniBtnConfirm() {
        this.confirm = (Button) findViewById(R.id.confirm);
        this.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reset) {
                    reset();
                } else {
                    stopService(new Intent(SleepClockActivity.this, BroadcastService.class));
                    startTime = minutesToMs(settingMins);
                    Intent intent = new Intent(SleepClockActivity.this, BroadcastService.class);
                    intent.setAction("startTime");
                    intent.putExtra("startTime", startTime);
                    startService(intent);
                    clockwise(settingMins);
                    seekBar.setEnabled(false);
                }
                reset = !reset;
                updateBtnConfirmText();
            }
        });
    }

    private void iniBtnCancel() {
        this.cancel = (Button) findViewById(R.id.cancel);
        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void updateBtnConfirmText() {
        if (reset) {
            this.confirm.setText("重設");
        } else {
            this.confirm.setText("確認");
        }
    }

    private void lockBtnConfirm() {
        this.confirm.setEnabled(false);
        this.confirm.setTextColor(Color.parseColor("#8e8e8e"));
    }

    private void unlockBtnConfirm() {
        this.confirm.setEnabled(true);
        this.confirm.setTextColor(Color.parseColor("#000000"));
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    private BroadcastReceiver brFinish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBackPressed();
            settingMins = 0;
            // or whatever method used to update your GUI fields
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        registerReceiver(brFinish, new IntentFilter(BroadcastService.FINISH_BR));
        Log.i(TAG, "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        unregisterReceiver(brFinish);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
            unregisterReceiver(brFinish);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
//        stopService(new Intent(SleepClockActivity.this, BroadcastService.class));
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void reset() {
        stopService(new Intent(SleepClockActivity.this, BroadcastService.class));
        this.settingMins = 0;
        this.countdownTime.setText("00:00:00");
        this.clockwise(0);
        this.seekBar.setEnabled(true);
        this.seekBar.setProgress(0);
        this.timeNumber.setText("0");
        lockBtnConfirm();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
//            int setMin = (intent.getIntExtra("startTime", startTime)) / 60000 ;

            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            String time = df.format(new Date(millisUntilFinished));
            this.countdownTime.setText(time);
//            this.seekBar.setProgress(setMin);
//            this.seekBar.setEnabled(false);
//            this.timeNumber.setText("" + setMin);
//            this.reset = true;
//            this.updateBtnConfirmText();

            Log.i(TAG, "Countdown seconds remaining: " +  millisUntilFinished / 1000);
        }
    }

}
