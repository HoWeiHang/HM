package fju.im2016.com.hm.core.entity.player;

import java.io.IOException;

import fju.im2016.com.hm.core.entity.Song;

public interface Player {
    void iniMediaPlayer(Song currentSong) throws IOException;

    void play();

    void pause();

    boolean isRepeat();

    void setRepeat(boolean repeat);

    int getDuration();

    int getCurrentPosition();

    void seekTo(int currentPosition);

    void setOnTimeTextListener(OnTimeTextListener onTimeTextListener);

    void setOnCompletionListener(OnCompletionListener onCompletionListener);
}