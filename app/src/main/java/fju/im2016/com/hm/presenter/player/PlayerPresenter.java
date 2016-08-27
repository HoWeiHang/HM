package fju.im2016.com.hm.presenter.player;

import java.io.IOException;

import fju.im2016.com.hm.core.entity.RepeatEnum;
import fju.im2016.com.hm.core.manager.SongManager;

public interface PlayerPresenter {
    void iniSongManager(SongManager songManager);

    void clickPlay();

    void next() throws IOException;

    void last() throws IOException;

    void iniMediaPlayer() throws IOException;

    void pause();

    void stop();

    void setPause(boolean isPause);

    void setRandom(boolean random);

    void setRepeat(boolean repeat);

    void setRepeatEnum(RepeatEnum repeatEnum);

    RepeatEnum getRepeatEnum();

    void establishSongIndexArray();

    void setBaseIndex();

    void resetIndexRef();

    void resetLastIndexValue();

    void clear();

    void updateView();

    void updateTimeText(int currentPosition);

    boolean isPause();

    void onProgressChanged(int currentPosition);

    void switchPlayMode();
}