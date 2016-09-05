package fju.im2016.com.hm.core.entity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import fju.im2016.com.hm.core.entity.Song;
import fju.im2016.com.hm.core.entity.SongOfList;
import fju.im2016.com.hm.dbhelper.DBHelper;

public class CheckColorList {
    private SQLiteDatabase db;
    private DBHelper helper;
    private String listId;
    private Song song;
    private List<SongOfList> songOfLists;
    private Context context;

    public CheckColorList(Context context) {
        this.context = context;
        this.songOfLists = new ArrayList<SongOfList>();
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public boolean findList(String listId) {
        this.db = context.openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        this.helper = new DBHelper(context.getApplicationContext());
        Cursor cSongOfList = db.rawQuery("select * from song_of_list where l_id = " + listId, null);
        cSongOfList.moveToFirst();
        getSongOfListInformation(cSongOfList);
        cSongOfList.close();
        this.db.close();
        this.helper.close();

        return this.checkInList();
    }

    private void getSongOfListInformation(Cursor cSongOfList) {
        if (this.songOfLists != null)
            this.songOfLists.clear();
        for (int i = 0; i < cSongOfList.getCount(); i++) {
            String id = cSongOfList.getString(cSongOfList.getColumnIndex("_id"));
            String songId = cSongOfList.getString(cSongOfList.getColumnIndex("s_id"));

            SongOfList songOfList = new SongOfList(id, songId, this.listId);
            this.songOfLists.add(songOfList);

            cSongOfList.moveToNext();
        }
    }

    private boolean checkInList() {
        for (int i = 0; i < this.songOfLists.size(); i++) {
            if (this.songOfLists.get(i).getSongId().equals(this.song.getId())) {
                return true;
            }
        }
        return false;
    }
}
