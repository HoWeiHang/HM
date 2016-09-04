package fju.im2016.com.hm.ui.component;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.PlayList;
import fju.im2016.com.hm.core.entity.Song;
import fju.im2016.com.hm.core.entity.SongOfList;
import fju.im2016.com.hm.dbhelper.DBHelper;

public class AdapterMenuListener implements View.OnClickListener, ListView.OnItemClickListener {
    private OnDeleteCallBack onDeleteCallBack;
    private Context context;
    private Song song;
    private String newAddress, newFilename;
    private int countAddress, choseList;
    private char[] charArray;
    private File file;
    private SQLiteDatabase db;
    private DBHelper helper;
    private List<PlayList> playLists;
    private PlayListAdapter playListAdapter;
    private ListView lstPlaylist;
    private EditText editText;
    private List<SongOfList> songOfLists;
    private boolean songInList = false;

    public AdapterMenuListener(Context context, Song song, OnDeleteCallBack onDeleteCallBack) {
        this.context = context;
        this.song = song;
        this.onDeleteCallBack = onDeleteCallBack;
        this.db = this.context.openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        this.helper = new DBHelper(this.context.getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        // This is an android.support.v7.widget.PopupMenu;
        PopupMenu popupMenu = new PopupMenu(context, view) {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.adapter_menu_setRingtone:
                        newAddress = "";
                        newFilename = "";
                        charArray = String.valueOf(song.getPath()).toCharArray();
                        getCountAddress();
                        setFile(getNewAddress(), getNewFilename());
                        setRingtones();
                        return true;

                    case R.id.adapter_menu_delete:
                        context.getContentResolver().delete(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                MediaStore.Audio.Media._ID + "=\'" + song.getId().toString() + "\'",
                                null);
                        new File(song.getPath()).delete();
                        onDeleteCallBack.onDelete();
//                        MusicListAdapter.getInstance().notifyDataSetChanged();
                        return true;

                    case R.id.adapter_menu_addPalyList:
                        if (playLists != null)
                            playLists.clear();
                        playLists = new ArrayList<PlayList>();
                        AlertDialog.Builder addlistDialog = new AlertDialog.Builder(context);
                        iniChoseListDialog(addlistDialog, LayoutInflater.from(context).inflate(R.layout.add_list_dialog_layout, null));
                        showPlayList();
                        return true;

                    default:
                        return super.onMenuItemSelected(menu, item);
                }
            }
        };

        popupMenu.inflate(R.menu.menu_adapter);

        popupMenu.show();
    }

//    private void checkExist() {
//        Cursor cSongOfList =db.rawQuery("select * from song_of_list where l_id = " + this.playListId, null);
//        cSongOfList.moveToFirst();
//        getSongOfListInformation(cSongOfList);
//        cSongOfList.close();
//    }

    private void iniChoseListDialog(AlertDialog.Builder alertDialog, View view) {
        this.lstPlaylist = (ListView) view.findViewById(R.id.lstPlayList);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.addsong(Integer.parseInt(song.getId()), Integer.parseInt(playLists.get(choseList).getId()));
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    private void iniAddListDialog(AlertDialog.Builder alertDialog, View view) {
        this.editText = (EditText) view.findViewById(R.id.addList_editText);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO empty and duplicated, selected after addList
                helper.newlist(editText.getText().toString());
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
        Cursor listWithFindedName = db.rawQuery("select * from list where list_name= '" + listName + "'", null);
        listWithFindedName.moveToFirst();
        String findedId = listWithFindedName.getString(listWithFindedName.getColumnIndex("_id"));
        listWithFindedName.close();
        return findedId;
    }

    private  void showPlayList() {
        Cursor clist =db.rawQuery("select * from list", null);
        clist.moveToFirst();
        getCListInformation(clist);
        clist.close();

        PlayList addPlayList = new PlayList(null, "新增播放清單");
        addPlayList.setColorImg(R.drawable.ic_plus_black);
        this.playLists.add(addPlayList);

        this.initialPlayList();
    }

    private void initialPlayList() {
        this.playListAdapter = new PlayListAdapter(this.playLists, context, false);
        this.lstPlaylist.setAdapter(playListAdapter);
        this.lstPlaylist.setSelector(R.color.nav_item_background);
        this.lstPlaylist.setOnItemClickListener(this);
    }

    private void getSongOfListInformation(Cursor cSongOfList) {
        this.songOfLists = new ArrayList<SongOfList>();
        for (int i = 0; i < cSongOfList.getCount(); i++) {
            String id = cSongOfList.getString(cSongOfList.getColumnIndex("_id"));
            String songId = cSongOfList.getString(cSongOfList.getColumnIndex("s_id"));
            String listId = cSongOfList.getString(cSongOfList.getColumnIndex("l_id"));

            SongOfList songOfList = new SongOfList(id, songId, listId);
            this.songOfLists.add(songOfList);

            cSongOfList.moveToNext();
        }
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
        this.context.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + this.getFile().getAbsolutePath() + "\"", null);
        Uri newUri = this.context.getApplicationContext().getContentResolver().insert(uri, values);

        RingtoneManager.setActualDefaultRingtoneUri(this.context.getApplicationContext(), RingtoneManager.TYPE_RINGTONE, newUri);
    }

    private boolean checkDuplicated() {
        for (int i = 0; i < this.songOfLists.size(); i++) {
            if (this.song.getId().equals(this.songOfLists.get(i).getSongId())) {
                this.songInList = true;
            }
        }
        return this.songInList;
    }

    private void showDuplicatedDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
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
            Cursor cSongOfList =db.rawQuery("select * from song_of_list where l_id = " + this.playLists.get(position).getId(), null);
            cSongOfList.moveToFirst();
            getSongOfListInformation(cSongOfList);
            cSongOfList.close();

            if (this.checkDuplicated()) {
                this.showDuplicatedDialog();
            }
        } else {
            AlertDialog.Builder addListDialog = new AlertDialog.Builder(this.context);
            this.iniAddListDialog(addListDialog, LayoutInflater.from(this.context).inflate(R.layout.add_list, null));
        }
    }
}

interface OnDeleteCallBack {
    void onDelete();
}