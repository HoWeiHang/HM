package fju.im2016.com.hm.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.RepeatEnum;
import fju.im2016.com.hm.core.entity.Song;
import fju.im2016.com.hm.core.entity.player.Player;
import fju.im2016.com.hm.core.manager.SlidingLayoutManager;
import fju.im2016.com.hm.core.manager.SongManager;
import fju.im2016.com.hm.presenter.player.PlayerPresenter;
import fju.im2016.com.hm.presenter.player.PlayerPresenterImpl;
import fju.im2016.com.hm.ui.player.PlayerFragment;
import fju.im2016.com.hm.ui.player.PlayerView;


public class MainActivity extends AppCompatActivity implements PlayerView, PlayerFragment.OnItemClickCallBack{

    private TextView albumName, musicName, runTime, fullTime, panelAlbumName, panelSongName;
    private SeekBar seekBar;
    private ImageView albumImage;
    private ImageButton btnPlay, btnFront, btnNext, btnRe, btnRandom, panelPlay, panelFront, panelNext;
    private Boolean Random = false;
    private RepeatEnum Repeat = RepeatEnum.repeatOff;

    private PlayerPresenter playerPresenter;


    private int navItemId;
    private boolean showPanel = true;
    private boolean hidePanel = false;
    private boolean isExpanded = false;
    private boolean isCollapsed = false;
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

    @Override
    public void onDefault(SongManager songManager){
        this.iniPlayer(songManager);
        this.playerPresenter.pause();
        this.updateBtnPlayImage();
        this.updatePanelPlayImage();
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.iniButtonPlay();
        this.iniPanelPlay();
        this.iniButtonFront();
        this.iniPanelFront();
        this.iniButtonNext();
        this.iniPanelNext();
        this.iniButtonRe();
        this.iniButtonRandom();
        this.iniSeekBar();

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

        // ------------------------------------------

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("歌曲列表 - 全部歌曲");
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
                        showOption(R.id.action_settings);
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#778e8e8e")));
                    } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                    {
                        isExpanded = false;
                        isCollapsed = true;
                        panelSongName.requestFocus();
                        getSupportActionBar().setTitle("歌曲列表 - 全部歌曲");
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
                        showOption(R.id.action_search);
                        showOption(R.id.action_home);
                        hideOption(R.id.action_settings);
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


    }

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
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, new PlayerFragment());
                fragmentTransaction.commit();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_item_album:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, new PlayerFragment());
                fragmentTransaction.commit();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_item_artist:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, new PlayerFragment());
                fragmentTransaction.commit();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_item_folder:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, new PlayerFragment());
                fragmentTransaction.commit();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_item_playlist:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, new PlayerFragment());
                fragmentTransaction.commit();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            default:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, new PlayerFragment(  ) );
                fragmentTransaction.commit();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
        }


        Toast.makeText(MainActivity.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        showOption(R.id.action_search);
        showOption(R.id.action_home);
        hideOption(R.id.action_settings);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View v = findViewById(R.id.action_home);

                if (v != null) {
                    v.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return false;
                        }
                    });
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                // User chose the "Settings" item, show the app settings UI...
//                return true;

            case R.id.action_home:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

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
                    playerPresenter.last();
                    setRepeatOnce();
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
                    playerPresenter.last();
                    setRepeatOnce();
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
                    playerPresenter.next();
                    setRepeatOnce();
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
                    playerPresenter.next();
                    setRepeatOnce();
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
                if(Random) {
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
        int duration = player.getDuration();
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
}