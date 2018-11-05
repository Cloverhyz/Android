package com.example.kangningj.myapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kangningj.myapplication.R;

/**
 * TODO: document your custom view class.
 */
public class TopBar extends RelativeLayout {
    // 包含TopBar上的元素：左按钮、右按钮、标题
    private Button mLeftButton, mRightButton;
    private TextView mTitleView;
    private String mTitle;
    // 布局属性，用来控制组件元素在ViewGroup中的位置
    private LayoutParams mLeftParams, mRightParams, mTitleParams;

    private int mTitleTextColor;
    private Drawable mRightBackground;
    private int mRightTextColor;
    private String mRightText;
    private Drawable mLeftBackground;
    private float mTitleTextSize;
    private int mLeftTextColor;
    private String mLeftText;
    private topBarClickListener mListener;

    public TopBar(Context context) {
        super(context);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        mLeftText = typedArray.getString(R.styleable.TopBar_leftText);
        mLeftTextColor = typedArray.getColor(R.styleable.TopBar_leftTextColor, 0);
        mLeftBackground = typedArray.getDrawable(R.styleable.TopBar_leftBackground);
        mRightText = typedArray.getString(R.styleable.TopBar_rightText);
        mRightTextColor = typedArray.getColor(R.styleable.TopBar_rightTextColor, 0);
        mRightBackground = typedArray.getDrawable(R.styleable.TopBar_rightBackground);
        mTitleTextSize = typedArray.getDimension(R.styleable.TopBar_titleTextSize, 10);
        mTitleTextColor = typedArray.getColor(R.styleable.TopBar_titleTextColor, 0);
        mTitle = typedArray.getString(R.styleable.TopBar_title);
        typedArray.recycle();

        mLeftButton = new Button(context);
        mRightButton = new Button(context);
        mTitleView = new TextView(context);

        //为创建的组件元素赋值
        //值就来源于我们在引用的xml文件中给对应属性的赋值
        mLeftButton.setTextColor(mLeftTextColor);
        mLeftButton.setBackground(mLeftBackground);
        mLeftButton.setText(mLeftText);

        mRightButton.setTextColor(mRightTextColor);
        mRightButton.setBackground(mRightBackground);
        mRightButton.setText(mRightText);

        mTitleView.setText(mTitle);
        mTitleView.setTextColor(mTitleTextColor);
        mTitleView.setTextSize(mTitleTextSize);
        mTitleView.setGravity(Gravity.CENTER);

        mLeftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(mLeftButton, mLeftParams);

        mRightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(mRightButton, mRightParams);

        mTitleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mTitleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(mTitleView,mTitleParams);

        mLeftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.leftClick();
            }
        });
        mRightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.rightClick();
            }
        });
    }

    public void setOnTopBarClickListener(topBarClickListener mListener){
        this.mListener = mListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public interface topBarClickListener{
        void leftClick();
        void rightClick();
    }


}
