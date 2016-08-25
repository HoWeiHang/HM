package fju.im2016.com.hm.core.entity;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Random;

public class RandomPlayMode implements PlayMode{
    private Song currentSong;
    private boolean random;
    private List<Song> songs;
    private SongIndexStack songIndexStack;
    private int[] songIndexArray = null;
    private int baseIndex, ignoreCount;
    private int indexRef = 0;
    private int lastIndexValue = -1;

    public RandomPlayMode(List<Song> songs){
        this.setSongs(songs);
        this.songIndexStack = new SongIndexStack();
    }

    private void iniSongIndexArray() {
        this.songIndexArray = new int[(this.getSongListSize() + 1)];
        for (int i = 0; i <= this.getSongListSize(); i++) {
            this.songIndexArray[i] = i;
        }
    }

    private static void shuffle(int[] array) {
        Random r = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = r.nextInt(i);
            //swap
            int tmp = array[index];
            array[index] = array[i];
            array[i] = tmp;
        }
    }

    private void indexPush(int songIndex) {
        this.songIndexStack.indexPush(songIndex);
    }

    private int indexPop() {
        return this.songIndexStack.indexPop();
    }

    private boolean isEmpty() {
        return this.songIndexStack.isEmpty();
    }

    private void clear() {
        this.songIndexStack.clear();
    }

    private void loopForLastIndex() {
        for(int i = 0; i < this.songIndexArray.length; i++) {
            this.lastIndexValue = songIndexArray[i];
        }
    }

    private void loopForLastSecondIndex() {
        for(int i = 0; i < this.songIndexArray.length; i++) {
            int a = songIndexArray.length - 2;
            if (i == a) {
                this.lastIndexValue = songIndexArray[i];
            }
        }
    }

    private void resetIndexRef() {
        this.indexRef = 0;
    }

    @Override
    public void resetLastIndexValue() {
        this.lastIndexValue = -1;
    }

    @Override
    public int getLastIndexValue() {
        return this.lastIndexValue;
    }

    @Override
    public void establishSongIndexArray() {
        this.iniSongIndexArray();
        this.shuffle(this.songIndexArray);
    }

    @Override
    public void setBaseIndex(int baseIndex) {
        this.baseIndex = baseIndex;
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
        this.indexPush(this.getCurrentSongIndex());
//            int a = this.lastIndexValue;
        for (int i = 0; i < this.songIndexArray.length; i++) {
            if(this.songIndexArray[i] == this.baseIndex) {
                this.ignoreCount = i;
            }
        }
        if (this.ignoreCount == (this.songIndexArray.length - 1)) {
            this.loopForLastSecondIndex();
        } else {
            this.loopForLastIndex();
        }
        if (this.random) {
            if (this.indexRef == this.ignoreCount && this.indexRef != (this.songIndexArray.length - 1)) {
                this.indexRef++;
                this.random = false;
            } else if (this.ignoreCount == (this.songIndexArray.length - 1) && this.indexRef == (this.songIndexArray.length - 2)) {
                this.indexRef = 0;
                this.establishSongIndexArray();
                this.random = false;
            }
        }
        if (this.indexRef == (this.songIndexArray.length - 1)) {
            this.currentSong = this.songs.get(this.songIndexArray[this.indexRef]);
            this.indexRef = -1;
            this.establishSongIndexArray();
        } else {
            this.currentSong = this.songs.get(this.songIndexArray[this.indexRef]);
        }
        this.indexRef ++;
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
        if (this.isEmpty()){
            return null;
//            throw new EmptyStackException();
        } else {
            this.currentSong = this.songs.get(this.indexPop());
            this.setCurrentSong(this.currentSong);
            return this.currentSong;
        }
    }

    @Override
    public void setRandom(boolean random) {
        this.random = random;
        this.resetIndexRef();
        this.resetLastIndexValue();
        this.clear();
    }
}

