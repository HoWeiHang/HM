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
public class PlayerFragment extends Fragment implements ListView.OnItemClickListener, ListView.OnCreateContextMenuListener{

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

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_song, viewGroup, false);

        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.songManager = new SongManager();

        Context ctx = getContext();
        ContentResolver resolver = ctx.getContentResolver();
        Cursor c = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        String dispStr = "";
        c.moveToFirst();
        getInformation(c);
        c.close();

        this.songManager.setCurrentSong(0);
        this.onItemClickCallBack.onDefault(this.songManager);

        this.initialMusicList();
    }

//        this.powerHandler = new PowerHandler();
//        powerHandler.switchPower("on");


    private void initialMusicList() {
        this.lstMusic = (ListView) view.findViewById(R.id.lstMusic);
//        ArrayAdapter<String> adaSong = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songName);
//        this.lstMusic.setAdapter(adaSong);

        MusicListAdapter adapter = new MusicListAdapter(this.songManager.getSongs());
        this.lstMusic.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        lstMusic.setOnItemClickListener(this);
        lstMusic.setOnCreateContextMenuListener(this);
    }

    private void getInformation(Cursor c) {
        for (int i = 0; i < c.getCount(); i++) {
            String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            String id = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            String artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String albumId = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            double length = c.getDouble(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            Song song = new Song(id, name, path);
            song.setAlbum(album);
            song.setAlbumId(albumId);
            song.setArtist(artist);
            song.setLength(length);
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

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        cListItem = menuInfo.position;
        switch (item.getItemId()) {
            case 0:
                /*    Toast.makeText(MainActivity.this, String.valueOf(songName.get(cListItem)), Toast.LENGTH_LONG).show();
                    File file = new File(songPath.get(cListItem));
                    file.delete();
                    break;*/
            case 1:
                /*    Toast.makeText(MainActivity.this,String.valueOf(songPath.get(cListItem)), Toast.LENGTH_LONG).show();
                    String path = "file://" + songPath.get(cListItem);
                    Uri uri = Uri.parse(path);
                    RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this, RingtoneManager.TYPE_RINGTONE, uri);
                    break;*/
            default:
        }
        return super.onContextItemSelected(item);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        cListItem = position;
        this.songManager.setCurrentSong(position);
        this.onItemClickCallBack.onClick(this.songManager);
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), MainActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        //添加菜单项
        menu.add("刪除歌曲");
        menu.add("設定鈴聲");
    }

    public interface OnItemClickCallBack {
        void onClick(SongManager songManager) ;
        void onDefault(SongManager songManager);
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

