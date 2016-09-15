package fju.im2016.com.hm.ui.player;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import java.util.List;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.Song;
import fju.im2016.com.hm.core.manager.SongManager;
import fju.im2016.com.hm.core.power.PowerHandler;
import fju.im2016.com.hm.ui.component.MusicListAdapter;
import fju.im2016.com.hm.ui.main.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements ListView.OnItemClickListener{

    private ListView lstMusic;
    List<String> songName = new ArrayList<>();
    List<String> songID = new ArrayList<>();
    List<String> songPath = new ArrayList<>();
    private int cListItem = 0;
    private int totalCount;
    private PowerHandler powerHandler;
    // protected static final int Menu_Item1=Menu.FIRST;
    private SongManager songManager;
    private View view;
    private OnItemClickCallBack onItemClickCallBack;
    private MusicListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_song, viewGroup, false);

        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.songManager = new SongManager();

        onItemClickCallBack.setToolBarTitle("全部歌曲");

        Context ctx = getContext();
        ContentResolver resolver = ctx.getContentResolver();
        Cursor c = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        c.moveToFirst();
        getInformation(c);
        c.close();


        this.initialMusicList();
        this.onItemClickCallBack.sendAdapter(this.adapter);
    }

//        this.powerHandler = new PowerHandler();
//        powerHandler.switchPower("on");


    private void initialMusicList() {
        this.lstMusic = (ListView) view.findViewById(R.id.lstMusic);
//        ArrayAdapter<String> adaSong = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songName);
//        this.lstMusic.setAdapter(adaSong);

        adapter = new MusicListAdapter(this.songManager.getSongs(), this.getContext(), true, null);
        this.lstMusic.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
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

//    private void getInformation(Cursor c, String dispStr) {
//        for (int i = 0; i < totalCount; i++) {
//            int index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
//            String src = c.getString(index);
//            songName.add(src);
////            songFile.add(src + ".mp3");
//            dispStr = dispStr + "Title:   " + src + "\n";
//            index = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
//            src = c.getString(index);
//            dispStr = dispStr + "ID:  " + src + "\n";
//            songID.add(src);
//            index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
//            src = c.getString(index);
//            dispStr = dispStr + "Artist:  " + src + "\n";
//            index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
//            src = c.getString(index);
//            dispStr = dispStr + "Album:  " + src + "\n";
//            index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA); // path
//            src = c.getString(index);
//            songPath.add(src);
//            dispStr = dispStr + "Path:  " + src + "\n";
//            c.moveToNext();
//        }
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        cListItem = position;
        Song song = (Song)adapter.getItem(position);
        for (int i = 0; i < songManager.getSongs().size(); i++) {
            if (song.getName().equals(songManager.getSongs().get(i).getName())) {
                this.songManager.setCurrentSong(i);
            }
        }
        this.onItemClickCallBack.onClick(this.songManager);
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), MainActivity.class);
//        startActivity(intent);
    }

    public interface OnItemClickCallBack {
        void onClick(SongManager songManager) ;
        void sendAdapter(MusicListAdapter musicListAdapter);
        void setToolBarTitle(String toolBarTitle) ;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onItemClickCallBack = (OnItemClickCallBack) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }
}

