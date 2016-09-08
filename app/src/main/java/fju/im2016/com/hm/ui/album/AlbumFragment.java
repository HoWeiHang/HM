package fju.im2016.com.hm.ui.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.ui.component.GridViewAdapter;

public class AlbumFragment extends Fragment implements GridView.OnItemClickListener{
    private View view;
    private GridView gridView;
    private List<String> albums;
    private GridViewAdapter gridViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gridview, viewGroup, false);

        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.albums = new ArrayList<String>();

        Context ctx = getContext();
        ContentResolver resolver = ctx.getContentResolver();
        Cursor c = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"distinct album"}, null, null, null);
        c.moveToFirst();
        getInformation(c);
        c.close();

        this.initialGridView();
    }

    private void initialGridView() {
        this.gridView = (GridView) view.findViewById(R.id.gridview);

        this.gridViewAdapter = new GridViewAdapter(this.getContext(), this.albums);
        this.gridView.setAdapter(gridViewAdapter);

        this.gridView.setNumColumns(2);
        this.gridView.setOnItemClickListener(this);
    }

    private void getInformation(Cursor c) {
        for (int i = 0; i < c.getCount(); i++) {
            String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            this.albums.add(album);
            c.moveToNext();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        AlbumSongFragment albumSongFragment = new AlbumSongFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumName", this.albums.get(position));
        albumSongFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, albumSongFragment);
        fragmentTransaction.commit();
    }
}
