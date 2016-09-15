package fju.im2016.com.hm.core.entity;

public class Song {
    private String id, name, path, artist, album;
    private double length, size;
    private long albumId;

    public Song() {
    }

    public Song(String id, String name, String path) {
        this.setId(id);
        this.setName(name);
        this.setPath(path);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public long getAlbumId() {
        return this.albumId;
    }

    public double getLength() {
        return this.length;
    }

    public double getSize() {
        return this.size;
    }

    public String toString() {
        return String.format("{id:%s,name:%s,path:%s,artist:%s,album:%s,albumId:%s,length:%s, size:%s}", this.id, this.name, this.path, this.artist, this.album, this.albumId, this.length, this.size);
    }
}
