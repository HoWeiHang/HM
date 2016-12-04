package fju.im2016.com.hm.ui.IntelligentPlayer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.dbhelper.DBHelper;
import fju.im2016.com.hm.ui.main.MainActivity;

/**
 * Created by User on 2016/11/20.
 */
public class Intelligent_Player extends Activity {
    private TimePicker timePicker;
    private TextView textTime2;
    private Button confirm,cancelButton;
    private ToggleButton monToggleButton,tuesToggleButton,wedToggleButton,thurToggleButton,
            friToggleButton,satToggleButton,sunToggleButton;
    private Boolean sun_b = false,mon_b = false,tue_b = false,wed_b = false
            ,thu_b = false,fri_b = false,sat_b = false,ischecked_b = false;
    private CheckBox checkBox;
    private String H,M,list_id;
    private static int hour,minute,intent_number = 0,intent_count = 0;
    private Calendar calendar;
    private SQLiteDatabase db;
    private DBHelper helper;

    public static final String VC_BR = "fju_im2016_com_hm_ui_intelligent_play";
    private Intent vcBroadcast = new Intent(VC_BR);


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
         /*載入main.xml Layout */
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.intelligent_player_dialog);

        Intent intent = this.getIntent();
        list_id = intent.getStringExtra("list_id");

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        H = String.valueOf(hour);
        M = String.valueOf(minute);

        textTime2  =  (TextView)findViewById(R.id.textTime2);        //透過按鈕取得時間
        confirm  =  (Button)findViewById(R.id.confirm);
        cancelButton = (Button)findViewById(R.id.cancel);
        sunToggleButton = (ToggleButton)findViewById(R.id.t_sun);
        monToggleButton = (ToggleButton)findViewById(R.id.t_mon);
        tuesToggleButton = (ToggleButton)findViewById(R.id.t_tue);
        wedToggleButton = (ToggleButton)findViewById(R.id.t_wed);
        thurToggleButton = (ToggleButton)findViewById(R.id.t_thu);
        friToggleButton = (ToggleButton)findViewById(R.id.t_fri);
        satToggleButton = (ToggleButton)findViewById(R.id.t_sat);
        checkBox = (CheckBox) findViewById(R.id.ch_every_week);

        confirm.setOnClickListener(GTimeListener);
        cancelButton.setOnClickListener(cancelListener);

        sunToggleButton.setOnCheckedChangeListener(toggleButtonListener);
        monToggleButton.setOnCheckedChangeListener(toggleButtonListener);
        tuesToggleButton.setOnCheckedChangeListener(toggleButtonListener);
        wedToggleButton.setOnCheckedChangeListener(toggleButtonListener);
        thurToggleButton.setOnCheckedChangeListener(toggleButtonListener);
        friToggleButton.setOnCheckedChangeListener(toggleButtonListener);
        satToggleButton.setOnCheckedChangeListener(toggleButtonListener);
        checkBox.setOnCheckedChangeListener(checkedChangeListener);

        timePicker  =  (TimePicker)findViewById(R.id.timePicker);
        //timepicker時間更改時，執行下面的動作。
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minuteOfHour) {
                // TODO Auto-generated method stub
                //取得 hour的值，透過TimeFix方法。轉換成String.並初始H。
                H = TimeFix(hourOfDay);
                hour = hourOfDay;
                //取得 minute的值，透過TimeFix方法。轉換成String.並初始M。
                M = TimeFix(minuteOfHour);
                minute = minuteOfHour;
                /*//將取得的資料設定到 textTime1
                textTime1.setText(H + ":" +M);*/
            }
        });

        //初始設定，查看是否之前有設置過
        startSetting();

    }

    private Button.OnClickListener GTimeListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    //設置智慧播放
                    SetIntelligentPlayer();

                    db = openOrCreateDatabase("music_database", MODE_PRIVATE, null);
                    helper = new DBHelper(getApplicationContext());

                    //將設置好的智慧播放時間判斷線放置Intelligent_Object中
                    int list_ID = Integer.parseInt(list_id);
                    Intelligent_Object intelligent_object = new Intelligent_Object(intent_number,intent_count,H,M,sun_b,mon_b,tue_b,wed_b,thu_b,fri_b,sat_b,ischecked_b,list_ID);
                    intent_count = 0;
                    Cursor clist = helper.select_intelligent_time(list_id);

                    //新增資料或更新資料
                    if(clist.getCount() == 0) {
                        helper.addIntelligentPlayer(intelligent_object);
                    }else{
                        helper.updateIntelligentPlayer(intelligent_object);
                    }

                    db.close();
                    helper.close();
                    finish();
                }
            };

    //Timepicker取得的資料為int，但時間數字小於10，是不會有像06這樣的情況。
    //所以透過TimeFix這個方法將送過來的數字轉換成String，在判斷是否需要加0。
    private static String TimeFix(int c){
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    //判斷toggleButton是否按下
    CompoundButton.OnCheckedChangeListener toggleButtonListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (compoundButton.getId()) {
                case R.id.t_sun:
                    if (b)
                        sun_b = true;
                    else
                        sun_b = false;
                    break;
                case R.id.t_mon:
                    if(b)
                        mon_b = true;
                    else
                        mon_b = false;
                    break;
                case R.id.t_tue:
                    if(b)
                        tue_b = true;
                    else
                        tue_b = false;
                    break;
                case R.id.t_wed:
                    if(b)
                        wed_b = true;
                    else
                        wed_b = false;
                    break;
                case R.id.t_thu:
                    if(b)
                        thu_b = true;
                    else
                        thu_b = false;
                    break;
                case R.id.t_fri:
                    if(b)
                        fri_b = true;
                    else
                        fri_b = false;
                    break;
                case R.id.t_sat:
                    if(b)
                        sat_b = true;
                    else
                        sat_b = false;
                    break;
            }
        }
    };

    //判斷checkbox是否按下
    CheckBox.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
            if(ischecked)
                ischecked_b = true;
            else
                ischecked_b = false;
        }
    };

    //取消設定
    public View.OnClickListener cancelListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            db = openOrCreateDatabase("music_database", MODE_PRIVATE, null);
            helper = new DBHelper(getApplicationContext());
            Cursor clist = helper.select_intelligent_time(list_id);

            //取出intent_number及intent_count
            if(clist.getCount() != 0) {
                clist.moveToFirst();
                intent_number = clist.getInt(clist.getColumnIndex("intent_number"));
                intent_count = clist.getInt(clist.getColumnIndex("intent_count"));

                clist.close();

                helper.delete_IntelligentPlayer(list_id);


                db.close();
                helper.close();
                Intent intent = new Intent();
                //AlarmReceiver.class 則是負責接收的 BroadcastReceiver
                intent.setClass(Intelligent_Player.this, AlarmReceiver.class);
                //利用intent_number及intent_count將設置的智慧播放取消
                for (int i = 0; i < intent_count; i++) {
                    //建立待處理意圖
                    PendingIntent pending = PendingIntent.getBroadcast(Intelligent_Player.this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    //取得AlarmManager
                    AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    //設定一個警報
                    if (alarm != null) {
                        alarm.cancel(pending);
                    }
                    alarm = null;
                    pending = null;
                }
                intent_number = 0;
                intent_count = 0;
            }
            finish();
        }
    };


    //設置智慧播放
    public void SetIntelligentPlayer(){
        //使用Calendar指定時間
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND, 0);
        db = openOrCreateDatabase("music_database", MODE_PRIVATE, null);
        helper = new DBHelper(getApplicationContext());
        Cursor clist = helper.select_intelligent_time();
        if(clist.getCount() != 0) {
            clist.moveToFirst();
            for (int i = 0; i < clist.getCount(); i++) {
                if (clist.getInt(clist.getColumnIndex("intent_number")) > intent_number) {
                    intent_number = clist.getInt(clist.getColumnIndex("intent_number"));
                }
                clist.moveToNext();
            }
            clist.close();

            db.close();
            helper.close();

            intent_number++;

        }
        Intent intent = new Intent(VC_BR);
        //這裡的 this 是指當前的 Activity
        //AlarmReceiver.class 則是負責接收的 BroadcastReceiver
        intent.setClass(this, AlarmReceiver.class);

        intent.putExtra("msg","play");
        PendingIntent pending;
        AlarmManager alarm;

        //判斷每一天是否設置，並新增各broadcast
        if(sun_b){
            pending = PendingIntent.getBroadcast(this, intent_number, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            calendar.set(Calendar.DAY_OF_WEEK,1);
            intent_number++;
            intent_count++;
            if(ischecked_b)
                //設定一個警報
                //參數1,我們選擇一個會在指定時間喚醒裝置的警報類型
                //參數2,將指定的時間以millisecond傳入
                //參數3,傳入待處理意圖
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pending);
            else
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
        }
        if(mon_b){
            pending = PendingIntent.getBroadcast(this, intent_number, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            calendar.set(Calendar.DAY_OF_WEEK,2);
            intent_number++;
            intent_count++;
            if(ischecked_b)
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pending);
            else
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
        }
        if(tue_b){
            pending = PendingIntent.getBroadcast(this, intent_number, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            calendar.set(Calendar.DAY_OF_WEEK,3);
            intent_number++;
            intent_count++;
            if(ischecked_b)
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pending);
            else
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
        }
        if(wed_b){
            pending = PendingIntent.getBroadcast(this, intent_number, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            calendar.set(Calendar.DAY_OF_WEEK,4);
            intent_number++;
            intent_count++;
            if(ischecked_b)
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pending);
            else
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
        }
        if(thu_b){
            pending = PendingIntent.getBroadcast(this, intent_number, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            calendar.set(Calendar.DAY_OF_WEEK,5);
            intent_number++;
            intent_count++;
            if(ischecked_b)
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pending);
            else
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
        }
        if(fri_b){
            pending = PendingIntent.getBroadcast(this, intent_number, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            calendar.set(Calendar.DAY_OF_WEEK,6);
            intent_number++;
            intent_count++;
            if(ischecked_b)
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pending);
            else
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
        }
        if(sat_b){
            pending = PendingIntent.getBroadcast(this, intent_number, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            calendar.set(Calendar.DAY_OF_WEEK,7);
            intent_number++;
            intent_count++;
            if(ischecked_b)
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pending);
            else
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
        }
        Toast.makeText(this, "設定智慧播放", Toast.LENGTH_SHORT).show();
        intent_number = 0;
    }

    //初始設定
    public void startSetting(){
        db = openOrCreateDatabase("music_database", MODE_PRIVATE, null);
        helper = new DBHelper(getApplicationContext());

        Cursor clist = helper.select_intelligent_time(list_id);

        if(clist.getCount() != 0) {

            clist.moveToFirst();

            hour = Integer.parseInt(clist.getString(clist.getColumnIndex("hour")));
            minute = Integer.parseInt(clist.getString(clist.getColumnIndex("minute")));
            sun_b = (clist.getInt(clist.getColumnIndex("sun_b")) != 0);
            mon_b = (clist.getInt(clist.getColumnIndex("mon_b")) != 0);
            tue_b = (clist.getInt(clist.getColumnIndex("tue_b")) != 0);
            wed_b = (clist.getInt(clist.getColumnIndex("wed_b")) != 0);
            thu_b = (clist.getInt(clist.getColumnIndex("thu_b")) != 0);
            fri_b = (clist.getInt(clist.getColumnIndex("fri_b")) != 0);
            sat_b = (clist.getInt(clist.getColumnIndex("sat_b")) != 0);
            ischecked_b = (clist.getInt(clist.getColumnIndex("repeat_b")) != 0);

            if (sun_b) sunToggleButton.setChecked(true);
            if (mon_b) monToggleButton.setChecked(true);
            if (tue_b) tuesToggleButton.setChecked(true);
            if (wed_b) wedToggleButton.setChecked(true);
            if (thu_b) thurToggleButton.setChecked(true);
            if (fri_b) friToggleButton.setChecked(true);
            if (sat_b) satToggleButton.setChecked(true);
            if (ischecked_b) checkBox.setChecked(true);

            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }

        clist.close();
    }
}
