package fju.im2016.com.hm.core.entity.player;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import fju.im2016.com.hm.core.entity.Song;

public class AndroidMediaPlayer implements Player {
    private MediaPlayer mediaPlayer;
    private OnTimeTextListener onTimeTextListener;
    private OnCompletionListener onCompletionListener;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public AndroidMediaPlayer(final OnTimeTextListener onTimeTextListener) {
        this.setOnTimeTextListener(onTimeTextListener);
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(onCompletionListener!=null)
                    onCompletionListener.onComplete();
            }
        });
    }

    @Override
    public void iniMediaPlayer(Song currentSong) throws IOException {
//        if (this.mediaPlayer != null) {
//            this.mediaPlayer.stop();
//        }
        this.mediaPlayer.reset();
        this.mediaPlayer.setDataSource(currentSong.getPath());
        this.mediaPlayer.prepare();
    }

    @Override
    public void play() {
        this.mediaPlayer.start();
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    onTimeTextListener.OnTimeText(getCurrentPosition());
                } else {
                    timer.cancel();
                    timer.purge();
                }
            }
        }, 0, 1000);
    }

    @Override
    public void pause() {
        this.mediaPlayer.pause();
    }

    @Override
    public boolean isRepeat() {
        return this.mediaPlayer.isLooping();
    }

    @Override
    public void setRepeat(boolean repeat) {
        this.mediaPlayer.setLooping(repeat);
    }

    @Override
    public int getDuration() {
        return this.mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return this.mediaPlayer.getCurrentPosition();
    }

    @Override
    public void setOnTimeTextListener(OnTimeTextListener onTimeTextListener) {
        this.onTimeTextListener = onTimeTextListener;
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    @Override
    public void seekTo(int currentPosition) {
        this.mediaPlayer.seekTo(currentPosition);
        onTimeTextListener.OnTimeText(this.getCurrentPosition());
    }
}
