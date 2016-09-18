package fju.im2016.com.hm.ui.viewpager;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.ui.LinkFirst.LinkFirstActivity;

public class Leading extends AppCompatActivity implements OnPageChangeListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;

    private ImageView[] dots;

    private int currentIndex;
    Boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leading1);

        initViews();

        initDots();
    }

    private void initViews() {

        SharedPreferences pref = getSharedPreferences("first",Activity.MODE_PRIVATE);
        isFirst = pref.getBoolean("status",true);

        if(!isFirst){
            Intent intent = new Intent(this,LinkFirstActivity.class);
            startActivity(intent);
            finish();
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout guideFour = (RelativeLayout) inflater.inflate(R.layout.guidefour, null);
        guideFour.findViewById(R.id.guide_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Leading.this,LinkFirstActivity.class);
                startActivity(intent);
                finish();
            }
        });
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.guideone, null));
        views.add(inflater.inflate(R.layout.guidetwo, null));
        views.add(inflater.inflate(R.layout.guidethree, null));
        views.add(guideFour);
        vpAdapter = new ViewPagerAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);


    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[views.size()];


        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentDot(arg0);
    }

}