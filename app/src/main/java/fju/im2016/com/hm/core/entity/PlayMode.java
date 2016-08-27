package fju.im2016.com.hm.core.entity;

import java.util.List;

public interface PlayMode {
    void setSongs(List<Song> songs);
    Song getCurrentSong();
    void setCurrentSong(Song song);
    Song getNextSong();
    Song getLastSong();
    int getCurrentSongIndex();
    int getSongListSize();
    void setBaseIndex(int baseIndex);
    void establishSongIndexArray();
    int getLastIndexValue();
    void clear();
    void resetIndexRef();
    void resetLastIndexValue();
    void setRandom(boolean random);
}
