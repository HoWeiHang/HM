package fju.im2016.com.hm.ui.sleepclock;

public interface SleepClockView {
    void runClockAnim();
    void setCountdownTime(String string);
    String getEditText();
    void onFinish();
}
