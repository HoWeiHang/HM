package fju.im2016.com.hm.ui.manual;


import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fju.im2016.com.hm.R;

public class ManualActivity extends AppCompatActivity implements OnPageChangeListener {

    /**
     * ViewPager
     */
    private ViewPager viewPager;

    /**
     * ???的ImageView??
     */
    private ImageView[] tips;

    /**
     * ?ImageView??
     */
    private ImageView[] mImageViews;

    /**
     * ?片?源id
     */
    private int[] imgIdArray ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_layout);
        ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //?入?片?源ID
        imgIdArray = new int[]{R.drawable.media, R.drawable.sleep, R.drawable.mylist, R.drawable.youtube,
                R.drawable.favorite,R.drawable.youtubevideo, R.drawable.setting, R.drawable.link1, R.drawable.link2, R.drawable.link3};


        //???加入到ViewGroup中
        tips = new ImageView[imgIdArray.length];
        for(int i=0; i<tips.length; i++){
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LayoutParams(10,10));
            tips[i] = imageView;
            if(i == 0){
                tips[i].setBackgroundResource(R.drawable.ch_dot);
            }else{
                tips[i].setBackgroundResource(R.drawable.unch_dot);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);
        }


        //??片??到??中
        mImageViews = new ImageView[imgIdArray.length];
        for(int i=0; i<mImageViews.length; i++){
            ImageView imageView = new ImageView(this);
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
        }

        //?置Adapter
        viewPager.setAdapter(new MyAdapter());
        //?置?听，主要是?置??的背景
        viewPager.setOnPageChangeListener(this);
        //?置ViewPager的默??, ?置??度的100倍，??子?始就能往左滑?
        viewPager.setCurrentItem((mImageViews.length) * 100);

    }

    /**
     *
     * @author xiaanming
     *
     */
    public class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);

        }

        /**
         * ?入?片?去，用?前的position 除以 ?片???度取余?是??
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
            return mImageViews[position % mImageViews.length];
        }



    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % mImageViews.length);
    }

    /**
     * ?置?中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.ch_dot);
            }else{
                tips[i].setBackgroundResource(R.drawable.unch_dot);
            }
        }
    }

}