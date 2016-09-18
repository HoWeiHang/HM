package fju.im2016.com.hm.ui.LinkFirst;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.ui.main.IndexActivity;

public class LinkFirstActivity extends AppCompatActivity {

    private EditText wifi_Name_Edt,wifi_Password_Edt;
    private ImageButton btnlink,btnskip;
    private ProgressDialog progressDialog;
    String result = "";
    boolean check_connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linkfirst);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("請先連接音響 Wi-Fi 板（ESP開頭）");
        builder.setCancelable(false);
        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Do something...
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


        wifi_Name_Edt = (EditText)findViewById(R.id.wifi_name) ;
        wifi_Password_Edt = (EditText)findViewById(R.id.wifi_pwd) ;


        btnlink=(ImageButton)findViewById(R.id.link);
        btnlink.setOnClickListener(lbtnlink);
        btnskip=(ImageButton)findViewById(R.id.skip);
        btnskip.setOnClickListener(lbtnskip);
    }


    private View.OnClickListener lbtnlink = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            final String wifi_name = processData(wifi_Name_Edt.getText().toString());
            final String wifi_pwd = processData(wifi_Password_Edt.getText().toString());

            showProgressDialog("提示", "正在嘗試連線......");

            // 執行緒, 2.3以上的版本需要執行緒輔助才可以去讀取網頁資料
            new Thread() {
                @Override
                public void run() {

                    // 網頁的 URL
                    String url = "http://192.168.4.1:81/update/?ssid=" + wifi_name + "&pwd=" + wifi_pwd;

                    // 開始進入網路的部分
                    result = getContent(url);

                    // 所有執行緒皆不能直接將資料傳送給 UI, 所以需要 Handler 幫忙轉送出去
                    if(result.indexOf("Connect is OK") != -1) {
                        handler.sendEmptyMessage(0);
                    }else if(result.indexOf("Connected") != -1){
                        handler.sendEmptyMessage(1);
                    }else{
                        handler.sendEmptyMessage(2);
                    }
                }
            }.start();
        }



        public String processData(String data) {
            /** 處理空白字元的問題 **/

            char[] temp = data.toCharArray();
            data = "";
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] == ' ')
                    data += "%20";
                else
                    data += temp[i];
            }
            return data;
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

        // 處理執行緒丟過來的資料
        Handler handler = new Handler() {

            // 當有不同類型的資料需要區分時, 可以用 Tag 去加以判斷
            public void handleMessage(Message mag) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LinkFirstActivity.this);
                AlertDialog alert;

                switch (mag.what) {
                    case 0:
                        hideProgressDialog();

                        builder.setMessage("連結成功！");
                        builder.setCancelable(false);
                        builder.setPositiveButton("確認", new DialogInterface.OnClickListener()

                                {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent it = new Intent();
                                        it.setClass(LinkFirstActivity.this, PrivateIPSet.class);
                                        result = result.substring(result.indexOf("IP is")+6,result.indexOf("</html>"));
                                        it.putExtra("IP",result);
                                        startActivity(it);
                                    }
                                }

                        );
                        alert = builder.create();
                        alert.show();
                        break;
                    case 1:
                        hideProgressDialog();

                        builder.setMessage("已經連結過囉!!!");
                        builder.setCancelable(false);
                        builder.setPositiveButton("確認", new DialogInterface.OnClickListener()

                                {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent it = new Intent();
                                        it.setClass(LinkFirstActivity.this, PublicIPSet.class);
                                        check_connected = true;
                                        it.putExtra("check_connect",check_connected);
                                        startActivity(it);
                                    }
                                }

                        );
                        alert = builder.create();
                        alert.show();
                        break;
                    case 2:
                        hideProgressDialog();
                        builder = new AlertDialog.Builder(LinkFirstActivity.this);
                        builder.setMessage("連結失敗，請再試一次");
                        builder.setCancelable(false);
                        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do something...
                            }
                        });
                        alert = builder.create();
                        alert.show();
                }

            }



        };
    };

    private View.OnClickListener lbtnskip = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent it = new Intent();
            it.setClass(LinkFirstActivity.this,IndexActivity.class);
            startActivity(it);
        }
    };

    /*
   * 提示加载
   */
    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {

            progressDialog = ProgressDialog.show(this, title,
                    message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }

        progressDialog.show();

    }

    /*
     * 隐藏提示加载
     */
    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }
}
