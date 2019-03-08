package com.gome.widget.test;
import android.view.View;
import android.widget.TextView;

import com.gome.widget.R;
import com.gome.widget.window.builder.BasePopupBuilder;
import com.gome.widget.window.helper.BasePopupHelper;
import com.gome.widget.window.popup.BasePopupWindow;

/**
 * Created by yangjian on 2019/3/6.
 */

public class GWindowCenterLevelNumberHelper extends BasePopupHelper<BasePopupBuilder> {

    private TextView mTextNumber;

    @Override
    public int onCreateViewLayoutId() {
        return R.layout.activity_popup_gouwu_number;
    }

    @Override
    public void onBuilder(View view, BasePopupBuilder builder, final BasePopupWindow decorWindow) {

        this.mTextNumber = view.findViewById(R.id.popup_number);
    }

    @Override
    public int postVectorY(BasePopupWindow decorWindow,View longitudinalView) {
        return -mTextNumber.getMeasuredHeight();
    }

    @Override
    public int postVectorX(BasePopupWindow decorWindow,View longitudinalView) {
        return 0;
    }


    @Override
    public void showPopupWindow(BasePopupWindow decorWindow) {


    }

    @Override
    public int hidePopupWindowDuration() {

        return 0;
    }

    @Override
    public boolean canNotHidePopupWindow() {
        return false;
    }

    @Override
    public boolean canNotShowPopupWindow() {
        return false;
    }

}
