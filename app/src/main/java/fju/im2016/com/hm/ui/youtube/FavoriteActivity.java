package fju.im2016.com.hm.ui.youtube;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.dbhelper.DBHelper;

public class FavoriteActivity extends AppCompatActivity implements Serializable,YouTubePlayer.OnFullscreenListener {
    private ListView mlistView;
    private SQLiteDatabase db;
    private DBHelper helper;
    List<String> myPlaylist = new ArrayList<String>();
    ArrayList<ListObject> myFavoriteList = new ArrayList<ListObject>();
    private PageAdapter adapter;
    private ImageButton fvIndexButton;
    buttonViewHolder cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_favorite);

        db = openOrCreateDatabase("music_database", MODE_PRIVATE, null);
        helper = new DBHelper(getApplicationContext());

        fvIndexButton = (ImageButton)findViewById(R.id.fv_index);

        fvIndexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"indexButton", Toast.LENGTH_LONG).show();
            }
        });


        mlistView = (ListView) findViewById(R.id.listView);


        Cursor clist = helper.select_youtube_song();
        clist.moveToFirst();
        for(int i = 0 ; i < clist.getCount() ; i++ )
        {
            int id = clist.getInt(clist.getColumnIndex("_id"));
            String url = clist.getString(clist.getColumnIndex("youtube_id"));
            String name = clist.getString(clist.getColumnIndex("youtube_name"));
            ListObject myFavoriteObject = new ListObject(id,url,name);
            myPlaylist.add(url);
            myFavoriteList.add(myFavoriteObject);

            clist.moveToNext();
        }
        clist.close();


        adapter = new PageAdapter(this,myFavoriteList);
        mlistView.setAdapter(adapter);

        //上方使用的videoFragment
        final VideoFragment videoFragment =
                (VideoFragment) getFragmentManager().findFragmentById(R.id.fragment);
        videoFragment.setPlayVideoList(myPlaylist);
        //videoFragment.setToolbar(toolbar);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoFragment.setVideoId(position);
            }




        });
    }



    @Override
    public void onFullscreen(boolean b) {

    }

    //ListView的adapter
    private final class PageAdapter extends BaseAdapter {

        private final List<ListObject> entries;  //我的最清單
        private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
        private final LayoutInflater inflater;
        private final ThumbnailListener thumbnailListener;
        ListObject entry;

        private boolean labelsVisible;

        public PageAdapter(Context context, List<ListObject> entries) {
            this.entries = entries;

            thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
            inflater = LayoutInflater.from(context);
            thumbnailListener = new ThumbnailListener();
            labelsVisible = true;
        }

        @Override
        public int getCount() {
            return entries.size();
        }

        @Override
        public ListObject getItem(int position) {
            return entries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //移除我的最愛Item
        public void removeItem(int position,int id){
            entries.remove(position);
            helper.delete_youtube(id);
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            entry = entries.get(position);


            // There are three cases here
            if (view == null) {
                // 1) The view has not yet been created - we need to initialize the YouTubeThumbnailView.
                view = inflater.inflate(R.layout.youtube_favorite_list, parent, false);
                YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
                //取消我的最愛按鈕
                cancelButton = new buttonViewHolder();
                cancelButton.imageButton = (ImageButton) view.findViewById(R.id.imageButton);
                cancelButton.imageButton.setOnClickListener(new lvButtonListener(position,entry.getId()));
                thumbnail.setTag(entry.getUrl());
                thumbnail.initialize(Developekey.DEVELOPER_KEY,thumbnailListener);      //thumbnail初始設定
            } else {
                YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
                YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(thumbnail);
                if (loader == null) {
                    // 2) The view is already created, and is currently being initialized. We store the
                    //    current videoId in the tag.
                    thumbnail.setTag(entry.getUrl());
                } else {
                    // 3) The view is already created and already initialized. Simply set the right videoId
                    //    on the loader.
                    thumbnail.setImageResource(R.drawable.loading_thumbnail);
                    loader.setVideo(entry.getUrl());
                }
            }

            //設定影片名稱TextView
            TextView label = ((TextView) view.findViewById(R.id.videoText));
            label.setText(entry.getTitle());
            label.setVisibility(labelsVisible ? View.VISIBLE : View.GONE);
            return view;

        }

        //取消我的最愛按鈕方法
        public class lvButtonListener implements View.OnClickListener {
            private int position,id;

            lvButtonListener(int pos,int id) {
                position = pos;
                this.id = id;
            }

            @Override
            public void onClick(View v) {
                int vid=v.getId();
                if (vid == cancelButton.imageButton.getId()) {
                    new AlertDialog.Builder(FavoriteActivity.this)
                            .setTitle("確認刪除")
                            .setMessage("您真的要刪嗎?")
                            .setPositiveButton("刪吧", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeItem(position,id);
                                    Toast.makeText(getApplicationContext(), "已刪除", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNeutralButton("別刪", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "那你按屁", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            }
        }

        //thumbnail初始listener
        private final class ThumbnailListener implements
                YouTubeThumbnailView.OnInitializedListener,
                YouTubeThumbnailLoader.OnThumbnailLoadedListener {

            @Override
            public void onInitializationSuccess(
                    YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
                //loading時thumbnail的圖片
                loader.setOnThumbnailLoadedListener(this);
                thumbnailViewToLoaderMap.put(view, loader);
                view.setImageResource(R.drawable.loading_thumbnail);
                //設定thumbnail影片圖片
                String videoId = (String) view.getTag();
                loader.setVideo(videoId);
            }

            @Override
            public void onInitializationFailure(
                    YouTubeThumbnailView view, YouTubeInitializationResult loader) {
                view.setImageResource(R.drawable.no_thumbnail);
            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }


        }

    }

    private class buttonViewHolder {
        ImageButton imageButton;
    }


    public static final class VideoFragment extends YouTubePlayerFragment
            implements YouTubePlayer.OnInitializedListener {

        private YouTubePlayer player;
        private List<String> myPlaylist = new ArrayList<String>();
        private int position;
        Toolbar toolbar ;

        public static VideoFragment newInstance() {
            return new VideoFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            initialize(Developekey.DEVELOPER_KEY, this);
        }

        @Override
        public void onDestroy() {
            if (player != null) {
                player.release();
            }
            super.onDestroy();
        }

        public void setPlayVideoList(List<String> myPlaylist) {
            this.myPlaylist = myPlaylist;
        }

        public void setToolbar(Toolbar toolbar) {
            this.toolbar = toolbar;
        }

        public void setVideoId(int position) {
            this.position = position;

            if (player != null) {
                player.cueVideos(myPlaylist, position, 0);
            }

        }

        public void pause() {
            if (player != null) {
                player.pause();
            }
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer mplayer, boolean restored) {
            this.player = mplayer;
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
            player.setFullscreen(false);
            player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                @Override
                public void onFullscreen(boolean fullscreen) {
                    if (fullscreen) {
                        toolbar.setVisibility(View.GONE);
                        player.play();

                    } else {
                        toolbar.setVisibility(View.VISIBLE);
                        player.play();
                    }
                }
            });
            if (!restored) {
                player.cueVideos(myPlaylist, position, 0);
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
            this.player = null;
        }

    }

    public static final class ActionBarPaddedFrameLayout extends FrameLayout {

        private ActionBar actionBar;
        private boolean paddingEnabled;

        public ActionBarPaddedFrameLayout(Context context) {
            this(context, null);
        }

        public ActionBarPaddedFrameLayout(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public ActionBarPaddedFrameLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            paddingEnabled = true;
        }

        public void setActionBar(ActionBar actionBar) {
            this.actionBar = actionBar;
            requestLayout();
        }

        public void setEnablePadding(boolean enable) {
            paddingEnabled = enable;
            requestLayout();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int topPadding =
                    paddingEnabled && actionBar != null && actionBar.isShowing() ? actionBar.getHeight() : 0;
            setPadding(0, topPadding, 0, 0);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }


}
