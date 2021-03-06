package fju.im2016.com.hm.ui.youtube;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.dbhelper.DBHelper;
import fju.im2016.com.hm.ui.main.IndexActivity;

public class YoutubeActivity extends AppCompatActivity {

    private ArrayList<ListObject> myFavoriteList = new ArrayList<ListObject>();
    private MyWebChromeClient mWebChromeClient = null;
    private View mCustomView;
    private RelativeLayout mContentView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private TableLayout youtubeTableLayout;
    private ImageButton favoriteButton,goToFavoriteButton,indexButton,goToYoutubeButton;
    private WebView mWebView;
    private SQLiteDatabase db;
    private DBHelper helper;
    private FloatingActionButton fabFavorite,fabHome;
    String url,go_back_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube);

        if (!isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(YoutubeActivity.this);
            AlertDialog alert;
            builder.setTitle("提醒");
            builder.setMessage("尚未開啟網路，請開啟網路連線!");
            builder.setCancelable(false);
            builder.setPositiveButton("確認", new DialogInterface.OnClickListener()

                    {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent it = new Intent(android.provider.Settings.ACTION_SETTINGS);
                            startActivity(it);
                        }
                    }

            );
            alert = builder.create();
            alert.show();




        }

        db = openOrCreateDatabase("music_database", MODE_PRIVATE, null);
        helper = new DBHelper(getApplicationContext());

        youtubeTableLayout = (TableLayout)findViewById(R.id.youtubeTableLayout);

        favoriteButton = (ImageButton)findViewById(R.id.add_favorite);
        goToFavoriteButton = (ImageButton)findViewById(R.id.favorite);
        indexButton = (ImageButton)findViewById(R.id.index);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkInFavorite = false;
                int index;
                url = mWebView.getUrl();
                index = url.indexOf("v=");
                url = mWebView.getUrl().substring(index + 2, url.length());  //取影片id

                final String title = mWebView.getTitle();

                //判斷是否在我的最愛清單裡

                Cursor clist = helper.select_youtube_song();
                clist.moveToFirst();
                for (int i = 0; i < clist.getCount(); i++) {
                    String id = clist.getString(clist.getColumnIndex("youtube_id"));
                    if (id.equals(url)) {
                        checkInFavorite = true;
                    }

                    clist.moveToNext();
                }
                clist.close();


                if (checkInFavorite) {
                    Toast.makeText(getApplicationContext(), "已加入過我的最愛", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(YoutubeActivity.this)
                            .setTitle("確認加入")
                            .setMessage("確認加入我的最愛?")
                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNeutralButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ListObject myNewFavorite = new ListObject(url, title);
                                    helper.add_youtube_song(myNewFavorite);
                                    addToFavoriteList();
                                    Toast.makeText(getApplicationContext(), "已加入", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();

                }
            }
        });

        goToFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(YoutubeActivity.this, FavoriteActivity.class);
                it.putExtra("list", (Serializable) myFavoriteList);
                startActivity(it);
            }
        });

        indexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(YoutubeActivity.this, IndexActivity.class);
                startActivity(it);
            }
        });


        mWebView = (WebView) findViewById(R.id.webView);

        WebSettings ws = mWebView.getSettings();   //取得websocket的設定權力
        mWebChromeClient = new MyWebChromeClient();
        //使webview可以讀取javascript
        ws.setJavaScriptEnabled(true);
        //設置加載進來的頁面自適應手機屏幕
        ws.setUseWideViewPort(true); //可任意比例縮放
        ws.setLoadWithOverviewMode(true);
        mWebView.setWebChromeClient(mWebChromeClient);  //輔助WebView處理Javascript的對話方塊、網站圖示、網站title、載入進度等
        mWebView.setWebViewClient(mWebViewClient);    //幫助WebView處理各種通知、請求事件
        mWebView.setInitialScale(1);    //設定webview顯示大小
        mWebView.goBack();  //使webview可以返回

        Intent intent = getIntent();
        url = intent.getStringExtra("url");



        //fab前往我的最愛
        fabFavorite = (FloatingActionButton) findViewById(R.id.fab_favorite);
        fabFavorite.setFocusable(true);
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(YoutubeActivity.this, FavoriteActivity.class);
                startActivity(it);
            }
        });


        fabHome = (FloatingActionButton) findViewById(R.id.fab_home);
        fabHome.setFocusable(true);
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(YoutubeActivity.this, IndexActivity.class);
                startActivity(it);
            }
        });

        //開啟webview初始頁面
        if (url == null) {
            mWebView.loadUrl("https://www.youtube.com");
        } else {
            String temp = "https://www.youtube.com/watch?v=";
            temp += url;
            mWebView.loadUrl(temp);
        }
    }


    //webview返回方法

    @Override
    public void onBackPressed() {

        if (mWebView.canGoBack())
            mWebView.goBack();
        else
            super.onBackPressed();
    }


    //toolbar配置layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }



    //加入我的最愛

    public void addToFavoriteList() {
        int index;
        String title, url;
        url = mWebView.getUrl();
        index = url.indexOf("v=");
        url = mWebView.getUrl().substring(index + 2, url.length());  //取影片id
        title = mWebView.getTitle();
        ListObject favoriteObject = new ListObject(url, title);
        myFavoriteList.add(favoriteObject);
    }

    //webViewClient設定

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            go_back_url = url;
        }


        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            if (url.indexOf("v=") != -1) {
                //toolbar.setVisibility(View.GONE);
                youtubeTableLayout.setVisibility(View.VISIBLE);
                fabFavorite.hide();
                fabHome.hide();
            }else{
                //toolbar.setVisibility(View.GONE);
                youtubeTableLayout.setVisibility(View.GONE);
                fabFavorite.show();
                fabHome.show();
            }
        }

    };

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private class MyWebChromeClient extends WebChromeClient {


        FrameLayout.LayoutParams LayoutParameters = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);



        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mContentView = (RelativeLayout) findViewById(R.id.youtube);
            mContentView.setVisibility(View.GONE);
            mCustomViewContainer = new FrameLayout(YoutubeActivity.this);
            mCustomViewContainer.setLayoutParams(LayoutParameters);
            mCustomViewContainer.setBackgroundResource(android.R.color.black);
            view.setLayoutParams(LayoutParameters);
            mCustomViewContainer.addView(view);
            mCustomView = view;
            mCustomViewCallback = callback;
            mCustomViewContainer.setVisibility(View.VISIBLE);
            setContentView(mCustomViewContainer);
        }

        @Override
        public void onHideCustomView() {
            if (mCustomView == null) {
                return;
            } else {
                getSupportActionBar().show();
                // Hide the custom view.
                mCustomView.setVisibility(View.GONE);
                // Remove the custom view from its container.
                mCustomViewContainer.removeView(mCustomView);
                mCustomView = null;
                mCustomViewContainer.setVisibility(View.GONE);
                mCustomViewCallback.onCustomViewHidden();
                // Show the content view.
                mContentView.setVisibility(View.VISIBLE);
                setContentView(mContentView);
            }
        }
    }

}
