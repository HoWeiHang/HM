package fju.im2016.com.hm.ui.playlist;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.Song;
import fju.im2016.com.hm.core.entity.SongOfList;
import fju.im2016.com.hm.core.manager.SongManager;
import fju.im2016.com.hm.dbhelper.DBHelper;
import fju.im2016.com.hm.ui.component.MusicListAdapter;

public class ListSongFragment extends Fragment implements ListView.OnItemClickListener {
    private View view;
    private SQLiteDatabase db;
    private DBHelper helper;
    private ListView lstListSong;
    private String playListId, nowInWhichPlayListId;
    private List<SongOfList> songOfLists;
    private SongManager songManager;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_song, viewGroup, false);

        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.songManager = new SongManager();
        this.songOfLists = new ArrayList<SongOfList>();
        this.playListId = getArguments().getString("playListId");
        this.nowInWhichPlayListId = getArguments().getString("nowInWhichPlayListId");
        this.db = this.getActivity().openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        this.helper = new DBHelper(this.getActivity().getApplicationContext());

        Cursor cSongOfList =db.rawQuery("select * from song_of_list where l_id = " + this.playListId, null);
        cSongOfList.moveToFirst();
        getSongOfListInformation(cSongOfList);
        cSongOfList.close();

        Cursor c = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        c.moveToFirst();
        getInformation(c);
        c.close();

        this.initialMusicList();
    }

    private void initialMusicList() {
        this.lstListSong = (ListView) view.findViewById(R.id.lstMusic);

        MusicListAdapter adapter = new MusicListAdapter(this.songManager.getSongs(), this.getContext(), false, nowInWhichPlayListId);
        this.lstListSong.setAdapter(adapter);

        lstListSong.setOnItemClickListener(this);
    }

    private void getSongOfListInformation(Cursor cSongOfList) {
        for (int i = 0; i < cSongOfList.getCount(); i++) {
            String id = cSongOfList.getString(cSongOfList.getColumnIndex("_id"));
            String songId = cSongOfList.getString(cSongOfList.getColumnIndex("s_id"));

            SongOfList songOfList = new SongOfList(id, songId, this.playListId);
            this.songOfLists.add(songOfList);

            cSongOfList.moveToNext();
        }
    }

    private void getInformation(Cursor c) {
        for (int j = 0; j < this.songOfLists.size(); j++) {
            for (int i = 0; i < c.getCount(); i++) {
                if (this.songOfLists.get(j).getSongId().equals(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)))) {
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
                }
                c.moveToNext();
            }
            c.moveToFirst();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

    }
}
