package fju.im2016.com.hm.core.entity;

import java.util.List;

public class NormalPlayMode implements PlayMode {
    private Song currentSong;
    private boolean random;
    private List<Song> songs;

    public NormalPlayMode(List<Song> songs){
        this.setSongs(songs);
    }

    @Override
    public int getLastIndexValue() {
        return -1;
    }

    @Override
    public void establishSongIndexArray() {

    }

    @Override
    public void setBaseIndex(int baseIndex) {

    }

    @Override
    public void resetLastIndexValue() {

    }

    @Override
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public Song getCurrentSong() {
        return this.currentSong;
    }

    @Override
    public void setCurrentSong(Song song) {
        this.currentSong = song;
    }

    @Override
    public Song getNextSong() {
        if(this.getCurrentSongIndex() >= this.songs.size() - 1) {
            this.currentSong = this.songs.get(0);
        }
        else {
            int index = this.getCurrentSongIndex();
            this.currentSong = this.songs.get(index + 1);
        }
        this.setCurrentSong(this.currentSong);
        return this.currentSong;
    }

    @Override
    public int getCurrentSongIndex() {
        return this.songs.indexOf(this.currentSong);
    }

    @Override
    public int getSongListSize() {
        return  this.songs.size() - 1;
    }

    @Override
    public Song getLastSong() {
        if(this.getCurrentSongIndex() <= 0) {
            this.currentSong = this.songs.get(this.songs.size() - 1);
        }
        else {
            this.currentSong = this.songs.get(this.getCurrentSongIndex() - 1);
        }
        this.setCurrentSong(this.currentSong);
        return this.currentSong;
    }

    @Override
    public void setRandom(boolean random) {

    }
}
