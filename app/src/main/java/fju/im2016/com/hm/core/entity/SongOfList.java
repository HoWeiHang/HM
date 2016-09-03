package fju.im2016.com.hm.core.entity;

public class SongOfList {
    private String id, songId, listId;

    public SongOfList() {

    }

    public SongOfList(String id, String songId, String listId) {
        this.setId(id);
        this.setSongId(songId);
        this.setListId(listId);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getId() {
        return this.id;
    }

    public String getSongId() {
        return this.songId;
    }

    public String getListId() {
        return this.listId;
    }
}