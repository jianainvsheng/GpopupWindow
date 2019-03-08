package com.gome.widget;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.gome.widget.test.GWindowCenterLevelNumberHelper;
import com.gome.widget.test.GWindowCenterNumberHelper;
import com.gome.widget.test.GWindowHelper;
import com.gome.widget.test.GWindowNumberLeftHelper;
import com.gome.widget.test.GWindowRightNumberHelper;
import com.gome.widget.test.widget.TestLayout;
import com.gome.widget.window.popup.gwindow.GPopupWindow;
import com.gome.widget.window.windowenum.WindowEnum;

public class MainActivity extends AppCompatActivity {

    GPopupWindow gPopupWindow = null;

    GPopupWindow lPopupWindow = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        TestLayout layout1 = findViewById(R.id.test_layout1);
        layout1.setTag("test_layout1");

        TestLayout layout2 = findViewById(R.id.test_layout2);
        layout2.setTag("test_layout2");

        TestLayout layout3 = findViewById(R.id.test_layout3);
        layout3.setTag("test_layout3");
     }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        System.out.println("========dispatchTouchEvent===MainActivity=======");
        return super.dispatchTouchEvent(ev);
    }

    public void onclick(View view){

        if(view.getId() == R.id.popup){

            if(gPopupWindow == null){
                gPopupWindow = GPopupWindow.createPopupWindow(this, GWindowHelper.class)
                        .setWindowEnum(WindowEnum.WINDOW_CENTER)
                        .setLevel(1)
                        .showPopupView(view);
            }else{
                if(gPopupWindow.isPopupWindowShow()){

                    gPopupWindow.hidePopupWindow();
                }else{
                    gPopupWindow.showPopupWindow(view);
                }
            }
        }else if(view.getId() == R.id.bottom){

            GPopupWindow.createPopupWindow(this, GWindowRightNumberHelper.class)
                    .setWindowEnum(WindowEnum.WINDOW_RIGHT)
                    .setOffY(- view.getMeasuredHeight())
                    .setOutsideClickHide(true)
                    .showPopupView(view);
        }else if(view.getId() == R.id.bottom_right){

            GPopupWindow.createPopupWindow(this, GWindowNumberLeftHelper.class)
                    .setWindowEnum(WindowEnum.WINDOW_LEFT)
                    .setOffY(- view.getMeasuredHeight())
                    .showPopupView(view);
        }else if(view.getId() == R.id.center){
            GPopupWindow.createPopupWindow(this, GWindowCenterNumberHelper.class)
                    .setWindowEnum(WindowEnum.WINDOW_CENTER)
                    .setOffY(- view.getMeasuredHeight())
                    .showPopupView(view);
        }else if(view.getId() == R.id.center_view){

            if(lPopupWindow == null){
                lPopupWindow = GPopupWindow.createPopupWindow(this, GWindowCenterLevelNumberHelper.class)
                        .setWindowEnum(WindowEnum.WINDOW_CENTER)
                        .setLevel(2)
                        .setOffY(- view.getMeasuredHeight())
                        .showPopupView(view);
            }else{

                if(lPopupWindow.isPopupWindowShow()){
                    lPopupWindow.hidePopupWindow();
                }else{
                    lPopupWindow.showPopupWindow(view);
                }
            }
        }
    }
}
