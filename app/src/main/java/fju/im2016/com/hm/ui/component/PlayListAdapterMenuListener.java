package fju.im2016.com.hm.ui.component;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.PlayList;
import fju.im2016.com.hm.dbhelper.DBHelper;

public class PlayListAdapterMenuListener implements View.OnClickListener {
    private PlayListOnDeleteCallBack playListOnDeleteCallBack;
    private Context context;
    private PlayList playList;
    private PopupMenu popupMenu;
    private SQLiteDatabase db;
    private DBHelper helper;

    public PlayListAdapterMenuListener(Context context, PlayList playList, PlayListOnDeleteCallBack playListOnDeleteCallBack) {
        this.context = context;
        this.playList = playList;
        this.playListOnDeleteCallBack = playListOnDeleteCallBack;
        this.db = this.context.openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        this.helper = new DBHelper(this.context.getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        // This is an android.support.v7.widget.PopupMenu;
        popupMenu = new PopupMenu(context, view) {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.playlist_adapter_menu_delete:
                        helper.delete_allsong(Integer.parseInt(playList.getId()));
                        helper.delete(Integer.parseInt(playList.getId()));
                        playListOnDeleteCallBack.onDelete();
                        return true;

                    case R.id.playlist_adapter_menu_regulartime:
                        //TODO regular time play
                        return true;

                    default:
                        return super.onMenuItemSelected(menu, item);
                }
            }
        };

        popupMenu.inflate(R.menu.menu_playlist_adapter);

        hideDelete();

        popupMenu.show();
    }

    private void hideDelete() {
        for (int i = 1; i <= 5; i++) {
            if (playList.getId() != null && playList.getId().equals(String.valueOf(i))) {
                popupMenu.getMenu().findItem(R.id.playlist_adapter_menu_delete).setVisible(false);
            }
        }
    }
}

interface PlayListOnDeleteCallBack {
    void onDelete();
}
