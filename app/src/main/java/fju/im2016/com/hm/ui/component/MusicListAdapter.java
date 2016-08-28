package fju.im2016.com.hm.ui.component;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.Song;

public class MusicListAdapter extends BaseAdapter {
    private List<Song> songs;
    private Context context;

    public MusicListAdapter(List<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.songs.size();
    }

    @Override
    public Object getItem(int i) {
        return this.songs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.music_list_adapter_layout, null);
            holder = new ViewHolder();
            holder.adapter_songName = (TextView) convertView.findViewById(R.id.adapter_songName);
            holder.adapter_artist = (TextView) convertView.findViewById(R.id.adapter_artist);
            holder.adapter_albumImage = (ImageView) convertView.findViewById(R.id.adapter_albumImage);
            holder.adapter_btnSetting = (ImageButton) convertView.findViewById(R.id.adapter_btnSetting);
            holder.adapter_btnSetting.setFocusable(false);
            holder.adapter_btnSetting.setFocusableInTouchMode(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Song song = this.songs.get(position);
        holder.adapter_songName.setText(song.getName());
        holder.adapter_artist.setText(song.getArtist());
        holder.adapter_albumImage.setImageResource(R.drawable.album);
        holder.adapter_btnSetting.setImageResource(R.drawable.overflow);


        holder.adapter_btnSetting.setOnClickListener(new AdapterMenuSelectedListener(this.context, song));


        return convertView;
    }

    private class ViewHolder{
        TextView adapter_songName, adapter_artist;
        ImageView adapter_albumImage;
        ImageButton adapter_btnSetting;
    }
}
