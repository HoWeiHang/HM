package fju.im2016.com.hm.presenter.player;

import java.io.IOException;

import fju.im2016.com.hm.core.entity.RepeatEnum;
import fju.im2016.com.hm.core.entity.RepeatMode;
import fju.im2016.com.hm.core.entity.Song;
import fju.im2016.com.hm.core.entity.player.AndroidMediaPlayer;
import fju.im2016.com.hm.core.entity.player.OnCompletionListener;
import fju.im2016.com.hm.core.entity.player.OnTimeTextListener;
import fju.im2016.com.hm.core.entity.player.Player;
import fju.im2016.com.hm.core.manager.SongManager;
import fju.im2016.com.hm.ui.player.PlayerView;

public class PlayerPresenterImpl implements PlayerPresenter {
    private PlayerView playerView;
    private Player player;
    private SongManager songManager;
    private RepeatMode repeaOn, repeatOff;
    private boolean isPause, random;
    private RepeatEnum repeatEnum;

    public PlayerPresenterImpl(final PlayerView playerView) throws IOException {
        this.repeaOn = new RepeatMode() {
            @Override
            public void onRrepeat() {
                try {
                    next();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        this.repeatOff = new RepeatMode() {
            @Override
            public void onRrepeat() {
//                int a = songManager.getLastIndexValue();
//                int b = songManager.getCurrentSongIndex();
                if (random ==false && songManager.getCurrentSongIndex() == songManager.getSongListSize()) {
                    pause();
                    isPause = true;
                    playerView.updateBtnPlayImage();
                    playerView.updatePanelPlayImage();
                } else if (random == true && songManager.getCurrentSongIndex() == songManager.getLastIndexValue()) {
                    pause();
                    isPause = true;
                    playerView.updateBtnPlayImage();
                    playerView.updatePanelPlayImage();
                    songManager.resetLastIndexValue();
                } else {
                    try {
                        next();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        };
//        this.songManager = songManager;
        this.playerView = playerView;
        this.iniPlayer();
//        this.updateView();
    }

    @Override
    public void iniSongManager(SongManager songManager) {
//        this.songManager = SongManager.getInstance();
        this.songManager = songManager;

    }

    private void iniPlayer() throws IOException {
        this.player = new AndroidMediaPlayer(new OnTimeTextListener() {
            @Override
            public void OnTimeText(int currentPosition) {
                updateTimeText(currentPosition);
            }
        });
        this.player.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onComplete() {
//                RepeatEnum e = repeatEnum;
//                boolean a = player.isRepeat();
                if (repeatEnum == RepeatEnum.repeatOn) {
                    repeaOn.onRrepeat();
                } else if (repeatEnum == RepeatEnum.repeatOff) {
                    repeatOff.onRrepeat();
                }
            }
        });
        this.random = false;
        this.isPause = false;
//        this.iniMediaPlayer();
    }

    @Override
    public void clickPlay() {
        this.isPause = !this.isPause;
        if(this.isPause())
            this.player.pause();
        else
            this.player.play();
        this.playerView.onMusicPlay();
    }

    @Override
    public void next() throws IOException {
        this.songManager.next();
        this.iniMediaPlayer();
        this.updateView();
    }

    @Override
    public void last() throws IOException {
        this.songManager.last();
        this.iniMediaPlayer();
        this.updateView();
    }

    @Override
    public void iniMediaPlayer() throws IOException {
        this.player.iniMediaPlayer(this.songManager.getCurrentPlay());
        if(!this.isPause())
            this.player.play();
    }

    @Override
    public void pause() {
        this.isPause = true;
        this.player.pause();
        this.playerView.onMusicPause();
    }

    @Override
    public void stop() {

    }

    @Override
    public void setPause(boolean isPause) {
        this.isPause = isPause;
    }

    @Override
    public void setRandom(boolean random) {
        this.random = random;
        this.songManager.setRandom(random);
    }

    @Override
    public void setRepeat(boolean repeat) {
        this.player.setRepeat(repeat);
    }

    @Override
    public void setRepeatEnum(RepeatEnum repeatEnum) {
        this.repeatEnum = repeatEnum;
    }

    @Override
    public RepeatEnum getRepeatEnum() {
        return  this.repeatEnum;
    }

    @Override
    public Song getCurrentSong() {
        return  this.songManager.getCurrentPlay();
    }

    @Override
    public void establishSongIndexArray() {
        this.songManager.establishSongIndexArray();
    }

    @Override
    public void setBaseIndex() {
        this.songManager.setBaseIndex();
    }

    @Override
    public void resetIndexRef() {
        this.songManager.resetIndexRef();
    }

    @Override
    public void resetLastIndexValue() {
        this.songManager.resetLastIndexValue();
    }

    @Override
    public void clear() {
        this.songManager.clear();
    }

    @Override
    public void updateView() {
        this.playerView.setView(songManager.getCurrentPlay(), player);
    }

    @Override
    public void updateTimeText(int currentPosition) {
        this.playerView.setTimeTextView(currentPosition);
    }

    @Override
    public boolean isPause() {
        return this.isPause;
    }

    @Override
    public void onProgressChanged(int currentPosition) {
        this.player.seekTo(currentPosition);
    }

    @Override
    public void switchPlayMode() {
        this.songManager.switchPlayMode();
    }
}
