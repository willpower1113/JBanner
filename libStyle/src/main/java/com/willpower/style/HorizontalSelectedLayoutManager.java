package com.willpower.style;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


public class HorizontalSelectedLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "Selected";
    private int mHorizontalOffset = 0;//水平方向累计位移
    private int itemUsedSpaceInHorizontal = -1;//水平方向占用空间
    private int itemUsedSpaceInlVertical = -1;//垂直方向占用空间
    private int mLastItemInScreen;//屏幕显示的最后一条
    private int mFirstItemInScreen;//屏幕显示的第一条
    private View centerItem;
    private Rect centerRect;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dx == 0 || getChildCount() == 0) return 0;
        mHorizontalOffset += dx;
        return fill(recycler, dx);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        fill(recycler, 0);
    }

    private int fill(RecyclerView.Recycler recycler, int offset) {
        /*边界检测*/
        if (offset < 0) {
            if (mHorizontalOffset < 0) {
                //已达左边界
                mHorizontalOffset = offset = 0;
            }
        } else if (offset > 0) {
            if (mLastItemInScreen == getItemCount() - 1) {
                //已达右边界
                mHorizontalOffset -= offset;
                offset = 0;
            }
        }

        /*取除item，加入到缓存区*/
        detachAndScrapAttachedViews(recycler);

        /*初始化基础数据*/
        View firstItem = null;
        if (itemUsedSpaceInHorizontal == -1 || itemUsedSpaceInlVertical == -1) {
            mFirstItemInScreen = 0;
            firstItem = recycler.getViewForPosition(mFirstItemInScreen);
            //测量 第一条Item
            measureChildWithMargins(firstItem, 0, 0);
            //存储 测量结果
            itemUsedSpaceInHorizontal = getDecoratedMeasurementHorizontal(firstItem);
            itemUsedSpaceInlVertical = getDecoratedMeasurementVertical(firstItem);
        }
        int minChildSize = getWidth() / 3;
        mFirstItemInScreen = (int) Math.floor(Math.abs(mHorizontalOffset) * 1f / minChildSize);
        Log.d(TAG, "mFirstItemInScreen: " + mFirstItemInScreen);
        mLastItemInScreen = getItemCount() - 1;

        /*开始布局*/
        for (int i = mFirstItemInScreen; i <= mLastItemInScreen; i++) {
            View item;
            if (i == 0 && firstItem != null) {
                item = firstItem;
            } else {
                item = recycler.getViewForPosition(i);
            }
            int l;
            int t = getPaddingTop();
            int r;
            int b = t + itemUsedSpaceInlVertical;

            if (i != (mFirstItemInScreen + 3 / 2)) {
                l = getPaddingLeft() + minChildSize * i - mHorizontalOffset;
                r = l + minChildSize;
                addView(item, 0);
            } else {
                l = getWidth() / 4 - mHorizontalOffset;
                r = getWidth() * 3 / 4  - mHorizontalOffset;
                addView(item);
            }
            measureChildWithMargins(item, 0, 0);
            layoutDecoratedWithMargins(item, l, t, r, b);
            if ((l + getPaddingRight()) >= getWidth()) {
                mLastItemInScreen = i;
                break;
            }
        }
        recycleChildren(recycler);
        return offset;
    }

    private void addCenterView(View v) {
        addView(v);
        measureChildWithMargins(centerItem, 0, 0);
        layoutDecoratedWithMargins(centerItem, getWidth() / 4, centerRect.top, getWidth() * 3 / 4, centerRect.bottom);
    }

    private void recycleChildren(RecyclerView.Recycler recycler) {
        for (RecyclerView.ViewHolder holder : recycler.getScrapList()) {
            removeAndRecycleView(holder.itemView, recycler);
        }
    }


    private int getDecoratedMeasurementHorizontal(View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin;
    }

    private int getDecoratedMeasurementVertical(View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }
}
