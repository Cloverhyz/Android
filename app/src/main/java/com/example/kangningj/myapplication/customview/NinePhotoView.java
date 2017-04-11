package com.example.kangningj.myapplication.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.kangningj.myapplication.R;
import com.example.kangningj.myapplication.utils.PixelUtil;

import java.util.ArrayList;

/**
 * Created by kangningj on 2017/2/9.
 */

public class NinePhotoView extends ViewGroup {

    public static final int MAX_PHOTO_NUMBER = 9;
    private int[] constImageIds = { R.drawable.girl_0, R.drawable.girl_1,
            R.drawable.girl_2, R.drawable.girl_3, R.drawable.girl_4,
            R.drawable.girl_5, R.drawable.girl_6, R.drawable.girl_7,
            R.drawable.girl_8 };
    // horizontal space among children views
    int hSpace = PixelUtil.dip2px(getContext(),10);
    int vSpace = PixelUtil.dip2px(getContext(),10);
    // every child view width and height.
    int childWidth = 0;
    int childHeight = 0;

    //store images res id
    ArrayList<Integer> mImageResArrayList = new ArrayList<>(9);
    private View addPhotoView;

    public NinePhotoView(Context context) {
        super(context, null);
    }

    public NinePhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NinePhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.NinePhotoView,0,0);
        hSpace = typedArray.getDimensionPixelSize(
                R.styleable.NinePhotoView_ninephoto_hspace, hSpace);
        vSpace = typedArray.getDimensionPixelSize(
                R.styleable.NinePhotoView_ninephoto_vspace, vSpace);
        typedArray.recycle();
        addPhotoView = new View(context);
        addView(addPhotoView);
        mImageResArrayList.add(new Integer(0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int rw = MeasureSpec.getSize(widthMeasureSpec);
        int rh = MeasureSpec.getSize(heightMeasureSpec);
        childWidth = (rw-2*hSpace)/3;
        childHeight = childWidth;
        int childCount = this.getChildCount();
        for(int i = 0;i<childCount;i++){
            View child = this.getChildAt(i);
//            this.measureChildren(widthMeasureSpec,heightMeasureSpec);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            layoutParams.left = (i % 3) * (childWidth + hSpace);
            layoutParams.top = (i / 3) * (childWidth + vSpace);
        }
        int vw = rw;
        int vh = rh;
        if (childCount < 3) {
            vw = childCount * (childWidth + hSpace);
        }
        vh = ((childCount + 3) / 3) * (childWidth + vSpace);
        setMeasuredDimension(vw, vh);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i0, int i1, int i2, int i3) {
        int childCount = this.getChildCount();
        for(int i = 0;i<childCount;i++){
            View child = this.getChildAt(i);
            LayoutParams layoutParams= (LayoutParams) child.getLayoutParams();
            child.layout(layoutParams.left,layoutParams.top,layoutParams.left+childWidth,layoutParams.top+childHeight);
            if (i == mImageResArrayList.size() - 1 && mImageResArrayList.size() != MAX_PHOTO_NUMBER) {
                child.setBackgroundResource(R.drawable.defhead);
                child.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        addPhotoBtnClick();
                    }
                });
            }else {
                child.setBackgroundResource(constImageIds[i]);
                child.setOnClickListener(null);
            }
        }
    }

    public void addPhoto() {
        if (mImageResArrayList.size() < MAX_PHOTO_NUMBER) {
            View newChild = new View(getContext());
            addView(newChild);
            mImageResArrayList.add(new Integer(0));
            requestLayout();
            invalidate();
        }
    }

    public void addPhotoBtnClick() {
        final CharSequence[] items = { "Take Photo", "Photo from gallery" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                addPhoto();
            }

        });
        builder.show();
    }
    

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public int left = 0;
        public int top = 0;

        public LayoutParams(Context arg0, AttributeSet arg1) {
            super(arg0, arg1);
        }

        public LayoutParams(int arg0, int arg1) {
            super(arg0, arg1);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams arg0) {
            super(arg0);
        }
    }

    @Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(
            AttributeSet attrs) {
        return new NinePhotoView.LayoutParams(getContext(), attrs);
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(
            android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof NinePhotoView.LayoutParams;
    }

}
