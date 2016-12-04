package fju.im2016.com.hm.ui.main;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.CheckColorList;
import fju.im2016.com.hm.core.entity.PageEnum;
import fju.im2016.com.hm.core.entity.PlayList;
import fju.im2016.com.hm.core.entity.RepeatEnum;
import fju.im2016.com.hm.core.entity.Song;
import fju.im2016.com.hm.core.entity.SongOfList;
import fju.im2016.com.hm.core.entity.player.Player;
import fju.im2016.com.hm.core.manager.SlidingLayoutManager;
import fju.im2016.com.hm.core.manager.SongManager;
import fju.im2016.com.hm.core.service.BroadcastService;
import fju.im2016.com.hm.dbhelper.DBHelper;
import fju.im2016.com.hm.presenter.player.PlayerPresenter;
import fju.im2016.com.hm.presenter.player.PlayerPresenterImpl;
import fju.im2016.com.hm.ui.IntelligentPlayer.AlarmReceiver;
import fju.im2016.com.hm.ui.album.AlbumFragment;
import fju.im2016.com.hm.ui.album.AlbumSongFragment;
import fju.im2016.com.hm.ui.artist.ArtistFragment;
import fju.im2016.com.hm.ui.artist.ArtistSongFragment;
import fju.im2016.com.hm.ui.component.MusicListAdapter;
import fju.im2016.com.hm.ui.component.PlayListAdapter;
import fju.im2016.com.hm.ui.latelyplay.LatelyPlayFragment;
import fju.im2016.com.hm.ui.manual.ManualActivity;
import fju.im2016.com.hm.ui.player.PlayerFragment;
import fju.im2016.com.hm.ui.player.PlayerView;
import fju.im2016.com.hm.ui.playlist.ListSongFragment;
import fju.im2016.com.hm.ui.playlist.PlayListFragment;
import fju.im2016.com.hm.ui.setting.SettingActivity;
import fju.im2016.com.hm.ui.sleepclock.SleepClockActivity;
import fju.im2016.com.hm.ui.youtube.FavoriteActivity;
import fju.im2016.com.hm.ui.youtube.YoutubeActivity;


public class MainActivity extends AppCompatActivity implements PlayerView, ListView.OnItemClickListener, PlayerFragment.OnItemClickCallBack, PlayListFragment.OnPageChangeCallBack, ListSongFragment.OnItemClickCallBack, ArtistFragment.OnPageChangeCallBack, ArtistSongFragment.OnItemClickCallBack, AlbumFragment.OnPageChangeCallBack, AlbumSongFragment.OnItemClickCallBack, SearchView.OnQueryTextListener, LatelyPlayFragment.OnPageChangeCallBack{

    private TextView albumName, musicName, runTime, fullTime, panelAlbumName, panelSongName;
    private SeekBar seekBar;
    private ImageView albumImage;
    private ImageButton btnPlay, btnFront, btnNext, btnRe, btnRandom, panelPlay, panelFront, panelNext, btnRed, btnOrange, btnYellow, btnGreen, btnBlue;
    private Boolean Random = false;
    private boolean updateRedImg = false;
    private boolean updateOrangeImg = false;
    private boolean updateYellowImg = false;
    private boolean updateGreenImg = false;
    private boolean updateBlueImg = false;
    private boolean inRedPlayList = false;
    private boolean inOrangePlayList = false;
    private boolean inYellowPlayList = false;
    private boolean inGreenPlayList = false;
    private boolean inBluePlayList = false;
    private RepeatEnum Repeat = RepeatEnum.repeatOff;

    private PlayerPresenter playerPresenter;
    private SongManager songManager;

    private SQLiteDatabase db;
    private DBHelper helper;
    private CheckColorList checkColorList;
    private List<SongOfList> songOfLists;
    private SongOfList songOfList;

    private MusicListAdapter musicListAdapter;

    private char[] charArray;
    private String newAddress, newFilename;
    private int countAddress;
    private File file;

    private List<PlayList> playLists;
    private PlayListAdapter playListAdapter;
    private ListView lstPlaylist;
    private EditText editText;
    private AlertDialog theDialog;
    private int choseList;
    private boolean songInList = false;

    private String nowTitle;

    private int navItemId;
    private boolean showPanel = true;
    private boolean hidePanel = false;
    private boolean isExpanded = false;
    private boolean isCollapsed = false;
    private static final int ACTIVITY_SLEEP = 1;
    private static final String NAV_ITEM_ID = "nav_index";
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FragmentTransaction fragmentTransaction;
    private Menu menu;
    private LinearLayout dragViewLayout, bottomPanelLeanerLayout;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ImageButton btn_expand;
    SlidingLayoutManager c;

    private int duration;

    private SensorManager mSensorManager;   //體感(Sensor)使用管理
    private Sensor mSensor;                 //體感(Sensor)類別
    private float mLastX;                    //x軸體感(Sensor)偏移
    private float mLastY;                    //y軸體感(Sensor)偏移
    private float mLastZ;                    //z軸體感(Sensor)偏移
    private double mSpeed;                 //甩動力道數度
    private long mLastUpdateTime;           //觸發時間

    //甩動力道數度設定值 (數值越大需甩動越大力，數值越小輕輕甩動即會觸發)
    private static final int SPEED_SHRESHOLD = 3000;

