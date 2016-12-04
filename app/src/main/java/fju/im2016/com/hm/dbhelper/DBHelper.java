package fju.im2016.com.hm.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fju.im2016.com.hm.ui.IntelligentPlayer.Intelligent_Object;
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

    private final static String INTELLIGENT_PLAYER_TABLE = "intelligent_player";
    private final static String ID = "_id";
    private final static String INTENT_NUMBER = "intent_number";
    private final static String INTENT_COUNT = "intent_count";
    private final static String HOUR = "hour";
    private final static String MINUTE = "minute";
    private final static String SUN_B = "sun_b";
    private final static String MON_B = "mon_b";
    private final static String TUE_B = "tue_b";
    private final static String WED_B = "wed_b";
    private final static String THU_B = "thu_b";
    private final static String FRI_B = "fri_b";
    private final static String SAT_B = "sat_b";
    private final static String REPEAT_B = "repeat_b";

    private final static String LATELY_PLAY_TABLE = "lately_play";
    private final static String LATELY_ID = "_id";

    private String sqllatelyplay =
            "CREATE TABLE IF NOT EXISTS "+LATELY_PLAY_TABLE+"("+
                    LATELY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    S_ID+" INTEGER"+
                    ")";


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

    private String sql_intelligent =
            "CREATE TABLE IF NOT EXISTS "+INTELLIGENT_PLAYER_TABLE+"("+
                    ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    INTENT_NUMBER+" INTEGER,"+
                    INTENT_COUNT+" INTEGER,"+
                    HOUR+" CHAR,"+
                    MINUTE+" CHAR,"+
                    SUN_B+" INTEGER,"+
                    MON_B+" INTEGER,"+
                    TUE_B+" INTEGER,"+
                    WED_B+" INTEGER,"+
                    THU_B+" INTEGER,"+
                    FRI_B+" INTEGER,"+
                    SAT_B+" INTEGER,"+
                    REPEAT_B+" INTEGER,"+
                    L_ID+" INTEGER"+
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
        db.execSQL(sql_intelligent);
        db.execSQL(sqllatelyplay);

        db.execSQL("insert into list values (1,'紅色清單')");
        db.execSQL("insert into list values (2,'橙色清單')");
        db.execSQL("insert into list values (3,'黃色清單')");
        db.execSQL("insert into list values (4,'綠色清單')");
        db.execSQL("insert into list values (5,'藍色清單')");
        db.execSQL("insert into ipset values (1,'0.0.0.0')");
        db.execSQL("insert into ipset values (2,'0.0.0.0')");
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

    public void addLatelyPlay(int song_id){
        ContentValues values = new ContentValues();
        values.put("s_id", song_id);
        database.insert("lately_play", null, values);
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

    public void update_IP_virtual(String itemText){
        ContentValues values = new ContentValues();
        values.put("IP", itemText);
        database.update("ipset", values, "_id" + "=" + Integer.toString(2), null);
    }

    public void updateLatelyPlay(int id,int song_id){
        ContentValues values = new ContentValues();
        values.put("s_id", song_id);
        database.update("lately_play", values, "_id" + "=" + id, null);
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

    public Cursor select_lately_play(){
        Cursor cursor_lately_play = database.rawQuery("select * from lately_play ", null);
        return cursor_lately_play;
    }

    public Cursor select_lately_play(int LATELY_ID){
        Cursor cursor_lately_play = database.rawQuery("select * from lately_play where _id=" + LATELY_ID, null);
        return cursor_lately_play;
    }

    public Cursor select_intelligent_time(String LIST_ID){
        Cursor cursor_intelligent_player = database.rawQuery("select * from intelligent_player where l_id="+LIST_ID, null);
        return cursor_intelligent_player;
    }

    public Cursor select_intelligent_time(){
        Cursor cursor_intelligent_player = database.rawQuery("select * from intelligent_player", null);
        return cursor_intelligent_player;
    }

    public void addIntelligentPlayer(Intelligent_Object intelligent_object){

        int intent_number = intelligent_object.getINTENT_NUMBER();
        int intent_count = intelligent_object.getINTENT_COUNT();
        String hour = intelligent_object.getHOUR();
        String minute = intelligent_object.getMINUTE();
        int sun = (intelligent_object.isSUN_B())?1:0;
        int mon = (intelligent_object.isMON_B())?1:0;
        int tue = (intelligent_object.isTUE_B())?1:0;
        int wed = (intelligent_object.isWED_B())?1:0;
        int thu = (intelligent_object.isTHU_B())?1:0;
        int fri = (intelligent_object.isFRI_B())?1:0;
        int sat = (intelligent_object.isSAT_B())?1:0;
        int repeat = (intelligent_object.isREPEAT_B())?1:0;
        int list_ID = intelligent_object.getLIST_ID();

        ContentValues values = new ContentValues();
        values.put("intent_number",intent_number);
        values.put("intent_count",intent_count);
        values.put("hour", hour);
        values.put("minute", minute);
        values.put("sun_b", sun);
        values.put("mon_b", mon);
        values.put("tue_b", tue);
        values.put("wed_b", wed);
        values.put("thu_b", thu);
        values.put("fri_b", fri);
        values.put("sat_b", sat);
        values.put("repeat_b", repeat);
        values.put("l_id", list_ID);

        database.insert("intelligent_player", null, values);
    }

    public void updateIntelligentPlayer(Intelligent_Object intelligent_object){

        int intent_number = intelligent_object.getINTENT_NUMBER();
        int intent_count = intelligent_object.getINTENT_COUNT();
        String hour = intelligent_object.getHOUR();
        String minute = intelligent_object.getMINUTE();
        int sun = (intelligent_object.isSUN_B())?1:0;
        int mon = (intelligent_object.isMON_B())?1:0;
        int tue = (intelligent_object.isTUE_B())?1:0;
        int wed = (intelligent_object.isWED_B())?1:0;
        int thu = (intelligent_object.isTHU_B())?1:0;
        int fri = (intelligent_object.isFRI_B())?1:0;
        int sat = (intelligent_object.isSAT_B())?1:0;
        int repeat = (intelligent_object.isREPEAT_B())?1:0;
        int list_ID = intelligent_object.getLIST_ID();

        ContentValues values = new ContentValues();
        values.put("intent_number",intent_number);
        values.put("intent_count",intent_count);
        values.put("hour", hour);
        values.put("minute", minute);
        values.put("sun_b", sun);
        values.put("mon_b", mon);
        values.put("tue_b", tue);
        values.put("wed_b", wed);
        values.put("thu_b", thu);
        values.put("fri_b", fri);
        values.put("sat_b", sat);
        values.put("repeat_b", repeat);
        values.put("l_id", list_ID);

        database.update("intelligent_player", values, "l_id" + "=" + list_ID, null);
    }

    public void delete_IntelligentPlayer(String LIST_ID){
        database.delete("intelligent_player", "l_id" + "=" + LIST_ID, null);
    }

    public void close(){
        database.close();
    }
}
