package fju.im2016.com.hm.ui.component;

import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.Song;

public class AdapterMenuSelectedListener implements View.OnClickListener {
    private Context context;
    private Song song;
    private String newAddress, newFilename;
    private int countAddress;
    private char[] charArray;
    private File file;

    public AdapterMenuSelectedListener(Context context, Song song) {
        this.context = context;
        this.song = song;
    }

    @Override
    public void onClick(View view) {
        newAddress = "";
        newFilename = "";
        charArray = String.valueOf(song.getPath()).toCharArray();
        getCountAddress();
        setFile(getNewAddress(), getNewFilename());
        // This is an android.support.v7.widget.PopupMenu;
        PopupMenu popupMenu = new PopupMenu(context, view) {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.adapter_menu_setRingTone:
                        setRingtones();
                        return true;

                    case R.id.adapter_menu_delete:
//                        Uri uri = MediaStore.Audio.Media.getContentUriForPath(getFile().getAbsolutePath());
//                        context.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + getFile().getAbsolutePath() + "\"", null);
//                        context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media._ID + "=\'" +
//                                song.getId().toString() + "\'", null);
//                        File file = new File(song.getPath());
//                        file.delete();
                        return true;

                    case R.id.adapter_menu_addPalyList:
                        return true;

                    default:
                        return super.onMenuItemSelected(menu, item);
                }
            }
        };

        popupMenu.inflate(R.menu.menu_adapter);

        popupMenu.show();
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
        for (int i = this.countAddress + 1; i < this.charArray.length; i ++){
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
}
