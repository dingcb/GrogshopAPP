package com.ding.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.ding.model.ScrollState;


public class PullScrollView extends NestedScrollView {

  private ScrollState state = ScrollState.SHOW;
  private int mDownY;
  private int mMoveY;

  public PullScrollView(Context context) {
    super(context);
  }

  public PullScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PullScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }



  public void setPullRelativeLayoutState(ScrollState state) {
    this.state=state;
  }

  /**
   * l, t代表left, top，也就是触摸点相对左上角的偏移量。而oldl, oldt就是滑动前的偏移量。
   */
  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mDownY = (int) ev.getRawY();
        break;

      case MotionEvent.ACTION_MOVE:
        mMoveY = (int) ev.getRawY();
        break;

      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        mDownY = 0;
        mMoveY = 0;
        break;
    }

    if (mMoveY - mDownY < 0) {//向上滑，PullScrollView消费事件
      return super.onInterceptTouchEvent(ev);
    }

    Log.i("DDDD","getScrollY()="+getScrollY());
    if (getScrollY() == 0) {//向下拉时，PullScrollView是不会动的 Y 偏移量为0

      if (state == ScrollState.SHOW || state == ScrollState.MOVE) {//当主体控件展示时，PullScrollView不消费事件
        Log.i("DDDD","state="+state);
        return false;
      }
    }
    return super.onInterceptTouchEvent(ev);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if (state == ScrollState.HIDE) {
      return false;
    }
    return super.onTouchEvent(ev);
  }


}
