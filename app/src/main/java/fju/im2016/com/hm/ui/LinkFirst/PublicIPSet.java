package fju.im2016.com.hm.ui.LinkFirst;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.dbhelper.DBHelper;
import fju.im2016.com.hm.ui.main.IndexActivity;

public class PublicIPSet extends AppCompatActivity {

    private ImageButton btnpublicback;
    private ImageButton btnfinish;
    private EditText mEdit1,mEdit2,mEdit3,mEdit4;
    private SQLiteDatabase db;
    private DBHelper helper;
    boolean check_skip = false;
    String ip = "",public_ip2="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_ipset);

        db = openOrCreateDatabase("music_database", MODE_PRIVATE, null);
        helper = new DBHelper(getApplicationContext());

        Intent it_result = this.getIntent();
        ip = it_result.getStringExtra("IP");
        check_skip = it_result.getBooleanExtra("check_skip",false);

        btnpublicback=(ImageButton)findViewById(R.id.back);
        btnpublicback.setOnClickListener(btpublic_back);

        if(check_skip){
            btnpublicback.setVisibility(View.GONE);
        }

        btnfinish=(ImageButton)findViewById(R.id.finish);
        btnfinish.setOnClickListener(btn_finish);

        mEdit1 = (EditText) findViewById(R.id.IP1);
        mEdit2 = (EditText) findViewById(R.id.IP2);
        mEdit3 = (EditText) findViewById(R.id.IP3);
        mEdit4 = (EditText) findViewById(R.id.IP4);


        //建立文字監聽
        TextWatcher mTextWatcher = new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count)
            {
                //如果字數達到4，取消自己焦點，下一個EditText取得焦點
                if(mEdit1.getText().toString().length()==3)
                {
                    mEdit1.clearFocus();
                    mEdit2.requestFocus();
                }

                if(mEdit2.getText().toString().length()==3)
                {
                    mEdit2.clearFocus();
                    mEdit3.requestFocus();
                }

                if(mEdit3.getText().toString().length()==3)
                {
                    mEdit3.clearFocus();
                    mEdit4.requestFocus();
                }

                //如果字數達到4，取消自己焦點，隱藏虛擬鍵盤

            }
        };

        //加入文字監聽
        mEdit1.addTextChangedListener(mTextWatcher);
        mEdit2.addTextChangedListener(mTextWatcher);
        mEdit3.addTextChangedListener(mTextWatcher);
        mEdit4.addTextChangedListener(mTextWatcher);

    }

    private View.OnClickListener btpublic_back = new View.OnClickListener(){

        @Override
        public void onClick(View v){


            Intent it = new Intent();
            it.setClass(PublicIPSet.this, PrivateIPSet.class);
            it.putExtra("IP",ip);
            startActivity(it);

        }

    };
    private View.OnClickListener btn_finish = new View.OnClickListener(){

        @Override
        public void onClick(View v){

            String public_ip = mEdit1.getText().toString()+"."+
                    mEdit2.getText().toString()+"."+
                    mEdit3.getText().toString()+"."+
                    mEdit4.getText().toString();
            helper.update_IP(public_ip);

            Intent it = new Intent();
            it.setClass(PublicIPSet.this,IndexActivity.class);
            startActivity(it);
        }

    };
}

