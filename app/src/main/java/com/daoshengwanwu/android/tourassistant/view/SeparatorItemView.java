package com.daoshengwanwu.android.tourassistant.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.utils.DisplayUtil;


public class SeparatorItemView extends RelativeLayout implements Runnable {
    private static final String sNamespace = "http://schemas.android.com/apk/res/android";

    private RelativeLayout mRoot;
    private TextView mTv;
    private ImageView mIv;
    private float mTextSize_sp = 16.0f;


    public SeparatorItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.home_rv_second_item, this);

        mRoot = (RelativeLayout)findViewById(R.id.home_rv_second_item_root);
        mTv = (TextView)findViewById(R.id.near_by_spot_recommend_text);
        mIv = (ImageView)findViewById(R.id.near_by_spot_recommend_img);

        //解析textSize属性
        String textSizeStr = attrs.getAttributeValue(sNamespace, "textSize");
        if (null != textSizeStr && !("".equals(textSizeStr))) {
            int len = textSizeStr.length();

            String textSizeTypeStr = textSizeStr.substring(len - 2, len);
            textSizeStr = textSizeStr.substring(0, len - 2);
            float textSizeFloat = Float.parseFloat(textSizeStr);

            switch (textSizeTypeStr) {
                case "sp": {
                    mTextSize_sp = textSizeFloat;
                } break;
                case "dp": {
                    mTextSize_sp = DisplayUtil.px2sp(context, DisplayUtil.dip2px(context, textSizeFloat));
                } break;
                case "px": {
                    mTextSize_sp = DisplayUtil.px2sp(context, textSizeFloat);
                } break;
            }//switch
        }//if

        post(this);
    }//con_SeparatorItemView

    @Override
    public void run() {
        int lineHeight = (int)(DisplayUtil.getTextHeightFromSp(getContext(), mTextSize_sp));

        //设置TextView的文字大小
        mTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize_sp);

        //调整root高度
        ViewGroup.LayoutParams params = mRoot.getLayoutParams();
        params.height = (int)(lineHeight * 1.9);
        mRoot.setLayoutParams(params);

        //调整iv宽高
        params = mIv.getLayoutParams();
        params.width = params.height = (int)(lineHeight * 1.2);
        mIv.setLayoutParams(params);

        //调整tv水平位置
        mTv.setX(mTv.getX() - DisplayUtil.dip2px(getContext(), 5.0f));
        mIv.setX(mIv.getX() - DisplayUtil.dip2px(getContext(), 5.0f));
    }
}
