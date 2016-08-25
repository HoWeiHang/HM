package fju.im2016.com.hm.api.power;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SwitchPower {
    @FormUrlEncoded
    @POST("/api/power/switch")
    Call<Object> switchPower(@Field("power") String power);
}
