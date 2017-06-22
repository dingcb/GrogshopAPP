package com.ding.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;
import android.widget.TableLayout;

import com.ding.listener.OnStateChangeListener;
import com.ding.model.ScrollState;


public class BodyTableLayout extends TableLayout {
    private Scroller mScroller;

    private static final int NORMAL_TIME = 600;

    private int mMaxOffset;
    private float mLastY;
    private int mMoveY;

    private ScrollState state = ScrollState.SHOW;

    private OnStateChangeListener mOnStateChangeListener;

    public BodyTableLayout(Context context) {
        super(context);
        init();
    }
    public BodyTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {//用来判断是否滚动是否结束，返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void setMaxOffset(int offset) {
        mMaxOffset=offset;
    }

    public ScrollState getState() {
        return state;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (state == ScrollState.HIDE) {
            return false;
        }
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                int moveY = (int) (y - mLastY);
                if (getScrollY() <= 0 && moveY > 0) {
                    int offset = moveY / 2;
                    move(offset);
                }
                mLastY = y;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                changeState();
                break;
        }
        return true;
    }




    private void move(int offset) {
        state = ScrollState.MOVE;
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.pullViewMove(state, -offset);
        }
        scrollBy(0, -offset);
    }

    private void hide() {
        state = ScrollState.HIDE;
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.pullViewHide(state);
        }
        mMoveY = getMeasuredHeight() + Math.abs(getScrollY());//获取偏移量
        smoothScrollTo(0, getScrollY(), 0, -mMoveY, NORMAL_TIME * 3);//向下移动内容
    }

    public void hide(int time) {
        state = ScrollState.HIDE;
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.pullViewHide(state);
        }
        mMoveY = getMeasuredHeight() + Math.abs(getScrollY());
        smoothScrollTo(0, getScrollY(), 0, -mMoveY, time);
    }

    private void show() {
        state = ScrollState.SHOW;
        smoothScrollTo(0, getScrollY(), 0, -getScrollY(),
                getScrollY());
    }

    private void changeState() {
        if (Math.abs(getScrollY()) > mMaxOffset ) {
            hide();
        } else {
            show();
        }
    }

    public void open() {
        state = ScrollState.OPEN_START;
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.pullViewOpenStart();
        }
        smoothScrollTo(0, -mMoveY, 0, mMoveY, NORMAL_TIME);
        postDelayed(new Runnable() {
            @Override public void run() {
                state = ScrollState.OPEN_FINISH;
                if (mOnStateChangeListener != null) {
                    mOnStateChangeListener.pullViewOpenFinish();
                }
            }
        }, NORMAL_TIME);
    }

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        mOnStateChangeListener = listener;
    }

    public void smoothScrollTo(int startX, int startY,
                               int dx, int dy, int duration) {
        //滚动，startX, startY为开始滚动的位置，dx,dy为滚动的偏移量, duration为完成滚动的时间
        mScroller.startScroll(startX, startY, dx, dy, duration);// 调用startScroll()方法来初始化滚动数据并刷新界面
       invalidate();
    }
}
