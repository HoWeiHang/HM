package fju.im2016.com.hm.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.ui.manual.ManualActivity;
import fju.im2016.com.hm.ui.setting.SettingActivity;
import fju.im2016.com.hm.ui.youtube.YoutubeActivity;

public class IndexActivity extends AppCompatActivity {

    private ImageButton btnMediaPlayer, btnYoutube, btnSetting, btnMmanual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        iniBtnMediaPlayer();
        iniBtnYoutube();
        iniBtnSetting();
        iniBtnMmanual();
    }

    private void iniBtnMediaPlayer() {
        this.btnMediaPlayer = (ImageButton) findViewById(R.id.btnMediaPlayer);
        this.btnMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(IndexActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            }
        });
    }

    private void iniBtnYoutube() {
        this.btnYoutube = (ImageButton) findViewById(R.id.btnYoutube);
        this.btnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(IndexActivity.this, YoutubeActivity.class));
            }
        });
    }

    private void iniBtnSetting() {
        this.btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        this.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(IndexActivity.this, SettingActivity.class));
            }
        });
    }

    private void iniBtnMmanual() {
        this.btnMmanual = (ImageButton) findViewById(R.id.btnMmanual);
        this.btnMmanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(IndexActivity.this, ManualActivity.class));
            }
        });
    }
}
