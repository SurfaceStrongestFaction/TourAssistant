package com.daoshengwanwu.android.tourassistant.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daoshengwanwu.android.tourassistant.activity.ScenicspotActivity;
import com.daoshengwanwu.android.tourassistant.model.HomeListSpotItem;
import com.daoshengwanwu.android.tourassistant.model.SpotItemBase;
import com.daoshengwanwu.android.tourassistant.model.SpotItemBase.SpotItemType;
import com.daoshengwanwu.android.tourassistant.utils.DisplayUtil;
import com.daoshengwanwu.android.tourassistant.utils.GlideImageLoader;
import com.daoshengwanwu.android.tourassistant.view.CustomRatingBar;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import com.daoshengwanwu.android.tourassistant.R;
import com.youth.banner.listener.OnBannerClickListener;


public class HomeSpotAdapter extends RecyclerView.Adapter<HomeSpotAdapter.BaseHolder> {
    private static final String TAG = "HomeSpotAdapter";

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SpotItemBase> mAdapterData;


    public HomeSpotAdapter(Context context, List<SpotItemBase> adapterData) {
        mContext = context;
        mAdapterData = adapterData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }//con_HomeSpotAdapter

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        switch (viewType) {
            case SpotItemType.BANNER: {
                View v = mLayoutInflater.inflate(R.layout.baihaoran_home_list_banner_item, parent, false);

                return new BannerHolder(v);
            }
            case SpotItemType.SPOT_ITEM: {
                View v = mLayoutInflater.inflate(R.layout.bhr_home_spot_item, parent, false);

                return new SpotItemHolder(v);
            }
            case SpotItemType.SEPARATOR: {
                View v = mLayoutInflater.inflate(R.layout.bhr_home_separator_item, parent, false);

                return new SeparatorHolder(v, parent);
            }
            default: return null;
        }//switch
    }//onCreateViewHolder

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        holder.bindData();
    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return mAdapterData.size() + 2;
    }//getItemCount

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0: return SpotItemType.BANNER;
            case 1: return SpotItemType.SEPARATOR;
            default: return getItem(position).getSpotItemType();
        }//switch
    }//getItemViewType

    private int position2Index(int position) {
        return position - 2;
    }//position2Index

    private int index2Position(int index) {
        return index + 2;
    }//index2Position

    private SpotItemBase getItem(int position) {
        return mAdapterData.get(position2Index(position));
    }//getItem


    abstract class BaseHolder extends RecyclerView.ViewHolder {
        public BaseHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData();
    }//class_BaseHolder

    private class BannerHolder extends BaseHolder implements OnBannerClickListener {
        private Banner mBanner;
        private List<Integer> mImgs;


        public BannerHolder(View itemView) {
            super(itemView);
            mBanner = (Banner)itemView.findViewById(R.id.bhr_home_banner);
            mBanner.setOnBannerClickListener(this);
            mBanner.setImageLoader(GlideImageLoader.getInstance());

            //初始化Banner的尺寸
            mBanner.post(new Runnable() {
                @Override
                public void run() {
                    int bannerWidth = mBanner.getWidth();
                    int bannerHeight = (int)(bannerWidth * 9 / 16.0);

                    LayoutParams params = mBanner.getLayoutParams();
                    params.height = bannerHeight;

                    mBanner.setLayoutParams(params);
                }
            });//Runnable
        }//con_BannerHolder

        @Override
        public void bindData() {
            if (null == mImgs) {
                mImgs = new ArrayList<>();
                mImgs.add(R.drawable.wuzhen3);
                mImgs.add(R.drawable.summerpalace3);
                mImgs.add(R.drawable.forbiddencty3);

                mBanner.setImages(mImgs);
                mBanner.post(new Runnable() {
                    @Override
                    public void run() {
                        mBanner.start();
                    }
                });//Runnable
            }//if
        }//bindData

        @Override
        public void OnBannerClick(int position) {
            //do nothing
        }//OnBannerClick
    }//BannerHolder

    private class SeparatorHolder extends BaseHolder implements ViewTreeObserver.OnScrollChangedListener
            ,RecyclerView.OnChildAttachStateChangeListener{
        private View mHideShow;
        private boolean mIsSeparatorAttached = false;
        private boolean mIsBannerAttached = false;


        public SeparatorHolder(View itemView, View parent) {
            super(itemView);

            RecyclerView recycler = (RecyclerView)parent;
            recycler.addOnChildAttachStateChangeListener(this);
            
            mHideShow = parent.getRootView().findViewById(R.id.hide_show);
            ViewTreeObserver observer = itemView.getViewTreeObserver();
            observer.addOnScrollChangedListener(this);
        }//con_SeparatorHolder

        @Override
        public void bindData() {
            //do nothing
        }//bindData

        @Override
        public void onScrollChanged() {
            if (mIsSeparatorAttached) {
                showHide(itemView.getTop());
            }
        }

        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (view.getId() == R.id.separator) {
                mIsSeparatorAttached = true;
                showHide(view.getTop());
            } else if (view.getId() == R.id.bhr_home_banner) {
                mIsBannerAttached = true;
                mHideShow.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {
            if (view.getId() == R.id.separator) {
                mIsSeparatorAttached = false;

                if (!mIsBannerAttached) {
                    mHideShow.setVisibility(View.VISIBLE);
                }
            } else if (view.getId() == R.id.bhr_home_banner) {
                mIsBannerAttached = false;

                mHideShow.setVisibility(View.VISIBLE);
            }
        }

        private void showHide(int top) {
            if (top <= 0) {
                mHideShow.setVisibility(View.VISIBLE);
            } else {
                mHideShow.setVisibility(View.INVISIBLE);
            }
        }
    }//class_SeparatorHolder

    private class SpotItemHolder extends BaseHolder implements View.OnClickListener, Runnable {
        private LinearLayout mRoot;
        private ImageView mSpotImage;
        private RelativeLayout mContent;
        private TextView mSpotName_cn;
        private TextView mSpotName_en;
        private CustomRatingBar mSpotRating;
        private TextView mDistance;


        public SpotItemHolder(View itemView) {
            super(itemView);

            mRoot = (LinearLayout)itemView.findViewById(R.id.spot_item_root);
            mSpotImage = (ImageView)itemView.findViewById(R.id.spot_image_view);
            mContent = (RelativeLayout)itemView.findViewById(R.id.spot_item_content);
            mSpotName_cn = (TextView)itemView.findViewById(R.id.home_list_item_spot_name_cn);
            mSpotName_en = (TextView)itemView.findViewById(R.id.home_list_item_spot_name_en);
            mSpotRating = (CustomRatingBar)itemView.findViewById(R.id.rating_bar);
            mDistance = (TextView)itemView.findViewById(R.id.spot_item_distance);

            //调节评分条高度
            int lineHeight = (int)DisplayUtil.getTextHeightFromSp(mContext, 16.0f);
            LayoutParams params = mSpotRating.getLayoutParams();
            params.height = lineHeight;
            mSpotRating.setLayoutParams(params);

            //设置itemView的背景
            itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.home_list_touch));

            //为条目设置单机事件
            itemView.setOnClickListener(this);

            //调节根视图高度以及图片尺寸
            itemView.post(this);
        }//con_SpotItemHolder

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            HomeListSpotItem item = (HomeListSpotItem)getItem(position);
            String spot_id = item.getId();

            Intent intent = ScenicspotActivity.actionStartActivity(mContext, spot_id);
            ((Activity)v.getContext()).overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
            mContext.startActivity(intent);
        }//onClick

        @Override
        public void bindData() {
            int position = getAdapterPosition();
            HomeListSpotItem item = (HomeListSpotItem)getItem(position);

            if (position > 2) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)itemView.getLayoutParams();
                params.topMargin = DisplayUtil.dip2px(mContext, 0.5f);
                itemView.setLayoutParams(params);
            } else {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)itemView.getLayoutParams();
                params.topMargin = DisplayUtil.dip2px(mContext, 0.0f);
                itemView.setLayoutParams(params);
            }
            mSpotName_cn.setText(item.getSpotName_cn());
            mSpotName_en.setText(item.getSpotName_en());
            mSpotRating.setRating(item.getSpotRate());
            mDistance.setText("距离：" + item.getDistance() + "m");
            mSpotImage.setImageResource(R.drawable.imperialpalace);
            Glide.with(mContext).load(item.getSpotImgUrl()).into(mSpotImage);
        }//bindData

        @Override
        public void run() {
            int contentHeight = mContent.getHeight();
            int rootWidth = mRoot.getWidth();
            int calHeight = rootWidth / 4 + DisplayUtil.dip2px(mContext, 4.0f);

            LayoutParams params = mSpotImage.getLayoutParams();
            if (calHeight > contentHeight) {
                params.height = calHeight;
                params.width = params.height * 4 / 3;
                mSpotImage.setLayoutParams(params);

                params = mContent.getLayoutParams();
                params.height = LayoutParams.MATCH_PARENT;
                mContent.setLayoutParams(params);

                RelativeLayout.LayoutParams rParams = (RelativeLayout.LayoutParams)mDistance.getLayoutParams();
                rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rParams.removeRule(RelativeLayout.BELOW);
                rParams.topMargin = 0;
                mDistance.setLayoutParams(rParams);
            } else {
                params.height = contentHeight;
                params.width = params.height * 4 / 3;
                mSpotImage.setLayoutParams(params);
            }
        }
    }//class_SpotItemHolder
}//class_HomeSpotAdapter
