package fju.im2016.com.hm.ui.component;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.Song;

public class AdapterMenuSelectedListener implements View.OnClickListener {
    private Context context;
    private Song Song;

    public AdapterMenuSelectedListener(Context context, Song song) {
        this.context = context;
        this.Song = song;
    }

    @Override
    public void onClick(View view) {
        // This is an android.support.v7.widget.PopupMenu;
        PopupMenu popupMenu = new PopupMenu(context, view) {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.adapter_menu_setRingTone:
                        return true;

                    case R.id.adapter_menu_delete:
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
}
