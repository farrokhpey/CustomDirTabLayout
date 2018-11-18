package ir.mahfa.customdirtablayout;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;


/**
 * Created by Farrokh on 17/11/2018.
 **/

public class CustomDirTabLayout extends TabLayout {

    private int mode;
    private int requestedTabMinWidth;
    private int INVALID_WIDTH;
    private int scrollableTabMinWidth;
    private int tabGravity;
    private gravity mTabDirection;
    private LinearLayout mSlidingTabIndicator;

    public enum gravity {
        center,
        right,
        left,
        start,
        end
    }

    public CustomDirTabLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomDirTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomDirTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(final Context context, AttributeSet attrs, int defStyleAttr) {

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomDirTabLayout, defStyleAttr, 0);
        mTabDirection = gravity.values()[a.getInt(R.styleable.CustomDirTabLayout_tabsGravity, 0)];
        a.recycle();

        try {
            Field field = TabLayout.class.getDeclaredField("mode");
            field.setAccessible(true);
            mode = field.getInt(this);
            field = TabLayout.class.getDeclaredField("contentInsetStart");
            field.setAccessible(true);
            requestedTabMinWidth = field.getInt(this);
            field = TabLayout.class.getDeclaredField("INVALID_WIDTH");
            field.setAccessible(true);
            INVALID_WIDTH = field.getInt(INVALID_WIDTH);
            field = TabLayout.class.getDeclaredField("scrollableTabMinWidth");
            field.setAccessible(true);
            scrollableTabMinWidth = field.getInt(this);
            field = TabLayout.class.getDeclaredField("tabGravity");
            field.setAccessible(true);
            tabGravity = field.getInt(this);
            field = TabLayout.class.getDeclaredField("slidingTabIndicator");
            field.setAccessible(true);
            mSlidingTabIndicator = (LinearLayout) field.get(this);

            applyModeAndGravity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyModeAndGravity() {
        switch (mode) {
            case MODE_SCROLLABLE:
                switch (mTabDirection) {
                    case left:
                        mSlidingTabIndicator.setGravity(Gravity.LEFT);
                        break;
                    case right:
                        mSlidingTabIndicator.setGravity(Gravity.RIGHT);
                        break;
                    case center:
                        mSlidingTabIndicator.setGravity(Gravity.CENTER_HORIZONTAL);
                        break;
                    case start:
                        mSlidingTabIndicator.setGravity(GravityCompat.START);
                        break;
                    case end:
                        mSlidingTabIndicator.setGravity(GravityCompat.END);
                        break;
                    default:
                        mSlidingTabIndicator.setGravity(Gravity.CENTER_HORIZONTAL);
                        break;
                }
                break;
            case MODE_FIXED:
                mSlidingTabIndicator.setGravity(Gravity.CENTER_HORIZONTAL);
                break;

        }

        updateTabViews();
    }

    private void updateTabViews() {
        for (int i = 0; i < mSlidingTabIndicator.getChildCount(); i++) {
            View child = mSlidingTabIndicator.getChildAt(i);
            child.setMinimumWidth(getTabMinWidth());
            updateTabViewLayoutParams((LinearLayout.LayoutParams) child.getLayoutParams());
            child.requestLayout();
        }
    }

    private int getTabMinWidth() {
        if (requestedTabMinWidth != INVALID_WIDTH) {
            // If we have been given a min width, use it
            return requestedTabMinWidth;
        }
        // Else, we'll use the default value
        return mode == MODE_SCROLLABLE ? scrollableTabMinWidth : 0;
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
        if (mode == MODE_FIXED && tabGravity == GRAVITY_FILL) {
            lp.width = 0;
            lp.weight = 1;
        } else {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            lp.weight = 0;
        }
    }

    public gravity getTabDirection() {
        return mTabDirection;
    }

    public void setTabDirection(gravity mTabDirection) {
        this.mTabDirection = mTabDirection;
    }
}