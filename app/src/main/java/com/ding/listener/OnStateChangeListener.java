package com.ding.listener;


import com.ding.model.ScrollState;

public interface OnStateChangeListener {

  void pullViewHide(ScrollState state);

  void pullViewMove(ScrollState state, int offset);

  void pullViewOpenStart();

  void pullViewOpenFinish();
}
