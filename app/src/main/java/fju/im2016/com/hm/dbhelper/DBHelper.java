package fju.im2016.com.hm.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fju.im2016.com.hm.ui.youtube.ListObject;

public class DBHelper extends SQLiteOpenHelper{

    private final static String DATABASE_NAME = "music_database";
    private final static int DATABASE_VERSION = 1;

    private final static String SONG_TABLE = "song";
    private final static String SONG_ID = "_id";
    private final static String SONG_NAME = "song_name";

    private final static String LIST_TABLE = "list";
    private final static String LIST_ID = "_id";
    private final static String LIST_NAME = "list_name";

    private final static String SONG_OF_LIST_TABLE = "song_of_list";
    private final static String SONG_OF_LIST_ID = "_id";
    private final static String S_ID = "s_id";
    private final static String L_ID = "l_id";

    private final static String YOUTUBE_TABLE = "youtube";
    private final static String YOUTUBE_ID = "_id";
    private final static String VIDEO_ID = "youtube_id";
    private final static String VIDEO_NAME = "youtube_name";

    private final static String IP_TABLE = "ipset";
    private final static String IP_ID = "_id";
    private final static String IP = "IP";



    private String sqlsong =
            "CREATE TABLE IF NOT EXISTS "+SONG_TABLE+"("+
                    SONG_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    SONG_NAME+" CHAR"+
                    ")";

    private String sqllist =
            "CREATE TABLE IF NOT EXISTS "+LIST_TABLE+"("+
                    LIST_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    LIST_NAME+" CHAR"+
                    ")";

    private String sqlsong_of_list =
            "CREATE TABLE IF NOT EXISTS "+SONG_OF_LIST_TABLE+"("+
                    SONG_OF_LIST_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    S_ID+" INTEGER,"+
                    L_ID+" INTEGER ,"+
                    "FOREIGN KEY ("+S_ID+")  REFERENCES song(_id),"+
                    "FOREIGN KEY ("+L_ID+") REFERENCES list(_id)"+
                    ")";

    private String sqlyoutube =
            "CREATE TABLE IF NOT EXISTS "+YOUTUBE_TABLE+"("+
                    YOUTUBE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    VIDEO_ID+" CHAR,"+
                    VIDEO_NAME+" CHAR"+
                    ")";

    private String sqlip =
            "CREATE TABLE IF NOT EXISTS "+IP_TABLE+"("+
                    IP_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    IP+" CHAR"+
                    ")";


    private SQLiteDatabase database;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlsong);
        db.execSQL(sqllist);
        db.execSQL(sqlsong_of_list);
        db.execSQL(sqlyoutube);
        db.execSQL(sqlip);

        db.execSQL("insert into list values (1,'紅色清單')");
        db.execSQL("insert into list values (2,'橙色清單')");
        db.execSQL("insert into list values (3,'黃色清單')");
        db.execSQL("insert into list values (4,'綠色清單')");
        db.execSQL("insert into list values (5,'藍色清單')");
        db.execSQL("insert into ipset values (1,'0.0.0.0')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public Cursor selectsong(){
        Cursor cursorsong = database.query("song", null, null, null, null, null, null);
        return cursorsong;
    }




    public void newlist(String name){
        ContentValues values = new ContentValues();
        values.put("list_name", name);
        database.insert("list", null, values);

    }



    public void addsong(int song_id , int list_id){
        ContentValues values = new ContentValues();
        values.put("s_id", song_id);
        values.put("l_id", list_id);
        database.insert("song_of_list", null, values);

    }

    public void delete(int id){
        database.delete("list", "_id" + "=" + Integer.toString(id), null);
    }

    public void delete_song_of_list(int id){
        database.delete("song_of_list", "_id" + "=" + Integer.toString(id), null);
    }


    public void delete_allsong(int id){
        database.delete("song_of_list", "l_id" + "=" + Integer.toString(id), null);
    }

    public void update(int id, String itemText){
        ContentValues values = new ContentValues();
        values.put("song_name", itemText);
        database.update("song", values, "_id" + "=" + Integer.toString(id), null);
    }

    public void update_IP(String itemText){
        ContentValues values = new ContentValues();
        values.put("IP", itemText);
        database.update("ipset", values, "_id" + "=" + Integer.toString(1), null);
    }

    public Cursor select_ip(){
        Cursor cursor_ip = database.rawQuery("select * from ipset", null);
        return cursor_ip;

    }

    public Cursor select_youtube_song(){
        Cursor cursorsong = database.rawQuery("select * from youtube", null);
        return cursorsong;
    }

    public void add_youtube_song(ListObject myFavorite){
        ContentValues values = new ContentValues();
        values.put("youtube_id",myFavorite.getUrl());
        values.put("youtube_name", myFavorite.getTitle());
        database.insert("youtube", null, values);
    }

    public void delete_youtube(int id){
        database.delete("youtube", "_id" + "=" + Integer.toString(id), null);
    }


    public void close(){
        database.close();
    }
}
