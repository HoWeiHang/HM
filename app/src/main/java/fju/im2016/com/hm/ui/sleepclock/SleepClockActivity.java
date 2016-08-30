package fju.im2016.com.hm.ui.sleepclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import fju.im2016.com.hm.presenter.sleepclock.SleepClockPresenter;
import fju.im2016.com.hm.presenter.sleepclock.SleepClockPresenterImpl;

public class SleepClockActivity extends Activity implements SleepClockView {
    private SleepClockPresenter sleepClockPresenter;
    private ImageView hand;
    private EditText editText;
    private Button confirm;
    private TextView countdownTime;
    private String checkEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_clock);
        this.hand = (ImageView) findViewById(R.id.hand);
        this.editText = (EditText) findViewById(R.id.editText);
        this.countdownTime = (TextView) findViewById(R.id.countdownTime);
        this.iniSleepClockPresenter();
        this.iniBtnConfirm();

        checkEditText = editText.getText().toString();
        if(!checkEditText.equals("")) {
            this.sleepClockPresenter.newCountDownTimer();
        }
    }

    private void iniSleepClockPresenter() {
        this.sleepClockPresenter = new SleepClockPresenterImpl(this);
    }

    private void clockwise() {

        RotateAnimation handRotate = new RotateAnimation(this.sleepClockPresenter.getDegrees(), 0, RotateAnimation.RELATIVE_TO_SELF, 0.58f, RotateAnimation.RELATIVE_TO_SELF, 0.985f);
        handRotate.setDuration(this.sleepClockPresenter.getDuration());
//        handRotate.setDuration(5000);
        handRotate.setInterpolator(new LinearInterpolator());
        this.hand.startAnimation(handRotate);
    }

    private void iniBtnConfirm() {
        this.confirm = (Button) findViewById(R.id.confirm);
        this.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleepClockPresenter.start();
            }
        });
    }

    @Override
    public void runClockAnim() {
        this.clockwise();
    }

    @Override
    public void setCountdownTime(String string) {
        this.countdownTime.setText(string);
    }

    @Override
    public String getEditText() {
        return  this.editText.getText().toString();
    }

    @Override
    public void onFinish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
