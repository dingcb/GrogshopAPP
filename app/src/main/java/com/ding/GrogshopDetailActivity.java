package com.ding;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ding.adapter.GrogshopDetailAdapter;
import com.ding.listener.OnStateChangeListener;
import com.ding.model.ScrollState;
import com.ding.widget.BodyTableLayout;
import com.ding.widget.PullScrollView;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class GrogshopDetailActivity extends SwipeBackActivity {

    private ImageButton im_open_iv;
    private RecyclerView mRecyclerView;
    private GrogshopDetailAdapter adapter;

    private PullScrollView mPullScrollView;
    private BodyTableLayout mBodyTableLayout;
    private RelativeLayout rl_open_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grogshop_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);// 显示标题和子标题
        getSupportActionBar().setDisplayUseLogoEnabled(false);// 显示应用的Logo
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("酒店详情");
//        collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER);//设置收缩后标题的位置
//        collapsingToolbar.setExpandedTitleGravity(Gravity.CENTER);////设置展开后标题的位置
//        collapsingToolbar.setExpandedTitleColor(Color.WHITE);//设置展开后标题的颜色
//        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后标题的颜色
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.mipmap.start).centerCrop().into(imageView);

        initData();
        findViewById();
        addClick();
    }

    public void initData() {
        adapter=new GrogshopDetailAdapter();
    }

    public void findViewById() {
        im_open_iv = (ImageButton) findViewById(R.id.im_open_iv);
        rl_open_iv = (RelativeLayout) findViewById(R.id.rl_open_iv);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mPullScrollView = (PullScrollView) findViewById(R.id.pullscrollview);
        mBodyTableLayout = (BodyTableLayout) findViewById(R.id.rl_body);
        mBodyTableLayout.setMaxOffset(250);//下拉回弹最大高度
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);

    }

    public void addClick() {
        im_open_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBodyTableLayout.open();
            }
        });
        mBodyTableLayout.setOnStateChangeListener(new OnStateChangeListener() {

            @Override
            public void pullViewHide(ScrollState state) {//往下滑动时 展示头布局
                mPullScrollView.setPullRelativeLayoutState(state);
                rl_open_iv.setVisibility(View.VISIBLE);
            }
            @Override
            public void pullViewMove(ScrollState state, int offset) {//主体部分 移动时   offset一直为负
                mPullScrollView.setPullRelativeLayoutState(state);
                mRecyclerView.setVisibility(View.VISIBLE);
                adapter.showImage();//图片缓加载，节省流量和内存
            }

            @Override
            public void pullViewOpenStart() {//主体部分从隐藏 ->刚开始展示
                rl_open_iv.setVisibility(View.GONE);
            }

            @Override
            public void pullViewOpenFinish() {//主体部分从隐藏->展示完成
                LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                manager.scrollToPosition(0);
                mRecyclerView.setVisibility(View.GONE);
                mPullScrollView.setPullRelativeLayoutState(ScrollState.SHOW);
            }
        });
    }

    //设置返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
