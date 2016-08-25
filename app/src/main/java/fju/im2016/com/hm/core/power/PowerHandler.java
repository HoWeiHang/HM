package fju.im2016.com.hm.core.power;

import android.util.Log;

import fju.im2016.com.hm.api.power.SwitchPower;
import fju.im2016.com.hm.api.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PowerHandler {
    private SwitchPower switchPower;

    public PowerHandler(){
        this.switchPower = ServiceGenerator.createService(SwitchPower.class);
    }

    public void switchPower(String power){
        Call<Object> call = this.switchPower.switchPower(power);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("onResponse", String.valueOf(response));
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("onFailure", String.valueOf(t));
            }
        });
    }
}
