package fju.im2016.com.hm.core.entity;

import java.util.Stack;

public class SongIndexStack {
    private Stack songIndexStack ;

    public SongIndexStack () {
        this.songIndexStack = new Stack();
    }

    public void indexPush(int songIndex) {
        this.songIndexStack.push(new Integer(songIndex));
    }

    public int indexPop() {
        return (int)this.songIndexStack.pop();
    }

    public boolean isEmpty() {
        return  this.songIndexStack.empty();
    }

    public void clear() {
        while (!this.songIndexStack.empty())
            this.indexPop();
    }
}
