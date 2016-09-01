package fju.im2016.com.hm.ui.youtube;

import java.io.Serializable;

public class ListObject implements Serializable{
    private int id;
    private String url;
    private String title;



    ListObject(String url ,String title){


        this.url = url;
        this.title = title;
    }


    ListObject(int id,String url ,String title){
        this.id = id;
        this.url = url;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {

        return url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
