package fju.im2016.com.hm.ui.latelyplay;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.Song;
import fju.im2016.com.hm.core.manager.SongManager;
import fju.im2016.com.hm.core.power.PowerHandler;
import fju.im2016.com.hm.dbhelper.DBHelper;
import fju.im2016.com.hm.ui.component.MusicListAdapter;
import fju.im2016.com.hm.ui.main.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class LatelyPlayFragment extends Fragment implements ListView.OnItemClickListener{

    private ListView lstMusic;
    private SongManager songManager;
    private View view;
    private MusicListAdapter adapter;
    private OnPageChangeCallBack onPageChangeCallBack;
    private SQLiteDatabase db;
    private DBHelper helper;
    private List<String> songIds = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_song, viewGroup, false);

        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.songManager = new SongManager();

        onPageChangeCallBack.setToolBarTitle("最近播放");


        db = getContext().openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        helper = new DBHelper(getContext().getApplicationContext());
        Cursor clist = helper.select_lately_play();
        if(clist.getCount() != 0) {
            clist.moveToFirst();
            for (int i = 0; i < clist.getCount(); i++) {
                songIds.add(clist.getString(clist.getColumnIndex("s_id")));
                clist.moveToNext();
            }
            clist.close();
            Collections.reverse(songIds);
            for (int i = 0; i < songIds.size(); i++) {
                Cursor c = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "_ID='" + songIds.get(i) + "'", null, null);
                c.moveToFirst();
                getInformation(c);
                c.close();
            }
        }
        db.close();
        helper.close();

        this.initialMusicList();
    }

//        this.powerHandler = new PowerHandler();
//        powerHandler.switchPower("on");


    private void initialMusicList() {
        this.lstMusic = (ListView) view.findViewById(R.id.lstMusic);
//        ArrayAdapter<String> adaSong = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songName);
//        this.lstMusic.setAdapter(adaSong);

        MusicListAdapter adapter = new MusicListAdapter(this.songManager.getSongs(), this.getContext(), true, null);
        this.lstMusic.setAdapter(adapter);

        lstMusic.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        this.songManager.setCurrentSong(position);
        this.onPageChangeCallBack.onClick(this.songManager);
    }

    public interface OnPageChangeCallBack {
        void onClick(SongManager songManager) ;
        void setToolBarTitle(String toolBarTitle) ;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onPageChangeCallBack = (OnPageChangeCallBack) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

}

