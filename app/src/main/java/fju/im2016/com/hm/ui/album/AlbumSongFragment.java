package fju.im2016.com.hm.ui.album;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.Song;
import fju.im2016.com.hm.core.manager.SongManager;
import fju.im2016.com.hm.ui.component.MusicListAdapter;

public class AlbumSongFragment extends Fragment implements ListView.OnItemClickListener {
    private View view;
    private ListView lstAlbumSong;
    private String albumName;
    private SongManager songManager;
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
        this.albumName = getArguments().getString("albumName");

        this.onItemClickCallBack.setToolBarTitle(this.albumName);

        Cursor c = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "ALBUM='"+ albumName +"'", null, null);
        c.moveToFirst();
        getInformation(c);
        c.close();


        this.initialMusicList();
    }

    private void initialMusicList() {
        this.lstAlbumSong = (ListView) view.findViewById(R.id.lstMusic);

        MusicListAdapter adapter = new MusicListAdapter(this.songManager.getSongs(), this.getContext(), true, null);
        this.lstAlbumSong.setAdapter(adapter);

        lstAlbumSong.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        this.songManager.setCurrentSong(position);
        this.onItemClickCallBack.onClick(this.songManager);
    }

    public interface OnItemClickCallBack {
        void onClick(SongManager songManager) ;
        void setToolBarTitle(String toolBarTitle);
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