    //觸發間隔時間
    private static final int UPTATE_INTERVAL_TIME = 70;

    @Override
    public void onClick(SongManager songManager) {
        this.playerPresenter.setPause(false);
        this.iniPlayer(songManager);
        this.playerPresenter.setBaseIndex();
        this.playerPresenter.establishSongIndexArray();
        this.playerPresenter.resetIndexRef();
        this.playerPresenter.resetLastIndexValue();
        this.playerPresenter.clear();
        setRepeatOnce();
        this.checkColorList();
        addToLatelyPlay();
    }

    @Override
    public void sendAdapter(MusicListAdapter musicListAdapter) {
        this.musicListAdapter = musicListAdapter;
    }

    @Override
    public void setToolBarTitle(String toolBarTitle) {
        this.nowTitle = toolBarTitle;
        this.getSupportActionBar().setTitle(toolBarTitle);
    }

    private void iniPlayer(SongManager songManager) {
        this.playerPresenter.iniSongManager(songManager);
        try {
            this.playerPresenter.iniMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.playerPresenter.updateView();
    }

    private void getInformation(Cursor c) {
        for (int i = 0; i < c.getCount(); i++) {
            String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            String id = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            String artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            long albumId = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            double length = c.getDouble(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            double size = c.getDouble(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

            Song song = new Song(id, name, path);
            song.setAlbum(album);
            song.setAlbumId(albumId);
            song.setArtist(artist);
            song.setLength(length);
            song.setSize(size);
            this.songManager.addSong(song);

            c.moveToNext();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(vcCommand, new IntentFilter(SettingActivity.VC_BR));
        registerReceiver(intelligent_play, new IntentFilter(AlarmReceiver.IP_BR));

        this.iniButtonPlay();
        this.iniPanelPlay();
        this.iniButtonFront();
        this.iniPanelFront();
        this.iniButtonNext();
        this.iniPanelNext();
        this.iniButtonRe();
        this.iniButtonRandom();
        this.iniSeekBar();
        this.iniButtonRed();
        this.iniButtonOrange();
        this.iniButtonYellow();
        this.iniButtonGreen();
        this.iniButtonBlue();

        this.checkColorList = new CheckColorList(this);
        this.songOfLists = new ArrayList<SongOfList>();
        this.playLists = new ArrayList<PlayList>();

        albumName = (TextView) findViewById(R.id.albumName);
        musicName = (TextView) findViewById(R.id.musicName);
        runTime = (TextView) findViewById(R.id.runTime);
        fullTime = (TextView) findViewById(R.id.fullTime);
        panelAlbumName = (TextView) findViewById(R.id.panelAlbumName);
        panelSongName = (TextView) findViewById(R.id.panelSongName);
        albumImage = (ImageView) findViewById(R.id.albumImage);

        try {
            this.iniPlayerPresenter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.playerPresenter.setRepeatEnum(Repeat);

        this.songManager = new SongManager();

        Context ctx = this;
        ContentResolver resolver = ctx.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        getInformation(cursor);
        cursor.close();

        this.songManager.setCurrentSong(0);
        this.iniPlayer(songManager);
        this.playerPresenter.pause();
        this.updateBtnPlayImage();
        this.updatePanelPlayImage();
        this.checkColorList();

        // ------------------------------------------

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("全部歌曲");
        nowTitle = "全部歌曲";
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //shadow color
        drawerLayout.setScrimColor(Color.parseColor("#99d1e6b7"));
        //設置陰影
//        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,GravityCompat.START);
        navView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(navView);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();

        if (null != savedInstanceState) {
            navItemId = savedInstanceState.getInt(NAV_ITEM_ID, R.id.nav_item_all_songs);
        } else {
            navItemId = R.id.nav_item_all_songs;
        }

        navigateTo(navView.getMenu().findItem(navItemId));

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, new PlayerFragment());
        fragmentTransaction.commit();


        bottomPanelLeanerLayout = (LinearLayout)findViewById(R.id.bottomPanel);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        btn_expand = (ImageButton) findViewById(R.id.btn_expand);
        btn_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        dragViewLayout = (LinearLayout)findViewById(R.id.dragView);
        c = new SlidingLayoutManager(MainActivity.this);
        c.setDragView(dragViewLayout);

//        c.setClickToCollapseEnabled(true);
//        slidingUpPanelLayout.setTouchEnabled(false);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slidingUpPanelLayout != null) {
                    if (slideOffset > 0.95f && showPanel == true) {
                        collapse(bottomPanelLeanerLayout);
                        showPanel = false;
                        hidePanel = true;
                    } else if (slideOffset < 0.95f && hidePanel == true) {
                        expand(bottomPanelLeanerLayout);
                        showPanel = true;
                        hidePanel = false;
                    }
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (slidingUpPanelLayout != null) {
                    if (newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                    {
                        isExpanded = true;
                        isCollapsed = false;
                        getSupportActionBar().setTitle("目前播放");
                        toolbar.setNavigationIcon(R.drawable.ic_collapse);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }
                        });
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
                        hideOption(R.id.action_search);
                        hideOption(R.id.action_home);
                        showOption(R.id.action_setRingtone);
                        showOption(R.id.action_sleepClock);
                        showOption(R.id.action_addToPalyLists);
                        showOption(R.id.action_moreInfo);
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#778e8e8e")));
                    } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                    {
                        isExpanded = false;
                        isCollapsed = true;
                        panelSongName.requestFocus();

                        getSupportActionBar().setTitle("" + nowTitle);
                        if (nowTitle.equals("全部歌曲"))
                            showOption(R.id.action_search);

                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
                        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
                            @Override
                            public void onDrawerClosed(View drawerView) {
                                super.onDrawerClosed(drawerView);
                            }

                            @Override
                            public void onDrawerOpened(View drawerView) {
                                super.onDrawerOpened(drawerView);
                            }

                            @Override
                            public void onDrawerSlide(View drawerView, float slideOffset) {
                                super.onDrawerSlide(drawerView, 0); // this disables the animation
                            }
                        };
                        drawerLayout.addDrawerListener(actionBarDrawerToggle);
                        actionBarDrawerToggle.syncState();
                        showOption(R.id.action_home);
                        hideOption(R.id.action_setRingtone);
                        hideOption(R.id.action_sleepClock);
                        hideOption(R.id.action_addToPalyLists);
                        hideOption(R.id.action_moreInfo);
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8e8e8e")));
                    } else if (newState == SlidingUpPanelLayout.PanelState.ANCHORED)
                    {
                        if (isExpanded )
                        {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                }
                            },100);
                        }else if (isCollapsed )
                        {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                                }
                            },100);
                        }
                    }
                }
            }
        });
        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


        this.iniSensor();
    }

    private void iniSensor() {
        //取得體感(Sensor)服務使用權限
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        //取得手機Sensor狀態設定
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //註冊體感(Sensor)甩動觸發Listener
        mSensorManager.registerListener(SensorListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private SensorEventListener SensorListener = new SensorEventListener()
    {
        public void onSensorChanged(SensorEvent mSensorEvent)
        {
            //當前觸發時間
            long mCurrentUpdateTime = System.currentTimeMillis();

            //觸發間隔時間 = 當前觸發時間 - 上次觸發時間
            long mTimeInterval = mCurrentUpdateTime - mLastUpdateTime;

            //若觸發間隔時間< 70 則return;
            if (mTimeInterval < UPTATE_INTERVAL_TIME) return;

            mLastUpdateTime = mCurrentUpdateTime;

            //取得xyz體感(Sensor)偏移
            float x = mSensorEvent.values[0];
            float y = mSensorEvent.values[1];
            float z = mSensorEvent.values[2];

            //甩動偏移速度 = xyz體感(Sensor)偏移 - 上次xyz體感(Sensor)偏移
            float mDeltaX = x - mLastX;
            float mDeltaY = y - mLastY;
            float mDeltaZ = z - mLastZ;

            mLastX = x;
            mLastY = y;
            mLastZ = z;

            //體感(Sensor)甩動力道速度公式
            mSpeed = Math.sqrt(mDeltaX * mDeltaX + mDeltaY * mDeltaY + mDeltaZ * mDeltaZ)/ mTimeInterval * 10000;

            //若體感(Sensor)甩動速度大於等於甩動設定值則進入 (達到甩動力道及速度)
            if (mSpeed >= SPEED_SHRESHOLD)
            {
                //達到搖一搖甩動後要做的事情
                Log.d("TAG", "搖一搖中...");
                try {
                    playerPresenter.setPause(false);
                    playerPresenter.next();
                    setRepeatOnce();
                    checkColorList();
                    addToLatelyPlay();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor , int accuracy)
        {
        }
    };

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        switch(menuItem.getItemId()) {
            case R.id.nav_item_all_songs:
                if (!nowTitle.equals("全部歌曲")) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, new PlayerFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    showOption(R.id.action_search);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.nav_item_album:
                if (!nowTitle.equals("專輯")) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, new AlbumFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    hideOption(R.id.action_search);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.nav_item_artist:
                if (!nowTitle.equals("演出者")) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, new ArtistFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    hideOption(R.id.action_search);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.nav_item_playlist:
                if (!nowTitle.equals("我的播放清單")) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, new PlayListFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    hideOption(R.id.action_search);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.nav_item_lately:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, new LatelyPlayFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    hideOption(R.id.action_search);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                break;
            case R.id.nav_item_youtube:
                Intent it = new Intent(MainActivity.this, YoutubeActivity.class);
                startActivity(it);
                break;
            case R.id.nav_item_setting:
                startActivity(new Intent().setClass(MainActivity.this, SettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.nav_item_manual:
                startActivity(new Intent().setClass(MainActivity.this, ManualActivity.class));
                break;
            default:
                return;
        }


//        Toast.makeText(MainActivity.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
        toolbar.setTitle(menuItem.getTitle());
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.musicListAdapter.getFilter().filter(newText);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        showOption(R.id.action_search);
        showOption(R.id.action_home);
        hideOption(R.id.action_setRingtone);
        hideOption(R.id.action_sleepClock);
        hideOption(R.id.action_addToPalyLists);
        hideOption(R.id.action_moreInfo);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
//
//        SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
//        }

        // hide toast
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                final View v = findViewById(R.id.action_home);
//
//                if (v != null) {
//                    v.setOnLongClickListener(new View.OnLongClickListener() {
//                        @Override
//                        public boolean onLongClick(View v) {
//                            return false;
//                        }
//                    });
//                }
//            }
//        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                return true;

            case R.id.action_home:
                startActivity(new Intent().setClass(MainActivity.this, IndexActivity.class));
                return true;

            case R.id.action_setRingtone:
                setRingtoneAlert();
                return true;

            case R.id.action_sleepClock:
                registerReceiver(brFinish, new IntentFilter(BroadcastService.FINISH_BR));
                Intent intent = new Intent(MainActivity.this, SleepClockActivity.class);
                startActivityForResult(intent, ACTIVITY_SLEEP);
                return true;

            case R.id.action_addToPalyLists:
                queryPlayList();
                AlertDialog.Builder addlistDialog = new AlertDialog.Builder(this);
                iniChoseListDialog(addlistDialog, LayoutInflater.from(this).inflate(R.layout.add_list_dialog_layout, null));
                enableDialog(false);
                initialPlayList();
                return true;

            case R.id.action_moreInfo:
                LayoutInflater inflater_more = LayoutInflater.from(MainActivity.this);
                ScrollView more = (ScrollView) inflater_more.inflate(R.layout.moreinformation, null);
                AlertDialog.Builder dialog_more = new AlertDialog.Builder(MainActivity.this);
                TextView m_name = (TextView) more.findViewById(R.id.m_name);
                m_name.setText(this.playerPresenter.getCurrentSong().getName());
                TextView m_singer = (TextView) more.findViewById(R.id.m_singer);
                m_singer.setText(this.playerPresenter.getCurrentSong().getArtist());
                TextView m_album = (TextView) more.findViewById(R.id.m_album);
                m_album.setText(this.playerPresenter.getCurrentSong().getAlbum());
                TextView m_long = (TextView) more.findViewById(R.id.m_long);
                m_long.setText(this.minSecFormat(duration));
                TextView m_path = (TextView) more.findViewById(R.id.m_path);
                m_path.setText(this.playerPresenter.getCurrentSong().getPath());
                TextView m_size = (TextView) more.findViewById(R.id.m_size);
                m_size.setText(""+String.format("%.2f", (this.playerPresenter.getCurrentSong().getSize() / (1024 * 1024)))+" MB");
                dialog_more.setView(more);
                dialog_more.setIcon(R.drawable.ic_info);
                dialog_more.setTitle(" 更多內容");

                dialog_more.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                dialog_more.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setRingtoneAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_note);
        dialog.setTitle(" 確認設定 ? ");
        dialog.setMessage("確定要將這首歌設為您的鈴聲 ? ");
        dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                newAddress = "";
                newFilename = "";
                charArray = String.valueOf(playerPresenter.getCurrentSong().getPath()).toCharArray();
                getCountAddress();
                setFile(getNewAddress(), getNewFilename());
                setRingtones();
                listToast(R.drawable.ic_ring, playerPresenter.getCurrentSong().getName() + " 已被設為鈴聲");
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    private void enableDialog(boolean enabled) {
        theDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(enabled);
    }

    private void iniChoseListDialog(AlertDialog.Builder alertDialog, View view) {
        this.lstPlaylist = (ListView) view.findViewById(R.id.lstPlayList);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db = openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
                helper = new DBHelper(getApplicationContext());
                helper.addsong(Integer.parseInt(playerPresenter.getCurrentSong().getId()), Integer.parseInt(playLists.get(choseList).getId()));
                db.close();
                helper.close();
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        theDialog = alertDialog.create();
        theDialog.show();
    }

    private void iniAddListDialog(AlertDialog.Builder alertDialog, View view) {
        this.editText = (EditText) view.findViewById(R.id.addList_editText);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO empty and duplicated, selected after addList
                db = openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
                helper = new DBHelper(getApplicationContext());
                helper.newlist(editText.getText().toString());
                db.close();
                helper.close();
                PlayList addPlayList = new PlayList(findId(editText.getText().toString()), editText.getText().toString());
                addPlayList.setColorImg(R.drawable.list_purple);
                playLists.add(playLists.size() - 1, addPlayList);
                playListAdapter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    private String findId(String listName) {
        this.db = openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        this.helper = new DBHelper(getApplicationContext());
        Cursor listWithFindedName = db.rawQuery("select * from list where list_name= '" + listName + "'", null);
        listWithFindedName.moveToFirst();
        String findedId = listWithFindedName.getString(listWithFindedName.getColumnIndex("_id"));
        listWithFindedName.close();
        this.db.close();
        this.helper.close();
        return findedId;
    }

    private void queryPlayList() {
        if (playLists != null)
            playLists.clear();
        this.db = this.openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        this.helper = new DBHelper(this.getApplicationContext());
        Cursor clist =db.rawQuery("select * from list", null);
        clist.moveToFirst();
        getCListInformation(clist);
        clist.close();
        this.db.close();
        this.helper.close();

        PlayList addPlayList = new PlayList(null, "新增播放清單");
        addPlayList.setColorImg(R.drawable.ic_plus_black);
        this.playLists.add(addPlayList);
    }

    private void initialPlayList() {
        this.playListAdapter = new PlayListAdapter(this.playLists, this, false);
        this.lstPlaylist.setAdapter(playListAdapter);
        this.lstPlaylist.setSelector(R.color.nav_item_background);
        this.lstPlaylist.setOnItemClickListener(this);
    }

    private void getCListInformation(Cursor clist) {
        for (int i = 0; i < clist.getCount(); i++) {
            String id = clist.getString(clist.getColumnIndex("_id"));
            String name = clist.getString(clist.getColumnIndex("list_name"));

            PlayList playList = new PlayList(id, name);
            switch (i) {
                case 0:
                    playList.setColorImg(R.drawable.list_red);
                    break;
                case 1:
                    playList.setColorImg(R.drawable.list_orange);
                    break;
                case 2:
                    playList.setColorImg(R.drawable.list_yellow);
                    break;
                case 3:
                    playList.setColorImg(R.drawable.list_green);
                    break;
                case 4:
                    playList.setColorImg(R.drawable.list_blue);
                    break;
                default:
                    playList.setColorImg(R.drawable.list_purple);
            }
            this.playLists.add(playList);

            clist.moveToNext();
        }
    }

    private boolean checkExist() {
        for (int i = 0; i < this.songOfLists.size(); i++) {
            if (this.playerPresenter.getCurrentSong().getId().equals(this.songOfLists.get(i).getSongId())) {
                this.songInList = true;
            }
        }
        return this.songInList;
    }

    private void showDuplicatedDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_warning);
        dialog.setTitle("警告");
        dialog.setMessage("這首歌有囉!");
        dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        this.songInList = false;
        this.choseList = position;
        if (position != playLists.size() - 1) {
            this.querySongOfList(this.playLists.get(position).getId());
            if (this.checkExist()) {
                this.showDuplicatedDialog();
                this.enableDialog(false);
            } else {
                this.enableDialog(true);
            }
        } else {
            this.enableDialog(false);
            AlertDialog.Builder addListDialog = new AlertDialog.Builder(this);
            this.iniAddListDialog(addListDialog, LayoutInflater.from(this).inflate(R.layout.add_list, null));
        }
    }

    private int getCountAddress() {
        for (int i = 0; i < this.charArray.length; i++) {
            if (this.charArray[i] == '/') {
                this.countAddress = i;
            }
        }
        return this.countAddress;
    }

    private String getNewAddress() {
        for (int i = 0; i <= this.countAddress; i++) {
            this.newAddress += this.charArray[i];
        }
        return this.newAddress;
    }

    private String getNewFilename() {
        for (int i = this.countAddress + 1; i < this.charArray.length; i++) {
            this.newFilename += this.charArray[i];
        }
        return this.newFilename;
    }

    private File getFile() {
        return this.file;
    }

    private void setFile(String address, String filename) {
        this.file = new File(address, filename);
    }

    private void setRingtones() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, this.getFile().getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, this.newFilename);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");

        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(this.getFile().getAbsolutePath());
        this.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + this.getFile().getAbsolutePath() + "\"", null);
        Uri newUri = this.getApplicationContext().getContentResolver().insert(uri, values);

        RingtoneManager.setActualDefaultRingtoneUri(this.getApplicationContext(), RingtoneManager.TYPE_RINGTONE, newUri);
    }

    private BroadcastReceiver brFinish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            playerPresenter.pause();
            updateBtnPlayImage();
            updatePanelPlayImage();
            // or whatever method used to update your GUI fields
        }
    };

    private void navigateTo(MenuItem menuItem) {
//        contentView.setText(menuItem.getTitle());

        navItemId = menuItem.getItemId();
        menuItem.setChecked(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, navItemId);
    }

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 10);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 3);
        v.startAnimation(a);
    }

    private void hideOption(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    private void setOptionTitle(int id, String title)
    {
        MenuItem item = menu.findItem(id);
        item.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout != null && (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ))
        {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
            if (nowTitle.equals("全部歌曲"))
                showOption(R.id.action_search);
        }
    }

    // ----------------------------
    private void iniPlayerPresenter() throws IOException {
        this.playerPresenter = new PlayerPresenterImpl(this);
    }

    private void iniButtonPlay() {
        this.btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        this.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.clickPlay();
                updateBtnPlayImage();
                updatePanelPlayImage();
            }
        });
        this.btnPlay.setImageResource(R.drawable.play);
    }

    private void iniPanelPlay() {
        this.panelPlay = (ImageButton) findViewById(R.id.panel_play);
        this.panelPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.clickPlay();
                updateBtnPlayImage();
                updatePanelPlayImage();
            }
        });
        this.panelPlay.setBackgroundResource(R.drawable.p_play);
    }

    @Override
    public void updateBtnPlayImage() {
        if (this.playerPresenter.isPause()) {
            this.btnPlay.setImageResource(R.drawable.play);
        } else {
            this.btnPlay.setImageResource(R.drawable.stop);
        }
    }

    @Override
    public void updatePanelPlayImage() {
        if (this.playerPresenter.isPause()) {
            this.panelPlay.setBackgroundResource(R.drawable.p_play);
        } else {
            this.panelPlay.setBackgroundResource(R.drawable.p_stop);
        }
    }

    private void iniButtonFront() {
        this.btnFront = (ImageButton) findViewById(R.id.btnFront);
        this.btnFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    playerPresenter.setPause(false);
                    playerPresenter.last();
                    setRepeatOnce();
                    checkColorList();
                    addToLatelyPlay();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void iniPanelFront() {
        this.panelFront = (ImageButton) findViewById(R.id.panel_front);
        this.panelFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    playerPresenter.setPause(false);
                    playerPresenter.last();
                    setRepeatOnce();
                    checkColorList();
                    addToLatelyPlay();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void iniButtonNext() {
        this.btnNext = (ImageButton) findViewById(R.id.btnNext);
        this.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    playerPresenter.setPause(false);
                    playerPresenter.next();
                    setRepeatOnce();
                    checkColorList();
                    addToLatelyPlay();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void iniPanelNext() {
        this.panelNext = (ImageButton) findViewById(R.id.panel_next);
        this.panelNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    playerPresenter.setPause(false);
                    playerPresenter.next();
                    setRepeatOnce();
                    checkColorList();
                    addToLatelyPlay();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setRepeatOnce() {
        if (playerPresenter.getRepeatEnum() == RepeatEnum.repeatOnce) {
            playerPresenter.setRepeat(true);
        }
    }

    private void iniButtonRe() {
        this.btnRe = (ImageButton) findViewById(R.id.btnRe);
        this.btnRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Repeat == RepeatEnum.repeatOff) {
                    Repeat = RepeatEnum.repeatOn;
                    playerPresenter.setRepeat(false);
                } else if (Repeat == RepeatEnum.repeatOn) {
                    Repeat = RepeatEnum.repeatOnce;
                    playerPresenter.setRepeat(true);
                } else if (Repeat == RepeatEnum.repeatOnce) {
                    Repeat = RepeatEnum.repeatOff;
                    playerPresenter.setRepeat(false);
                }
                playerPresenter.setRepeatEnum(Repeat);
                updateBtnReImage();
            }
        });
    }

    private void updateBtnReImage() {
        if (this.Repeat == RepeatEnum.repeatOff) {
            btnRe.setImageDrawable(getResources().getDrawable(R.drawable.re_off));
        } else if (this.Repeat == RepeatEnum.repeatOn) {
            btnRe.setImageDrawable(getResources().getDrawable(R.drawable.re_on));
        } else if (this.Repeat == RepeatEnum.repeatOnce) {
            btnRe.setImageDrawable(getResources().getDrawable(R.drawable.re_only));
        }
    }

    private void iniButtonRandom() {
        this.btnRandom = (ImageButton) findViewById(R.id.btnRandom);
        this.btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBtnRandomImage();
                playerPresenter.switchPlayMode();
                if (Random) {
                    playerPresenter.setRandom(true);
                } else {
                    playerPresenter.setRandom(false);
                }
            }
        });
    }

    private void updateBtnRandomImage() {
        if (Random) {
            btnRandom.setImageDrawable(getResources().getDrawable(R.drawable.random_off));
            Random = false;
        } else {
            btnRandom.setImageDrawable(getResources().getDrawable(R.drawable.random_on));
            Random = true;
        }
    }

    private void addToLatelyPlay() {
        db = openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        helper = new DBHelper(getApplicationContext());
        Cursor cLately = helper.select_lately_play();
        if (cLately.getCount() < 15) {
            helper.addLatelyPlay(Integer.parseInt(playerPresenter.getCurrentSong().getId()));
        } else if (cLately.getCount() == 15) {
            for (int i = 2; i < 16 ; i++) {
                Cursor cSongId = helper.select_lately_play(i);
                cSongId.moveToFirst();
                int tempSongId = cSongId.getInt(cSongId.getColumnIndex("s_id"));
                cSongId.close();
                helper.updateLatelyPlay(i - 1, tempSongId);
            }
            helper.updateLatelyPlay(15, Integer.parseInt(playerPresenter.getCurrentSong().getId()));
        }
        db.close();
        helper.close();
    }

    private void listToast(int img, String string) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(img);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(string);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void iniButtonRed() {
        this.btnRed = (ImageButton) findViewById(R.id.btnRed);
        this.btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inRedPlayList) {
                    listToast(R.drawable.ic_delete, "移除自紅色清單");
                    deleteSongFromList("1");
                    inRedPlayList = false;
                } else {
                    listToast(R.drawable.ic_tick, "已加入紅色清單");
                    addSongToList(1);
                    inRedPlayList = true;
                }
                updateBtnRedImg();
            }
        });
    }

    private void updateBtnRedImg() {
        if (updateRedImg) {
            this.btnRed.setImageResource(R.drawable.list_red_checked);
            this.updateRedImg = false;
            this.inRedPlayList = true;
        } else {
            this.btnRed.setImageResource(R.drawable.list_red);
            this.updateRedImg = true;
            this.inRedPlayList = false;
        }
    }

    private void iniButtonOrange() {
        this.btnOrange = (ImageButton) findViewById(R.id.btnOrange);
        this.btnOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inOrangePlayList) {
                    listToast(R.drawable.ic_delete, "移除自橙色清單");
                    deleteSongFromList("2");
                    inOrangePlayList = false;
                } else {
                    listToast(R.drawable.ic_tick, "已加入橙色清單");
                    addSongToList(2);
                    inOrangePlayList = true;
                }
                updateBtnOrangeImg();
            }
        });
    }

    private void updateBtnOrangeImg() {
        if (updateOrangeImg) {
            this.btnOrange.setImageResource(R.drawable.list_orange_checked);
            this.updateOrangeImg = false;
            this.inOrangePlayList = true;
        } else {
            this.btnOrange.setImageResource(R.drawable.list_orange);
            this.updateOrangeImg = true;
            this.inOrangePlayList = false;
        }
    }

    private void iniButtonYellow() {
        this.btnYellow = (ImageButton) findViewById(R.id.btnYellow);
        this.btnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inYellowPlayList) {
                    listToast(R.drawable.ic_delete, "移除自黃色清單");
                    deleteSongFromList("3");
                    inYellowPlayList = false;
                } else {
                    listToast(R.drawable.ic_tick, "已加入黃色清單");
                    addSongToList(3);
                    inYellowPlayList = true;
                }
                updateBtnYellowImg();
            }
        });
    }

    private void updateBtnYellowImg() {
        if (updateYellowImg) {
            this.btnYellow.setImageResource(R.drawable.list_yellow_checked);
            this.updateYellowImg = false;
            this.inYellowPlayList = true;
        } else {
            this.btnYellow.setImageResource(R.drawable.list_yellow);
            this.updateYellowImg = true;
            this.inYellowPlayList = false;
        }
    }

    private void iniButtonGreen() {
        this.btnGreen = (ImageButton) findViewById(R.id.btnGreen);
        this.btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inGreenPlayList) {
                    listToast(R.drawable.ic_delete, "移除自綠色清單");
                    deleteSongFromList("4");
                    inGreenPlayList = false;
                } else {
                    listToast(R.drawable.ic_tick, "已加入綠色清單");
                    addSongToList(4);
                    inGreenPlayList = true;
                }
                updateBtnGreenImg();
            }
        });
    }

    private void updateBtnGreenImg() {
        if (updateGreenImg) {
            this.btnGreen.setImageResource(R.drawable.list_green_checked);
            this.updateGreenImg = false;
            this.inGreenPlayList = true;
        } else {
            this.btnGreen.setImageResource(R.drawable.list_green);
            this.updateGreenImg = true;
            this.inGreenPlayList = false;
        }
    }

    private void iniButtonBlue() {
        this.btnBlue = (ImageButton) findViewById(R.id.btnBlue);
        this.btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inBluePlayList) {
                    listToast(R.drawable.ic_delete, "移除自藍色清單");
                    deleteSongFromList("5");
                    inBluePlayList = false;
                } else {
                    listToast(R.drawable.ic_tick, "已加入藍色清單");
                    addSongToList(5);
                    inBluePlayList = true;
                }
                updateBtnBlueImg();
            }
        });
    }

    private void updateBtnBlueImg() {
        if (updateBlueImg) {
            this.btnBlue.setImageResource(R.drawable.list_blue_checked);
            this.updateBlueImg = false;
            this.inBluePlayList = true;
        } else {
            this.btnBlue.setImageResource(R.drawable.list_blue);
            this.updateBlueImg = true;
            this.inBluePlayList = false;
        }
    }

    private void checkColorRed() {
        if (this.checkColorList.findList("1")) {
            this.inRedPlayList = true;
            this.updateRedImg = true;
            this.updateBtnRedImg();
        } else {
            this.inRedPlayList = false;
            this.updateRedImg = false;
            this.updateBtnRedImg();
        }
    }

    private void checkColorOrange() {
        if (this.checkColorList.findList("2")) {
            this.inOrangePlayList = true;
            this.updateOrangeImg = true;
            this.updateBtnOrangeImg();
        } else {
            this.inOrangePlayList = false;
            this.updateOrangeImg = false;
            this.updateBtnOrangeImg();
        }
    }

    private void checkColorYellow() {
        if (this.checkColorList.findList("3")) {
            this.inYellowPlayList = true;
            this.updateYellowImg = true;
            this.updateBtnYellowImg();
        } else {
            this.inYellowPlayList = false;
            this.updateYellowImg = false;
            this.updateBtnYellowImg();
        }
    }

    private void checkColorGreen() {
        if (this.checkColorList.findList("4")) {
            this.inGreenPlayList = true;
            this.updateGreenImg = true;
            this.updateBtnGreenImg();
        } else {
            this.inGreenPlayList = false;
            this.updateGreenImg = false;
            this.updateBtnGreenImg();
        }
    }

    private void checkColorBlue() {
        if (this.checkColorList.findList("5")) {
            this.inBluePlayList = true;
            this.updateBlueImg = true;
            this.updateBtnBlueImg();
        } else {
            this.inBluePlayList = false;
            this.updateBlueImg = false;
            this.updateBtnBlueImg();
        }
    }

    @Override
    public void checkColorList() {
        this.checkColorList.setSong(this.playerPresenter.getCurrentSong());
        this.checkColorRed();
        this.checkColorOrange();
        this.checkColorYellow();
        this.checkColorGreen();
        this.checkColorBlue();
    }

    private void querySongOfList(String querySQL) {
        if (songOfLists != null)
            songOfLists.clear();
        this.db = openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        this.helper = new DBHelper(getApplicationContext());
        Cursor cSongOfList =db.rawQuery("select * from song_of_list where l_id = " + querySQL, null);
        cSongOfList.moveToFirst();
        getSongOfListInformation(cSongOfList);
        cSongOfList.close();
        this.db.close();
        this.helper.close();
    }

    private void getSongOfListInformation(Cursor cSongOfList) {
        for (int i = 0; i < cSongOfList.getCount(); i++) {
            String id = cSongOfList.getString(cSongOfList.getColumnIndex("_id"));
            String songId = cSongOfList.getString(cSongOfList.getColumnIndex("s_id"));
            String listId = cSongOfList.getString(cSongOfList.getColumnIndex("l_id"));

            SongOfList songOfList = new SongOfList(id, songId, listId);
            this.songOfLists.add(songOfList);

            cSongOfList.moveToNext();
        }
    }

    private void addSongToList(int listId) {
        this.db = openOrCreateDatabase("music_database", MODE_PRIVATE, null);
        this.helper = new DBHelper(getApplicationContext());
        helper.addsong(Integer.parseInt(playerPresenter.getCurrentSong().getId()), listId);
        this.db.close();
        this.helper.close();
    }

    private void deleteSongFromList(String listId) {
        querySongOfList(listId);
        for (int i = 0; i < songOfLists.size(); i++) {
            if (songOfLists.get(i).getSongId().equals(playerPresenter.getCurrentSong().getId())) {
                songOfList = songOfLists.get(i);
            }
        }
        this.db = openOrCreateDatabase("music_database", MODE_PRIVATE, null);
        this.helper = new DBHelper(getApplicationContext());
        helper.delete_song_of_list(Integer.parseInt(songOfList.getId()));
        this.db.close();
        this.helper.close();
    }

    private void iniSeekBar() {
        this.seekBar = (SeekBar) findViewById(R.id.seekBar);
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int currentPosition, boolean fromUser) {
                if (fromUser)
                    playerPresenter.onProgressChanged(currentPosition);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onRequestPermissionsRequest(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("取得權限")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("已取得權限，按確定紐結束應用程式後啟動。")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .show();
            } else {
                Toast.makeText(this, "未取得權限!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMusicPlay() {

    }

    @Override
    public void onMusicPause() {

    }

    @Override
    public void setView(Song currentSong, Player player) {
        duration = player.getDuration();
        this.musicName.setText(currentSong.getName());
        this.albumName.setText(currentSong.getAlbum() + " - " + currentSong.getArtist());
        this.panelSongName.setText(currentSong.getName());
        this.panelAlbumName.setText(currentSong.getAlbum());
        this.seekBar.setMax(duration);
        this.fullTime.setText(this.minSecFormat(duration));
        if (this.playerPresenter != null) {
            this.updateBtnPlayImage();
            this.updatePanelPlayImage();
        }
//        getEmbeddedPicture(playerPresenter.getCurrentSong().getPath());
        decodeSampledBitmapFromResource(playerPresenter.getCurrentSong().getPath(), albumImage, 100, 100);
    }

//    public void getEmbeddedPicture(String songPath){
//        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//        mmr.setDataSource(songPath);
//
//        byte [] data = mmr.getEmbeddedPicture();
//        //coverart is an Imageview object
//
//        // convert the byte array to a bitmap
//        if(data != null) {
//            Bitmap image =  BitmapFactory.decodeByteArray(data, 0, data.length);
//            if (image != null) {
//                Bitmap songImage = Bitmap.createScaledBitmap(image, 100, 100, true);
//                albumImage.setImageBitmap(image);
//            } else {
//                albumImage.setImageResource(R.drawable.album);
//            }
//        } else {
//            albumImage.setImageResource(R.drawable.album);
//        }
//    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void  decodeSampledBitmapFromResource(String songPath, ImageView albumImage, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songPath);

        byte [] data = mmr.getEmbeddedPicture();
        if(data != null) {
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        if(data != null) {
            Bitmap image =  BitmapFactory.decodeByteArray(data, 0, data.length, options);
            if (image != null) {
                albumImage.setImageBitmap(image);
            } else {
                albumImage.setImageResource(R.drawable.album);
            }
        } else {
            albumImage.setImageResource(R.drawable.album);
        }
    }

    @Override
    public void setTimeTextView(final int currentPosition) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runTime.setText(minSecFormat(currentPosition));
                seekBar.setProgress(currentPosition);
            }
        });
    }

    private String minSecFormat(int millisecond) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) millisecond) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) millisecond));
        return String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisecond), seconds);
    }

    // ---------------------------------

    //BroadcastReceiver，時間到要執行的事
    private BroadcastReceiver intelligent_play = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("intelligent_play").equals("play")) {
                playerPresenter.play();
                updateBtnPlayImage();
                updatePanelPlayImage();
            }
        }
    };

    private BroadcastReceiver vcCommand = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("vcCommand").equals("play")) {
                playerPresenter.play();
                updateBtnPlayImage();
                updatePanelPlayImage();
            } else if (intent.getStringExtra("vcCommand").equals("stop")) {
                playerPresenter.pause();
                updateBtnPlayImage();
                updatePanelPlayImage();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("" + nowTitle);
        mSensorManager.registerListener(SensorListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && nowTitle.equals("全部歌曲") && (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED )) {
            // Add your Dialogue or whatever to alert
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //在程式關閉時移除體感(Sensor)觸發
        mSensorManager.unregisterListener(SensorListener);
    }

    //    @Override
//    public void onPause() {
//        super.onPause();
//        unregisterReceiver(vcCommand);
//    }
//
//    @Override
//    public void onStop() {
//        try {
//            unregisterReceiver(vcCommand);
//        } catch (Exception e) {
//            // Receiver was probably already stopped in onPause()
//        }
//        super.onStop();
//    }
}