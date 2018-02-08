package com.ding.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ding.R;
import com.ding.model.Status;

/**
 */
public class GrogshopDetailAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {

    private boolean showIm=false;

    public GrogshopDetailAdapter() {
        super( R.layout.adapter_grogshop_detail, DataServer.getSampleData(10));
    }

    @Override
    protected void convert(final BaseViewHolder helper, Status item) {

        if (showIm) {//图片缓加载，节省流量和内存
            Glide.with(mContext).load(R.mipmap.start).crossFade().into((ImageView) helper.getView(R.id.img));
        }
        helper.getView(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=getOnItemChildClickListener()) {
//                    getOnItemChildClickListener().onItemChildClick(mContext, helper.getView(R.id.img), helper.getLayoutPosition());
                }
            }
        });

    }

    public void showImage(){
        if (!showIm) {
            this.showIm = true;
            notifyDataSetChanged();
        }
    }


}
