package fju.im2016.com.hm.ui.LinkFirst;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import fju.im2016.com.hm.R;
import fju.im2016.com.hm.dbhelper.DBHelper;

public class PrivateIPSet extends AppCompatActivity {

    private ImageButton btnprivatenext;
    private TextView virtualIP_Tv;
    private SQLiteDatabase db;
    private DBHelper helper;
    String ip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_ip);



        virtualIP_Tv = (TextView)findViewById(R.id.to_private_ip);
        Intent it = this.getIntent();
        ip = it.getStringExtra("IP");
        virtualIP_Tv.setText(ip);


        btnprivatenext=(ImageButton)findViewById(R.id.nextstep);
        btnprivatenext.setOnClickListener(btprivatenext);
    }


    private View.OnClickListener btprivatenext = new View.OnClickListener(){

        @Override
        public void onClick(View v){
            Intent it = new Intent();
            it.setClass(PrivateIPSet.this, PublicIPSet.class);
            it.putExtra("IP",ip);
            startActivity(it);

        }

    };


}

