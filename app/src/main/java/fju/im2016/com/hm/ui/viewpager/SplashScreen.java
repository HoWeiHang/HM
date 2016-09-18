package fju.im2016.com.hm.ui.viewpager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.ui.LinkFirst.LinkFirstActivity;

public class SplashScreen extends AppCompatActivity {
    private boolean isFirstIn =false;
    private static final int Time =2000;
    private static final int Go_Home= 1000;
    private static final int Go_Guide= 1001;


    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Go_Home:
                    go_Home();
                    break;
                case Go_Guide:
                    go_Guide();
                    break;
            }
        };
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();

    }

    private void init() {
        SharedPreferences preferences = getSharedPreferences("suibianqu", MODE_PRIVATE);
        //先去取isFirstI的值，如果?有，表明是第一次取，第一次取肯定是true
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        //通?isFirstIn????哪??面，再?新boolean值存入SharedPreferences
        if(isFirstIn){
            mHandler.sendEmptyMessageDelayed(Go_Guide, Time);  //制定多少毫秒后?送空消息，第一???是一?int what值
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstIn", false);
            editor.commit();
        }else{
            mHandler.sendEmptyMessageDelayed(Go_Home, Time);
        }

    }

    private void go_Home(){
        Intent intent = new Intent(SplashScreen.this, LinkFirstActivity.class);
        startActivity(intent);
        finish(); //用了finish，就不能后退回welcome?面了， onActivityResult()也不能使用
    }

    private void go_Guide(){
        Intent intent = new Intent(SplashScreen.this, Leading.class);
        startActivity(intent);
        finish();
    }




}