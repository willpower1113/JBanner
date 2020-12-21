package com.willpower.style;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


public class PandaLayoutManager extends RecyclerView.LayoutManager {
    private int mHorizontalOffset = 0; //水平方向累计偏移量
    private int mFirstVisibilityPos = 0; //屏幕可见的第一个View的Position
    private int mLastVisibilityPos = 0;//屏幕可见的最后一个View的Position
    private float onceCompleteScrollLength = -1f;//一次完整的聚焦滑动所需要移动的距离
    private static final String TAG = "JStyle";

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        onceCompleteScrollLength = -1f;
        //分离全部已有的view，放入临时缓存
        detachAndScrapAttachedViews(recycler);
        fill(recycler, 0);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;
        //位移0、没有子View 当然不移动
        int offsetX = dx;
        if (offsetX == 0 || getChildCount() == 0) {
            return 0;
        }
        mHorizontalOffset += offsetX; //水平方向累计偏移量 累加实际滑动距离
        offsetX = fill(recycler, offsetX);
        return offsetX;
    }

    private int fill(RecyclerView.Recycler recycler, int delta) {
        int resultDelta = fillHorizontalLeft(recycler, delta);
        recycleChildren(recycler);
        return resultDelta;
    }

    /**
     * 水平滚动、向左堆叠布局
     *
     * @param dx 偏移量。手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;
     */
    private int fillHorizontalLeft(RecyclerView.Recycler recycler, int dx) {
        Log.i(TAG, "边界检测: ");
        //----------------1、边界检测-----------------
        int offsetX = dx;
        if (offsetX < 0) {
            //已达左边界
            if (mHorizontalOffset < 0) {
                offsetX = 0;
                mHorizontalOffset = offsetX;
            }
        }
        if (offsetX > 0) {
            //滑动到只剩堆叠view，没有普通view了，说明已经到达右边界了
            if (mLastVisibilityPos - mFirstVisibilityPos <= 0) {
                //因为scrollHorizontallyBy里加了一次dx，现在减回去
                mHorizontalOffset -= offsetX;
                offsetX = 0;
            }
        }

        //分离全部的view，放入临时缓存
        detachAndScrapAttachedViews(recycler);
        Log.i(TAG, "初始化布局数据: ");
        //----------------2、初始化布局数据-----------------
        int startX = getPaddingLeft();
        View tempView = null;
        int tempPosition = -1;
        if (onceCompleteScrollLength == -1f) {
            //因为 mFirstVisibilityPos 在下面可能会被改变，所以用tempPosition暂存一下。【只有初始化时进入一次】
            tempPosition = mFirstVisibilityPos;
            tempView = recycler.getViewForPosition(tempPosition);
            //测量 child 水平方向占用控件大小
            measureChildWithMargins(tempView, 0, 0);
            //获取测量值
            onceCompleteScrollLength = getDecoratedMeasurementHorizontal(tempView);
            Log.i(TAG, "测量控件水平方向占用空间: $onceCompleteScrollLength px");
        }
        //当前"一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT，从右向左移动fraction将从0%到100%）
        float fraction = Math.abs(mHorizontalOffset) % onceCompleteScrollLength / (onceCompleteScrollLength * 1.0f);
        Log.i(TAG, "fraction: " + fraction + "%");
        //堆叠区域view偏移量。在一次完整的聚焦滑动期间，其总偏移量是一个layerPadding的距离
        //普通区域view偏移量。在一次完整的聚焦滑动期间，其总位移量是一个onceCompleteScrollLength
        float normalViewOffset = onceCompleteScrollLength * fraction;
        boolean isLayerViewOffset = false;
        boolean isNormalViewOffset = false;

        //修正第一个可见的view：mFirstVisibilityPos。已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        mFirstVisibilityPos = (int) Math.floor(Math.abs(mHorizontalOffset) / onceCompleteScrollLength);//向下取整
        //临时将 mLastVisibilityPos 赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisibilityPos = getItemCount() - 1;
        //----------------3、开始布局-----------------
        for (int i = mFirstVisibilityPos; i <= mLastVisibilityPos; i++) {
            //属于堆叠区域
            if (i - mFirstVisibilityPos < 1) {
                View item;
                if (i == tempPosition && tempView != null) {
                    //如果初始化数据时已经取了一个临时view，可别浪费了！
                    item = tempView;
                } else {
                    item = recycler.getViewForPosition(i);
                }
                addView(item);
                measureChildWithMargins(item, 0, 0);
                if (!isLayerViewOffset) {
                    isLayerViewOffset = true;
                }

                int l = startX;
                int t = getPaddingTop();
                int r = (startX + getDecoratedMeasurementHorizontal(item));
                int b = getPaddingTop() + getDecoratedMeasurementVertical(item);
                layoutDecoratedWithMargins(item, l, t, r, b);
            } else { //属于普通区域
                View item = recycler.getViewForPosition(i);
                addView(item);
                measureChildWithMargins(item, 0, 0);
                startX += onceCompleteScrollLength;
                if (!isNormalViewOffset) {
                    startX -= normalViewOffset;
                    isNormalViewOffset = true;
                }

                int l = startX;
                int t = getPaddingTop();
                int r = (startX + getDecoratedMeasurementHorizontal(item));
                int b = getPaddingTop() + getDecoratedMeasurementVertical(item);
                layoutDecoratedWithMargins(item, l, t, r, b);
                //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正 mLastVisibilityPos 并跳出遍历
                if (startX + onceCompleteScrollLength > getWidth() - getPaddingRight()) {
                    mLastVisibilityPos = i;
                    break;
                }
            }
        }
        return offsetX;
    }

    @Override
    public void layoutDecoratedWithMargins(@NonNull View child, int left, int top, int right, int bottom) {
        Log.i(TAG, String.format("%d,%d,%d,%d",left,top,right,bottom));
        super.layoutDecoratedWithMargins(child, left, top, right, bottom);
    }

    @Override
    public void onAdapterChanged(@Nullable RecyclerView.Adapter oldAdapter, @Nullable RecyclerView.Adapter newAdapter) {
        //重置数据
        mFirstVisibilityPos = 0;
        mLastVisibilityPos = 0;
        onceCompleteScrollLength = -1f;
        mHorizontalOffset = 0;
    }

    public boolean isAutoMeasureEnabled() {
        return true;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 回收需回收的Item。
     */
    private void recycleChildren(RecyclerView.Recycler recycler) {
        for (RecyclerView.ViewHolder holder : recycler.getScrapList()) {
            removeAndRecycleView(holder.itemView, recycler);
        }
    }

    /**
     * 获取某个childView在水平方向所占的空间，将margin考虑进去
     *
     * @param view
     * @return
     */
    private int getDecoratedMeasurementHorizontal(View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间,将margin考虑进去
     *
     * @param view
     * @return
     */
    private int getDecoratedMeasurementVertical(View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }
}