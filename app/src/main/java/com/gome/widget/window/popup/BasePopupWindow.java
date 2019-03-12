package com.gome.widget.window.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.gome.widget.R;
import com.gome.widget.window.IDecorWindow;
import com.gome.widget.window.builder.BasePopupBuilder;
import com.gome.widget.window.decorchild.DecorChildView;
import com.gome.widget.window.helper.BasePopupHelper;
import com.gome.widget.window.windowenum.WindowEnum;

/**
 * Created by yangjian on 2019/3/6.
 */

public class BasePopupWindow<B extends BasePopupBuilder<B>, H extends BasePopupHelper<B>>
        implements IDecorWindow {

    private IDecorWindow mDecorWindow;

    private B mBuilder;

    private H mHelper;

    public BasePopupWindow(Context context, B builder, H helper) {

        this.mBuilder = builder;
        this.mHelper = helper;
        IDecorWindow decorChildView = findDecorChild(context);
        decorChildView.attach(this);
        this.mBuilder.attach(this);
    }

    @Override
    public void attach(IDecorWindow popupWindow) {
        this.mDecorWindow = popupWindow;
    }

    @Override
    public void showPopupWindow(View view, Class<?> cls, int level,IDecorWindow decorWindow) {

        mDecorWindow.showPopupWindow(view, cls, level,decorWindow);
        setOutsideClickHide(mBuilder.getOutsideClickHide(),cls);
        setOutsideTouchable(mBuilder.getOutsideTouchable(),cls);
    }

    public void showPopupWindow(View longitudinalView) {

        showPopupWindow(longitudinalView, mBuilder.getLevel());
    }

    public void showPopupWindow(final View longitudinalView, int level) {
        if (longitudinalView == null || isPopupWindowShow() || mHelper.canNotShowPopupWindow()) {
            return;
        }
        int height = getCorrectionHeight(longitudinalView);
        final Rect startPosition = new Rect();
        longitudinalView.getGlobalVisibleRect(startPosition);
        startPosition.top = startPosition.top - height <= 0 ? 0: startPosition.top - height;
        final View popupWindowView = this.mHelper.attachView(mBuilder, getDecorView(), this);
        if (popupWindowView == null) {
            throw new NullPointerException(this.getClass().getCanonicalName() + " 获取加载的popupWindwoView 为空");
        }
        final int viewWidth = longitudinalView.getMeasuredWidth();
        final Rect endPosition = new Rect();
        getDecorView().getGlobalVisibleRect(endPosition);
        endPosition.top = endPosition.top - height <= 0 ? 0: endPosition.top - height;

        WindowEnum windowEnum = mBuilder.getWindowEnum();
        windowEnum = windowEnum == null ? WindowEnum.WINDOW_LEFT : windowEnum;
        final WindowEnum wEnum = windowEnum;
        popupWindowView.setVisibility(View.INVISIBLE);
        showPopupWindow(popupWindowView, mHelper.getClass(), level,this);
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) popupWindowView.getLayoutParams();
        popupWindowView.post(new Runnable() {
            @Override
            public void run() {
                int longitudinalY = startPosition.top + mBuilder.getOffY() + mHelper.postVectorY(BasePopupWindow.this,longitudinalView);
                int longitudinalX = startPosition.left + mBuilder.getOffX() + mHelper.postVectorX(BasePopupWindow.this,longitudinalView);
                int maxWidthSize = mDecorWindow.getDecorView().getMeasuredWidth();
                int contentViewWidthSize = mHelper.getContentView().getMeasuredWidth();
                int popupWidth = popupWindowView.getMeasuredWidth();
                if (wEnum == WindowEnum.WINDOW_LEFT) {
                    params.topMargin = longitudinalY;
                    params.leftMargin = longitudinalX;
                    if(longitudinalX + contentViewWidthSize > maxWidthSize){

                        int leftMargin = longitudinalX - (longitudinalX + contentViewWidthSize - maxWidthSize);
                        params.leftMargin = leftMargin < 0 ? 0: leftMargin;
                    }
                } else if (wEnum == WindowEnum.WINDOW_CENTER) {
                    params.topMargin = longitudinalY;
                    params.leftMargin = longitudinalX - (popupWidth - viewWidth) / 2 < 0 ? 0 : longitudinalX - (popupWidth - viewWidth) / 2;

                    if(params.leftMargin + contentViewWidthSize > maxWidthSize){

                        int leftMargin = params.leftMargin - (params.leftMargin + contentViewWidthSize - maxWidthSize);
                        params.leftMargin = leftMargin < 0 ? 0: leftMargin;
                    }

                } else if (wEnum == WindowEnum.WINDOW_RIGHT) {
                    params.topMargin = longitudinalY;
                    params.leftMargin = longitudinalX + viewWidth - popupWidth < endPosition.left ? endPosition.left : longitudinalX + viewWidth - popupWidth;
                    if(params.leftMargin + contentViewWidthSize > maxWidthSize){
                        int leftMargin = params.leftMargin - (params.leftMargin + contentViewWidthSize - maxWidthSize);
                        params.leftMargin = leftMargin < 0 ? 0: leftMargin;
                    }
                }
                popupWindowView.setLayoutParams(params);
                mHelper.showPopupWindow(BasePopupWindow.this);
                popupWindowView.setVisibility(View.VISIBLE);
            }
        });
    }

    private int getCorrectionHeight(View view){
        int height = 0;
        Context context = view.getContext();
        if(context instanceof Activity && mBuilder.isFitsSystemWindows()){
            Activity activity = (Activity) context;
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            int vis = decorView.getSystemUiVisibility();
            if(vis != 0){
                return -view.getMeasuredHeight();
            }
        }

        ViewGroup decorView = ((Activity)context).findViewById(android.R.id.content);
        if(context instanceof AppCompatActivity &&
                decorView.getParent() != null &&
                decorView.getParent() instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) decorView.getParent();
            for(int i = 0 ; i < decorView.getChildCount() ; i++){
                if(decorView != viewGroup.getChildAt(i)){
                    height += viewGroup.getChildAt(i).getMeasuredHeight();
                }
            }

        }
        return height;
    }

    public int on(Context context,View anchor){

        return context.getResources().getDisplayMetrics().heightPixels - anchor.getHeight();
    }

    @Override
    public void hidePopupWindow() {

        hidePopupWindow(mHelper.getClass());
    }

    @Override
    public void hidePopupWindow(final Class<?> cls) {

        if (mHelper != null && mHelper.canNotHidePopupWindow()) {
            return;
        }
        if (isPopupWindowShow(mHelper.getClass())) {
            int duration =  mHelper.hidePopupWindowDuration();
            if (duration > 0) {
                this.mHelper.getContentView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDecorWindow.hidePopupWindow(cls);
                    }
                }, duration);
            } else {
                mDecorWindow.hidePopupWindow(cls);
            }
        }
    }

    @Override
    public ViewGroup getDecorView() {
        return this.mDecorWindow.getDecorView();
    }

    @Override
    public boolean isPopupWindowShow(Class<?> cls) {
        return mDecorWindow.isPopupWindowShow(cls);
    }

    @Override
    public int getViewCurentLevel(Class<?> cls) {
        return mDecorWindow.getViewCurentLevel(cls);
    }

    /**
     * 获取当前控件实际层级
     * @return
     */
    public int getViewCurentLevel(){

        return getViewCurentLevel(mHelper.getClass());
    }

    /**
     * 获取控件设置的层级
     * @return
     */
    public int getViewLevel() {

        return getViewLevel(mHelper.getClass());
    }

    @Override
    public int getViewLevel(Class<?> cls) {
        return mDecorWindow.getViewLevel(cls);
    }

    @Override
    public void setOutsideTouchable(boolean outCanTouch, Class<?> cls) {
        this.mDecorWindow.setOutsideTouchable(outCanTouch,cls);
    }

    @Override
    public void setOutsideClickHide(boolean outClickHide, Class<?> cls) {

        this.mDecorWindow.setOutsideClickHide(outClickHide,cls);
    }

    public boolean isPopupWindowShow() {
        return isPopupWindowShow(mHelper.getClass());
    }

    private IDecorWindow findDecorChild(Context context) {
        Activity activity = (Activity) context;
        View decorView = activity.findViewById(android.R.id.content);
        Object decorObject = decorView.getTag(R.id.decor_view_window_id);
        if (decorObject == null) {
            return new DecorChildView(context);
        } else {
            return (IDecorWindow) decorObject;
        }
    }
}
