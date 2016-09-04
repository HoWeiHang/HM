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
import fju.im2016.com.hm.core.entity.PlayList;

public class PlayListAdapter extends BaseAdapter {
    private List<PlayList> playLists;
    private Context context;
    private boolean overFlow;

    public PlayListAdapter(List<PlayList> playLists, Context context, boolean overFlow) {
        this.playLists = playLists;
        this.context = context;
        this.overFlow = overFlow;
    }

    @Override
    public int getCount() {
        return this.playLists.size();
    }

    @Override
    public Object getItem(int i) {
        return this.playLists.get(i);
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
            convertView = inflater.inflate(R.layout.playlist_adapter_layout, null);
            holder = new ViewHolder();
            holder.playlist_adapter_playlist_name = (TextView) convertView.findViewById(R.id.playlist_adapter_playlist_name);
            holder.playlist_adapter_color_img = (ImageView) convertView.findViewById(R.id.playlist_adapter_color_img);
            holder.playlist_adapter_btnSetting = (ImageButton) convertView.findViewById(R.id.playlist_adapter_btnSetting);
            holder.playlist_adapter_btnSetting.setFocusable(false);
            holder.playlist_adapter_btnSetting.setFocusableInTouchMode(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PlayList playList = this.playLists.get(position);
        holder.playlist_adapter_playlist_name.setText(playList.getName());
        holder.playlist_adapter_color_img.setImageResource(playList.getColorImg());
        if (this.overFlow) {
            holder.playlist_adapter_btnSetting.setImageResource(R.drawable.overflow);
            holder.playlist_adapter_btnSetting.setOnClickListener(new PlayListAdapterMenuListener(this.context, playList, new PlayListOnDeleteCallBack() {
                @Override
                public void onDelete() {
                    playLists.remove(position);
                    notifyDataSetChanged();
                }
            }));
        }
        if (position == (playLists.size() - 1)) {
            holder.playlist_adapter_btnSetting.setImageResource(R.drawable.konkon);
            holder.playlist_adapter_btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        return convertView;
    }

    private class ViewHolder {
        TextView playlist_adapter_playlist_name;
        ImageView playlist_adapter_color_img;
        ImageButton playlist_adapter_btnSetting;
    }
}