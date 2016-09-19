package fju.im2016.com.hm.ui.setting;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.drafts.Draft_75;
import org.java_websocket.drafts.Draft_76;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.ui.LinkFirst.LinkFirstActivity;
import fju.im2016.com.hm.ui.main.IndexActivity;
import fju.im2016.com.hm.ui.main.MainActivity;
import fju.im2016.com.hm.ui.youtube.YoutubeActivity;

public class SettingActivity extends AppCompatActivity {

    private WebSocketClient client;
    private DraftInfo selectDraft;
    private ToggleButton speakersButton,soundButton;
    private ImageButton sound_up,sound_down,goTo_Index,goTo_YouTube,goTo_MediaPlayer;
    private TextView textView;
    private Button settingButton;
    String result = "";
    public static final String VC_BR = "fju_im2016_com_hm_ui_setting_voice_control";
    private Intent vcBroadcast = new Intent(VC_BR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        speakersButton=(ToggleButton)findViewById(R.id.speakerspower);
        soundButton=(ToggleButton)findViewById(R.id.soundpower);
        sound_up=(ImageButton)findViewById(R.id.sound_up);
        sound_down=(ImageButton)findViewById(R.id.sound_down);
        textView=(TextView)findViewById(R.id.textView);
        settingButton = (Button)findViewById(R.id.settingConnect_btn);


        settingButton.setOnClickListener(connectSetting);

        iniBtnMediaPlayer();
        iniBtnYoutube();
        iniBtnSetting();


        DraftInfo[] draftInfos = {new DraftInfo("WebSocket協議Draft_17", new Draft_17()), new DraftInfo
                ("WebSocket協議Draft_10", new Draft_10()), new DraftInfo("WebSocket協議Draft_76", new Draft_76()), new
                DraftInfo("WebSocket協議Draft_75", new Draft_75())};// 所有連接協議
        selectDraft = draftInfos[0];// 默認選擇第一個連接協議

        WebSocketImpl.DEBUG = true;

        sound_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {

                        // 網頁的 URL
                        String url = "http://192.168.4.1:81/RLHIGH";

                        // 開始進入網路的部分
                        result = getContent(url);
                    }
                }.start();
            }
        });

        sound_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {

                        // 網頁的 URL
                        String url = "http://192.168.4.1:81/RLLOW";

                        // 開始進入網路的部分
                        result = getContent(url);
                    }
                }.start();
            }
        });

        speakersButton.setOnCheckedChangeListener( new  CompoundButton.OnCheckedChangeListener(){
            public void  onCheckedChanged(CompoundButton buttonView, boolean  isChecked) {
                speakersButton.setChecked(isChecked);
                //使用三目運算符來響應按鈕變換的事件
                if(isChecked){

                    new Thread() {
                        @Override
                        public void run() {

                            // 網頁的 URL
                            String url = "http://192.168.4.1:81/RYON";

                            // 開始進入網路的部分
                            result = getContent(url);

                        }
                    }.start();


                }else{
                    new Thread() {
                        @Override
                        public void run() {

                            // 網頁的 URL
                            String url = "http://192.168.4.1:81/RYOFF";

                            // 開始進入網路的部分
                            result = getContent(url);
                            handler.sendEmptyMessage(1);
                        }
                    }.start();

                }

            }
        });

        soundButton.setOnCheckedChangeListener(soundWebsocket);

    }
    public String getContent(String url) {


        // 設定 url 及指定傳送方法, 有 get 或 post 兩種
        HttpGet httpGet = new HttpGet(url);
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        // 客戶端處理http協定的物件, 簡單來說就是傳送資料的方法
        HttpClient client = new DefaultHttpClient(httpParameters);

        try {
            // 丟 request 過去並得到 response
            HttpResponse response = client.execute(httpGet);

            // 取得封包
            HttpEntity entity = response.getEntity();

            // 變成可以讀的輸入串流, 簡單來說就是接收資料的管道
            InputStream is = entity.getContent();

            // 緩衝區, 整理字串資料
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String s = "";
            String line = null;
            while ((line = reader.readLine()) != null) {
                s += line + "\n";
            }
            return s;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private void iniBtnMediaPlayer() {
        this.goTo_MediaPlayer = (ImageButton) findViewById(R.id.goToMediaPlayer);
        this.goTo_MediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(SettingActivity.this, MainActivity.class));
            }
        });
    }

    private void iniBtnYoutube() {
        this.goTo_YouTube = (ImageButton) findViewById(R.id.goToYoutube);
        this.goTo_YouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(SettingActivity.this, YoutubeActivity.class));
            }
        });
    }

    private void iniBtnSetting() {
        this.goTo_Index = (ImageButton) findViewById(R.id.goToIndex);
        this.goTo_Index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(SettingActivity.this, IndexActivity.class));
            }
        });
    }

    CompoundButton.OnCheckedChangeListener soundWebsocket = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            soundButton.setChecked(isChecked);
            //使用三目運算符來響應按鈕變換的事件
            if(isChecked){

                new Thread() {
                    @Override
                    public void run() {

                        try {
                            if (selectDraft == null) {
                                return;
                            }
                            String address = "ws://192.168.4.1";
                            /*if (address.contains("JSR356-WebSocket")) {
                                address += mNameEdt.getText().toString().trim();
                            }*/
                            Log.e("wlf", "连接地址：" + address);
                            client = new WebSocketClient(new URI(address), selectDraft.draft) {

                                @Override
                                public void onOpen(ServerHandshake handshakedata) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Log.e("wlf", "已經連接到服務器【" + getURI() + "】");


                                            textView.setText("Connect is Start!!!");
                                        }
                                    });
                                }

                                @Override
                                public void onMessage(final String message) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Log.e("wlf", "獲取到服務器信息【" + message + "】");
                                            if(message.equals("play")){
                                                vcBroadcast.putExtra("vcCommand", "play");
                                                sendBroadcast(vcBroadcast);
                                                textView.setText("play");
                                            }else if(message.equals("stop")){
                                                vcBroadcast.putExtra("vcCommand", "stop");
                                                sendBroadcast(vcBroadcast);
                                                textView.setText("stop");
                                            }


                                        }
                                    });


                                }



                                @Override
                                public void onClose(int code, String reason, boolean remote) {

                                    Log.e("wlf", "断开服务器连接【" + getURI() + "，状态码： " + code + "，断开原因：" + reason + "】");

                                    textView.setText("Connect Close!!!");
                                }

                                @Override
                                public void onError(Exception ex) {

                                    try {
                                        handler.sendEmptyMessage(0);
                                        Log.e("wlf", "连接发生了异常【异常原因：" + ex + "】");




                                        textView.setText("Connect Error!!!");

                                    }catch(Exception e){

                                    }

                                }
                            };
                            client.connect();
                        } catch (URISyntaxException e) {
                            if (client != null) {
                                client.close();
                            }
                            e.printStackTrace();
                        }
                    }
                }.start();


            }else{

                if (client != null) {
                    client.close();
                }



            }
        }
    };

    View.OnClickListener connectSetting = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            new android.support.v7.app.AlertDialog.Builder(SettingActivity.this)
                    .setTitle("確認")
                    .setMessage("您確定要重新設定連線嗎?")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent it = new Intent();
                            it.setClass(SettingActivity.this,LinkFirstActivity.class);
                            startActivity(it);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    };


    // 處理執行緒丟過來的資料
    Handler handler = new Handler() {

        // 當有不同類型的資料需要區分時, 可以用 Tag 去加以判斷
        public void handleMessage(Message mag) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            AlertDialog alert;

            switch (mag.what) {
                case 0:

                    new android.support.v7.app.AlertDialog.Builder(SettingActivity.this)
                            .setTitle("錯誤")
                            .setMessage("無法連線...")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();


                case 1:

                    break;

            }

        }



    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.close();
        }
    }

    private class DraftInfo {

        private final String draftName;
        private final Draft draft;

        public DraftInfo(String draftName, Draft draft) {
            this.draftName = draftName;
            this.draft = draft;
        }

        @Override
        public String toString() {
            return draftName;
        }

    }
}

