package fju.im2016.com.hm.ui.sleepclock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.CountDown;
import fju.im2016.com.hm.core.service.BroadcastService;

public class SleepClockActivity extends Activity {
    private ImageView hand;
    private EditText editText;
    private Button confirm;
    private TextView countdownTime;
    private CountDownTimer count ;
    private String checkEditText;
    private static final String TAG = "aaa";
    private int startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_clock);
        hand = (ImageView) findViewById(R.id.hand);
        editText = (EditText) findViewById(R.id.editText);
        this.countdownTime = (TextView) findViewById(R.id.countdownTime);
        this.iniBtnConfirm();

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

    private void iniBtnConfirm() {
        this.confirm = (Button) findViewById(R.id.confirm);
        this.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(SleepClockActivity.this, BroadcastService.class));
                startTime = minutesToMs(Integer.parseInt(editText.getText().toString()));
                Intent intent = new Intent(SleepClockActivity.this, BroadcastService.class);
                intent.setAction("startTime");
                intent.putExtra("startTime", startTime);
                startService(intent);
//                startService(new Intent(SleepClockActivity.this, BroadcastService.class));
                clockwise(Integer.parseInt(editText.getText().toString()));

//                if(count != null) {
//                    count.cancel();
//                }
//                newCountDownTimer();
//                count.start();
            }
        });
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.i(TAG, "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
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

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);

            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            String time = df.format(new Date(millisUntilFinished));
            countdownTime.setText(time);

            Log.i(TAG, "Countdown seconds remaining: " +  millisUntilFinished / 1000);
        }
    }

}
