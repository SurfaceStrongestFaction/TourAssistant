package com.daoshengwanwu.android.tourassistant.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import com.daoshengwanwu.android.tourassistant.R;


public class CustomRatingBar extends LinearLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    private static final String ATTR_NUM_STAR = "numStars";
    private static final String ATTR_RATING = "rating";

    private int mNumStar;
    private float mRating;
    private List<StarImageView> mStarImgs = new ArrayList<>();


    public CustomRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);

        mNumStar = attrs.getAttributeIntValue(NAMESPACE, ATTR_NUM_STAR, 5);
        mRating = attrs.getAttributeFloatValue(NAMESPACE, ATTR_RATING, 0.0f);

        int i = 0;
        StarImageView starImg;
        while (i < mNumStar) {
            starImg = new StarImageView(getContext());
            addView(starImg);
            mStarImgs.add(starImg);

            i++;
        }

        setRating(mRating);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        if (params.height >= 0) {
            setMeasuredDimension(5 * params.height, params.height);
        }
    }

    public void setRating(float rating) {
        if (rating < 0) {
            mRating = 0;
        }
        mRating = rating > mNumStar ? mNumStar : rating;

        for (int i = 0; i < (int)mRating && i < mNumStar; i++) {
            mStarImgs.get(i).setImageResource(R.drawable.star);
        }

        for (int i = (int)mRating; i < mNumStar && i < mNumStar; i++) {
            mStarImgs.get(i).setImageResource(R.drawable.star1);
        }
    }

    public float getRating() {
        return mRating;
    }
}
