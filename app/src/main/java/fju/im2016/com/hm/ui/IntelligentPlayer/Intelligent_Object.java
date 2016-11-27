package fju.im2016.com.hm.ui.IntelligentPlayer;

/**
 * Created by User on 2016/11/20.
 */
public class Intelligent_Object {
    private int INTENT_NUMBER ;
    private int INTENT_COUNT ;
    private String HOUR = "HOUR";
    private String MINUTE = "MINUTE";
    private boolean SUN_B = false;
    private boolean MON_B = false;
    private boolean TUE_B = false;
    private boolean WED_B = false;
    private boolean THU_B = false;
    private boolean FRI_B = false;
    private boolean SAT_B = false;
    private boolean REPEAT_B = false;
    private int LIST_ID ;


    Intelligent_Object(){

    }

    Intelligent_Object(int INTENT_NUMBER,int INTENT_COUNT, String HOUR, String MINUTE, boolean SUN_B, boolean MON_B, boolean TUE_B, boolean WED_B, boolean THU_B, boolean FRI_B, boolean SAT_B, boolean REPEAT_B, int LIST_ID){
        this.INTENT_NUMBER = INTENT_NUMBER;//智慧播放起始number
        this.INTENT_COUNT = INTENT_COUNT;//設置intent的數量
        this.HOUR = HOUR;
        this.MINUTE = MINUTE;
        this.SUN_B = SUN_B;
        this.MON_B = MON_B;
        this.TUE_B = TUE_B;
        this.WED_B = WED_B;
        this.THU_B = THU_B;
        this.FRI_B = FRI_B;
        this.SAT_B = SAT_B;
        this.REPEAT_B = REPEAT_B;
        this.LIST_ID = LIST_ID;
    }

    public int getINTENT_NUMBER() {
        return INTENT_NUMBER;
    }

    public int getINTENT_COUNT() {
        return INTENT_COUNT;
    }

    public String getHOUR() {
        return HOUR;
    }

    public String getMINUTE() {
        return MINUTE;
    }

    public boolean isSUN_B() {
        return SUN_B;
    }

    public boolean isMON_B() {
        return MON_B;
    }

    public boolean isTUE_B() {
        return TUE_B;
    }

    public boolean isTHU_B() {
        return THU_B;
    }

    public boolean isWED_B() {
        return WED_B;
    }

    public boolean isFRI_B() {
        return FRI_B;
    }

    public boolean isSAT_B() {
        return SAT_B;
    }

    public boolean isREPEAT_B() {
        return REPEAT_B;
    }

    public int getLIST_ID() {
        return LIST_ID;
    }
}
