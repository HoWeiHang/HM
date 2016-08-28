package fju.im2016.com.hm.ui.component;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import fju.im2016.com.hm.R;

public class AdapterMenuSelectedListener implements View.OnClickListener {
    private Context context;

    public AdapterMenuSelectedListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        // This is an android.support.v7.widget.PopupMenu;
        PopupMenu popupMenu = new PopupMenu(context, view) {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    default:
                        return super.onMenuItemSelected(menu, item);
                }
            }
        };

        popupMenu.inflate(R.menu.menu_adapter);

        popupMenu.show();
    }
}
