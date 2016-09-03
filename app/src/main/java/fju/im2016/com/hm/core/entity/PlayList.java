package fju.im2016.com.hm.core.entity;

public class PlayList {
    private String id, name;
    private int colorImg;

    public PlayList() {

    }

    public PlayList(String id, String name) {
        this.setId(id);
        this.setName(name);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColorImg(int colorImg) {
        this.colorImg = colorImg;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getColorImg() {
        return this.colorImg;
    }
}
