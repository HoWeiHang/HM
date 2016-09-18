package fju.im2016.com.hm.ui.viewpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Karen on 2016/8/13.
 */
public class ViewPagerAdapter extends PagerAdapter {

    // 界面列表
    private List<View> views;
    private AppCompatActivity activity;


    public ViewPagerAdapter(List<View> views, AppCompatActivity activity) {
        this.views = views;
        this.activity = activity;
    }
    //加?viewpager的每?item
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position),0);
        return views.get(position);
    }
    //?除ViewPager的item
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }

    // ?得?前界面?
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }
    //官方推荐?么?，?研究。。。。
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}