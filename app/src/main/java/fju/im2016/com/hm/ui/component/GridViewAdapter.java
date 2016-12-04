package fju.im2016.com.hm.ui.component;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fju.im2016.com.hm.R;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> artists;
    private boolean albumOrArtist;

    public GridViewAdapter(Context context, List<String> artists, boolean albumOrArtist) {
        this.context = context;
        this.artists = artists;
        this.albumOrArtist = albumOrArtist;
    }

    @Override
    public int getCount() {
        return this.artists.size();
    }

    @Override
    public Object getItem(int i) {
        return this.artists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_adapter_layout, null);
            holder = new ViewHolder();
            holder.gridview_adapter_artist_name = (TextView) convertView.findViewById(R.id.gridview_adapter_artist_name);
            holder.gridview_adapter_artist_img = (ImageView) convertView.findViewById(R.id.gridview_adapter_artist_img);
//            holder.playlist_adapter_btnSetting.setFocusable(false);
//            holder.playlist_adapter_btnSetting.setFocusableInTouchMode(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String artist = this.artists.get(position);
        holder.gridview_adapter_artist_name.setText(artist);
        if (this.albumOrArtist == true) {
            holder.gridview_adapter_artist_img.setImageResource(R.drawable.album);
        } else {
            holder.gridview_adapter_artist_img.setImageResource(R.drawable.singer);
        }


        return convertView;
    }

    private class ViewHolder {
        TextView gridview_adapter_artist_name;
        ImageView gridview_adapter_artist_img;
    }
}