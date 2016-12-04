package fju.im2016.com.hm.ui.IntelligentPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String IP_BR =
            "my_intelligent_player";

    @Override
    public void onReceive(Context context, Intent intent) {
        //....do something
        Intent intentActivity = new Intent(IP_BR);
        intentActivity.putExtra("intelligent_play", "play");
        context.sendBroadcast(intentActivity);
        Toast.makeText(context, "正在執行鬧鐘", Toast.LENGTH_LONG).show();
    }
}
