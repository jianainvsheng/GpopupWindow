package com.gome.widget.test.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by yangjian on 2019/3/8.
 */

public class TestLayout extends RelativeLayout{

    private String tag = "";
    public TestLayout(Context context) {
        super(context);
    }

    public TestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTag(String tag){

        this.tag = tag;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        System.out.println("======TestLayout=dispatchTouchEvent== " + tag);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        System.out.println("======TestLayout=onTouchEvent== " + tag);
        return super.onTouchEvent(event);
    }
}
