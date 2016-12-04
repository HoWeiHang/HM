package fju.im2016.com.hm.ui.component;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
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
//            holder.gridview_adapter_artist_img.setImageResource(R.drawable.album);

            Cursor c = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "ALBUM='"+ artist +"'", null, null);
            c.moveToFirst();
            String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            c.close();

            decodeSampledBitmapFromResource(path, holder.gridview_adapter_artist_img, 100, 100);
        } else {
            holder.gridview_adapter_artist_img.setImageResource(R.drawable.singer);
        }


        return convertView;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void  decodeSampledBitmapFromResource(String songPath, ImageView albumImage, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songPath);

        byte [] data = mmr.getEmbeddedPicture();
        if(data != null) {
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        if(data != null) {
            Bitmap image =  BitmapFactory.decodeByteArray(data, 0, data.length, options);
            if (image != null) {
                albumImage.setImageBitmap(image);
            } else {
                albumImage.setImageResource(R.drawable.album);
            }
        } else {
            albumImage.setImageResource(R.drawable.album);
        }
    }

    private class ViewHolder {
        TextView gridview_adapter_artist_name;
        ImageView gridview_adapter_artist_img;
    }
}