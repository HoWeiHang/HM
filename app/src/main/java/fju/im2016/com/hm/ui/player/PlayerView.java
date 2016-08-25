package fju.im2016.com.hm.ui.player;

import fju.im2016.com.hm.core.entity.Song;
import fju.im2016.com.hm.core.entity.player.Player;

public interface PlayerView {
    void onMusicPlay();
    void onMusicPause();
    void setView(Song song, Player player);
    void setTimeTextView(int currentPosition);
    void updateBtnPlayImage();
    void updatePanelPlayImage();
}
