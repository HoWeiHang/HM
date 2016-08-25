package fju.im2016.com.hm.core.manager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class SlidingLayoutManager extends SlidingUpPanelLayout {

    private boolean mClickToCollapse = true;

    public SlidingLayoutManager(Context context) {
        super(context);
    }

    public SlidingLayoutManager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingLayoutManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setClickToCollapseEnabled(boolean enabled){
        mClickToCollapse = enabled;
    }

    @Override
    public void setDragView(View dragView) {
        super.setDragView(dragView);
        if (dragView != null) {
            dragView.setClickable(true);
            dragView.setFocusable(false);
            dragView.setFocusableInTouchMode(false);
            dragView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEnabled() || !isTouchEnabled()) return;
                    if (getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED && getPanelState() != PanelState.ANCHORED) {
                        if (getAnchorPoint() < 1.0f) {
                            setPanelState(PanelState.ANCHORED);
                        } else {
                            setPanelState(PanelState.EXPANDED);
                        }
                    } else if (mClickToCollapse){
                        setPanelState(PanelState.COLLAPSED);
                    }
                }
            });
        }
    }
}