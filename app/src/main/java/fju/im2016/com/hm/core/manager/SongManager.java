package fju.im2016.com.hm.core.manager;

import java.util.ArrayList;
import java.util.List;

import fju.im2016.com.hm.core.entity.PlayMode;
import fju.im2016.com.hm.core.entity.NormalPlayMode;
import fju.im2016.com.hm.core.entity.RandomPlayMode;
import fju.im2016.com.hm.core.entity.Song;

public class SongManager {
    private static SongManager songManager;
    private List<Song> songs;
    private PlayMode playMode, playModeBehind, playModeTemp;

    public SongManager() {
        this.songs = new ArrayList<Song>();
        this.playMode = new NormalPlayMode(this.getSongs());
        this.playModeBehind = new RandomPlayMode(this.getSongs());
    }

    public static SongManager getInstance() {
        if (songManager == null)
            songManager = new SongManager();
        return songManager;
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public List<Song> getSongs() {
        return this.songs;
    }

    public void setCurrentSong(int index) {
        this.playMode.setCurrentSong(this.songs.get(index));
        this.playMode.setSongs(this.songs);
        this.playModeBehind.setCurrentSong(this.songs.get(index));
        this.playModeBehind.setSongs(this.songs);
    }

    public Song getCurrentPlay() {
        return this.playMode.getCurrentSong();
    }

    public void next() {
        this.playMode.getNextSong();
    }

    public void last() {
        this.playMode.getLastSong();
    }

    public int getCurrentSongIndex() {
        return  this.playMode.getCurrentSongIndex();
    }

    public int getSongListSize() {
        return this.playMode.getSongListSize();
    }

    public int getLastIndexValue() {
        return this.playMode.getLastIndexValue();
    }

    public void setRandom(boolean random) {
        this.playMode.setRandom(random);
    }

    public void resetLastIndexValue() {
        this.playMode.resetLastIndexValue();
    }

    public void switchPlayMode() {
        this.playModeTemp = this.playMode;
        this.playMode = this.playModeBehind;
        this.playModeBehind = this.playModeTemp;
        this.playMode.setCurrentSong(this.playModeBehind.getCurrentSong());
        this.playMode.setBaseIndex(this.playModeBehind.getCurrentSongIndex());
        this.playMode.establishSongIndexArray();
    }
}
