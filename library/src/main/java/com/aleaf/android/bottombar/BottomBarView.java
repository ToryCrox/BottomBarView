package com.aleaf.android.bottombar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.aleaf.android.R;

import java.util.List;

/**
 * @author tory
 * @date 2019/4/30
 * @des:
 */
public class BottomBarView extends ViewGroup implements View.OnClickListener {

    private int mTextSize;
    private int mIconSize;
    private int mIconMainSize;
    private int mItemBackground;
    private boolean mHasMainIcon;
    private ColorStateList mItemTint;
    private OnItemSelectedListener mListener;
    private int mBackgroundColor;
    private Path mBackgroundPath = new Path();
    private Paint mPaint = new Paint();

    public BottomBarView(Context context) {
        this(context, null);
    }

    public BottomBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        BottomBarUtil.checkAppCompatTheme(context);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.BottomBarView, defStyleAttr, R.style.WidgetBottomBarView);

        mItemTint = a.getColorStateList(R.styleable.BottomBarView_bbv_itemTint);
        mTextSize = a.getDimensionPixelSize(R.styleable.BottomBarView_bbv_itemLabelSize, 0);
        mIconSize = a.getDimensionPixelSize(R.styleable.BottomBarView_bbv_itemIconSize, 0);
        mIconMainSize = a.getDimensionPixelSize(R.styleable.BottomBarView_bbv_itemIconMainSize, 0);
        mItemBackground = a.getResourceId(R.styleable.BottomBarView_bbv_itemBackground, 0);
        mBackgroundColor = a.getColor(R.styleable.BottomBarView_bbv_backgroundColor, 0);
        Log.d("TAGTAG", "mBackgroundColor=#" + Integer.toHexString(mBackgroundColor));
        if (mIconMainSize < mIconSize) {
            throw new IllegalStateException("itemIconMainSize must be large than " +
                    "itemIconSize, mIconSize=" + mIconSize + ", mIconMainSize=" + mIconMainSize);
        }

        if (a.hasValue(R.styleable.BottomBarView_bbv_menu)) {
            List<MMenuItem> items = BottomBarUtil.inflateMenu(
                    context, a.getResourceId(R.styleable.BottomBarView_bbv_menu, 0));
            buildMenuView(items);
        }

        a.recycle();
        setClipToPadding(false);
        setClipChildren(false);
        setWillNotDraw(false);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mBackgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private void buildMenuView(List<MMenuItem> menuItems) {
        removeAllViews();
        mHasMainIcon = false;
        for (MMenuItem menuItem : menuItems) {
            BottomBarItemView itemView = newItemView();
            itemView.setTextSize(mTextSize);
            itemView.setIconSize(mIconSize);
            itemView.setTextColor(mItemTint);
            itemView.setBackgroundResource(mItemBackground);
            itemView.setId(menuItem.id);
            itemView.setText(menuItem.title);
            if (menuItem.isMain) {
                if (mHasMainIcon) {
                    throw new IllegalStateException("max main icon is 1");
                }
                mHasMainIcon = true;
                itemView.setIconSize(mIconMainSize);
            } else {
                itemView.setIconTint(mItemTint);
            }
            itemView.setIcon(menuItem.iconRes);
            itemView.setIsMainIcon(menuItem.isMain);
            addView(itemView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            itemView.setOnClickListener(this);
        }
    }

    private BottomBarItemView newItemView() {
        return new BottomBarItemView(getContext());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int diffWidth = 0;
        if (mHasMainIcon) {
            diffWidth = mIconMainSize - mIconSize;
        }
        int height = 0;
        int count = getChildCount();
        int itemWidth = (width - diffWidth) / count;
        for (int i = 0; i < count; i++) {
            BottomBarItemView child = (BottomBarItemView) getChildAt(i);
            int realWidth = child.isMainIcon() ? itemWidth + diffWidth : itemWidth;
            child.measure(MeasureSpec.makeMeasureSpec(realWidth, MeasureSpec.EXACTLY),
                    heightMeasureSpec);
            if (!child.isMainIcon()) {
                height = Math.max(height, child.getMeasuredHeight());
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int bottom = b - t - getPaddingBottom();
        int count = getChildCount();
        mBackgroundPath.reset();
        mBackgroundPath.moveTo(0, 0);
        for (int i = 0; i < count; i++) {
            BottomBarItemView child = (BottomBarItemView) getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(left, bottom - height,
                    left + width, bottom);
            left += width;
            if (!child.isMainIcon()) {
                mBackgroundPath.lineTo(left, 0);
            } else {
                int start = left - width;
                int middle = start + width / 2;
                int top = bottom - height;
                float controlLength = width * 3f / 8;
                mBackgroundPath.cubicTo(start + controlLength, 0,
                        middle - controlLength, top, middle, top);
                mBackgroundPath.cubicTo(middle + controlLength, top,
                        left - controlLength, 0, left, 0);
            }
        }
        mBackgroundPath.lineTo(r - l, b - t);
        mBackgroundPath.lineTo(0, b - t);
        mBackgroundPath.close();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mListener = listener;
    }


    public void setSelectedItemId(int id) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            BottomBarItemView child = (BottomBarItemView) getChildAt(i);
            child.setChecked(child.getId() == id);
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener == null || mListener.onItemSelected(v.getId())) {
            setSelectedItemId(v.getId());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mBackgroundPath, mPaint);
        super.onDraw(canvas);
    }

    public interface OnItemSelectedListener {
        boolean onItemSelected(int id);
    }
}
