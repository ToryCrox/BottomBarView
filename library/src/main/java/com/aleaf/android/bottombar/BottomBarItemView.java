package com.aleaf.android.bottombar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aleaf.android.R;

/**
 * @author tory
 * @date 2019/4/30
 * @des:
 */
public class BottomBarItemView extends LinearLayout implements Checkable {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] CHECKED_NOT_STATE_SET = {-android.R.attr.state_checked};

    private ImageView mIconView;
    private CheckedTextView mTextView;
    private boolean mIsMainIcon;
    private boolean mChecked;

    public BottomBarItemView( @NonNull Context context) {
        this(context, null);
    }

    public BottomBarItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarItemView( @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_bottom_bar_item_view, this, true);
        mIconView = findViewById(R.id.icon);
        mTextView = findViewById(R.id.label);
        setOrientation(LinearLayout.VERTICAL);
        int padding = getResources().getDimensionPixelOffset(R.dimen.bottom_bar_view_item_margin);
        setPadding(0, padding, 0, padding);
    }

    void setIsMainIcon(boolean isMainIcon){
        this.mIsMainIcon = isMainIcon;
    }

    boolean isMainIcon(){
        return mIsMainIcon;
    }

    void setTextColor(ColorStateList color){
        mTextView.setTextColor(color);
    }

    void setIconTint(ColorStateList color){
        ImageViewCompat.setImageTintList(mIconView, color);
    }

    void setTextSize(int size){
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    void setIconSize(int size){
        ViewGroup.LayoutParams lp = mIconView.getLayoutParams();
        lp.width = size;
        lp.height = size;
        mIconView.setLayoutParams(lp);
    }

    void setIcon(int iconRes){
        mIconView.setImageResource(iconRes);
    }

    void setIcon(Drawable icon){
        mIconView.setImageDrawable(icon);
    }

    void setText(CharSequence text){
        mTextView.setText(text);
    }


    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked){
            mChecked = checked;
            mIconView.setImageState(checked ? CHECKED_STATE_SET : CHECKED_NOT_STATE_SET,false);
            mTextView.setChecked(checked);
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
