package fju.im2016.com.hm.ui.playlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.core.entity.PlayList;
import fju.im2016.com.hm.dbhelper.DBHelper;
import fju.im2016.com.hm.ui.component.PlayListAdapter;
import fju.im2016.com.hm.ui.player.PlayerFragment;

public class PlayListFragment extends Fragment implements ListView.OnItemClickListener {
    private View view;
    private SQLiteDatabase db;
    private DBHelper helper;
    private List<PlayList> playLists;
    private ListView lstPlaylist;
    private EditText editText;
    private PlayListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_song, viewGroup, false);

        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.playLists = new ArrayList<PlayList>();
        this.db = this.getActivity().openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        this.helper = new DBHelper(this.getActivity().getApplicationContext());
        Cursor clist =db.rawQuery("select * from list", null);
        clist.moveToFirst();
        getInformation(clist);
        clist.close();
        this.db.close();
        this.helper.close();

        PlayList addPlayList = new PlayList(null, "新增播放清單");
        addPlayList.setColorImg(R.drawable.ic_plus_black);
        this.playLists.add(addPlayList);

        this.initialPlayList();

    }

    private void initialPlayList() {
        this.lstPlaylist = (ListView) view.findViewById(R.id.lstMusic);

        this.adapter = new PlayListAdapter(this.playLists, this.getContext(), true);
        this.lstPlaylist.setAdapter(adapter);

        this.lstPlaylist.setOnItemClickListener(this);
    }

    private void iniAddListDialog(AlertDialog.Builder alertDialog, View view) {
        this.editText = (EditText) view.findViewById(R.id.addList_editText);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO empty and duplicated
                db = getActivity().openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
                helper = new DBHelper(getActivity().getApplicationContext());
                helper.newlist(editText.getText().toString());
                db.close();
                helper.close();
                PlayList addPlayList = new PlayList(findId(editText.getText().toString()), editText.getText().toString());
                addPlayList.setColorImg(R.drawable.list_purple);
                playLists.add(playLists.size() - 1, addPlayList);
                adapter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    private String findId(String listName) {
        this.db = this.getActivity().openOrCreateDatabase("music_database", android.content.Context.MODE_PRIVATE, null);
        this.helper = new DBHelper(this.getActivity().getApplicationContext());
        Cursor listWithFindedName = db.rawQuery("select * from list where list_name= '" + listName + "'", null);
        listWithFindedName.moveToFirst();
        String findedId = listWithFindedName.getString(listWithFindedName.getColumnIndex("_id"));
        listWithFindedName.close();
        this.db.close();
        this.helper.close();
        return findedId;
    }

    private void getInformation(Cursor clist) {
        for (int i = 0; i < clist.getCount(); i++) {
            String id = clist.getString(clist.getColumnIndex("_id"));
            String name = clist.getString(clist.getColumnIndex("list_name"));

            PlayList playList = new PlayList(id, name);
            switch (i) {
                case 0:
                    playList.setColorImg(R.drawable.list_red);
                    break;
                case 1:
                    playList.setColorImg(R.drawable.list_orange);
                    break;
                case 2:
                    playList.setColorImg(R.drawable.list_yellow);
                    break;
                case 3:
                    playList.setColorImg(R.drawable.list_green);
                    break;
                case 4:
                    playList.setColorImg(R.drawable.list_blue);
                    break;
                default:
                    playList.setColorImg(R.drawable.list_purple);
            }
            this.playLists.add(playList);

            clist.moveToNext();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        if (position != playLists.size() - 1) {
            //TODO inter list
            ListSongFragment listSongFragment = new ListSongFragment();
            Bundle bundle = new Bundle();
            bundle.putString("playListId", playLists.get(position).getId());
            bundle.putString("nowInWhichPlayListId", String.valueOf(position));
            listSongFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContent, listSongFragment);
            fragmentTransaction.commit();
        } else {
            AlertDialog.Builder addListDialog = new AlertDialog.Builder(this.getActivity());
            this.iniAddListDialog(addListDialog, LayoutInflater.from(this.getActivity()).inflate(R.layout.add_list, null));
        }
    }

}
