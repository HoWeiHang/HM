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
import fju.im2016.com.hm.core.entity.PlayList;

public class PlayListAdapterMenuListener implements View.OnClickListener {
    private Context context;
    private PlayList playList;
    private PopupMenu popupMenu;

    public PlayListAdapterMenuListener(Context context, PlayList playList) {
        this.context = context;
        this.playList = playList;
    }

    @Override
    public void onClick(View view) {
        // This is an android.support.v7.widget.PopupMenu;
        popupMenu = new PopupMenu(context, view) {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.playlist_adapter_menu_delete:

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
