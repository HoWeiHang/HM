package fju.im2016.com.hm.ui.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.Song;

public class MusicListAdapter extends BaseAdapter implements Filterable {
    private List<Song> songs;
    private List<Song> filterSongs;
    private Context context;
    private boolean deleteFromMediaStore;
    private String nowInWhichPlayListId;
    private SongFilter songFilter;

    public MusicListAdapter(List<Song> songs, Context context, boolean deleteFromMediaStore, String nowInWhichPlayListId) {
        this.songs = songs;
        this.filterSongs = songs;
        this.context = context;
        this.deleteFromMediaStore = deleteFromMediaStore;
        this.nowInWhichPlayListId = nowInWhichPlayListId;
        this.getFilter();
    }

    @Override
    public int getCount() {
        return this.filterSongs.size();
    }

    @Override
    public Object getItem(int i) {
        return this.filterSongs.get(i);
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

        final Song song = this.filterSongs.get(position);
        holder.adapter_songName.setText(song.getName());
        holder.adapter_artist.setText(song.getArtist());
        getEmbeddedPicture(song.getPath(), holder.adapter_albumImage);
        holder.adapter_btnSetting.setImageResource(R.drawable.overflow);


        holder.adapter_btnSetting.setOnClickListener(new AdapterMenuListener(this.context, song, deleteFromMediaStore, position, nowInWhichPlayListId, new OnDeleteCallBack() {
            @Override
            public void onDelete() {
                songs.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onRingtoneSetting() {
                showToast(R.drawable.ic_ring, song.getName() + " 已被設為鈴聲");
            }
        }));


        return convertView;
    }

    private class ViewHolder {
        TextView adapter_songName, adapter_artist;
        ImageView adapter_albumImage;
        ImageButton adapter_btnSetting;
    }

    public void getEmbeddedPicture(String songPath, ImageView albumImage){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songPath);

        byte [] data = mmr.getEmbeddedPicture();
        //coverart is an Imageview object

        // convert the byte array to a bitmap
        if(data != null) {
            Bitmap image =  BitmapFactory.decodeByteArray(data, 0, data.length);
            if (image != null) {
                Bitmap songImage = Bitmap.createScaledBitmap(image, 1024, 768, true);
                albumImage.setImageBitmap(songImage);
            } else {
                albumImage.setImageResource(R.drawable.album);
            }
        } else {
            albumImage.setImageResource(R.drawable.album);
        }
    }

    private void showToast(int img, String string) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_layout, null);

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(img);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(string);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public Filter getFilter() {
        if (this.songFilter == null) {
            this.songFilter = new SongFilter();
        }

        return this.songFilter;
    }

    private class SongFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                List<Song> tempList = new ArrayList<Song>();

                // search content in friend list
                for (Song song : songs) {
                    if (song.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(song);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = songs.size();
                filterResults.values = songs;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterSongs = (List<Song>) results.values;
            notifyDataSetChanged();
        }
    }
}
