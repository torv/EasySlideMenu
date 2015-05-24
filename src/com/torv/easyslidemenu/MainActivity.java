package com.torv.easyslidemenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity implements OnClickListener, OnTouchListener {

    private LinearLayout mSlideMenulayout;
    private LinearLayout mMainPageLayout;

    private LayoutParams mMenuParams;
    private LayoutParams mMainParams;

    private static final int SLIDE_MENU_PADDING = 50;
    private static final int MOVE_LENGTH_MIN = 200;

    private int mMinMoveLength;
    private float mTouchDownX;

    private boolean isMenuOpened = false;

    private Button mBtnSlideMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSlideMenu();
    }

    private void initSlideMenu() {

        mSlideMenulayout = (LinearLayout) findViewById(R.id.ll_slide_menu);
        mMainPageLayout = (LinearLayout) findViewById(R.id.ll_main_page);

        mMenuParams = (LayoutParams) mSlideMenulayout.getLayoutParams();
        mMainParams = (LayoutParams) mMainPageLayout.getLayoutParams();

        int width = getResources().getDisplayMetrics().widthPixels;
        float density = getResources().getDisplayMetrics().density;

        int menuPadding = (int) (density * SLIDE_MENU_PADDING);
        mMinMoveLength = (int) (density * MOVE_LENGTH_MIN);

        mMenuParams.width = width - menuPadding;
        mMainParams.width = width;

        mSlideMenulayout.setLayoutParams(mMenuParams);
        mMenuParams.leftMargin = 0 - mMenuParams.width;
        mSlideMenulayout.postInvalidate();
        isMenuOpened = false;

        mBtnSlideMenu = (Button) findViewById(R.id.btn_slide_menu);
        mBtnSlideMenu.setOnClickListener(this);

        mMainPageLayout.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_slide_menu:
                if (isMenuOpened) {
                    closeSlideMenu();
                } else {
                    openSlideMenu();
                }
                break;

            default:
                break;
        }
    }

    @SuppressLint("NewApi")
    private void openSlideMenu() {

        isMenuOpened = true;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(mSlideMenulayout, "translationX", mMenuParams.width),
                ObjectAnimator.ofFloat(mMainPageLayout, "translationX", mMenuParams.width));
        animatorSet.start();
    }

    @SuppressLint("NewApi")
    private void closeSlideMenu() {

        isMenuOpened = false;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(mSlideMenulayout, "translationX", 0), ObjectAnimator.ofFloat(mMainPageLayout, "translationX", 0));
        animatorSet.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (view.getId()) {
            case R.id.ll_main_page:

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchDownX = event.getRawX();
                        return true; // return true so will get action up event.

                    case MotionEvent.ACTION_UP:
                        float length = event.getRawX() - mTouchDownX;
                        if (length > mMinMoveLength && !isMenuOpened)
                            openSlideMenu();
                        else if (length < -mMinMoveLength && isMenuOpened)
                            closeSlideMenu();
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (isMenuOpened) {
            closeSlideMenu();
        } else {
            super.onBackPressed();
        }
    }
}
