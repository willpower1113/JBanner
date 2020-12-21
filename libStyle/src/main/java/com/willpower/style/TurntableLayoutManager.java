package com.willpower.style;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TurntableLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "Turntable";
    List<PosTan> mPositions = null;
    private int itemUseSpace = -1;//水平方向ItemView占用空间
    private float damping = 1.0f;//阻尼系数

    public TurntableLayoutManager(RecyclerView recyclerView) {
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return fill(recycler, dy);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return fill(recycler, -dx);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);//清空views
            return;
        }
        fill(recycler, 0);
    }

    private int fill(RecyclerView.Recycler recycler, int delta) {
        float realOffset = delta / damping;
        /*--------------边界检测-----------------*/
        //将views加入缓存区
        detachAndScrapAttachedViews(recycler);
        /* ---------------- 初始化布局数据-----------------*/
        if (itemUseSpace == -1) {
            View v = recycler.getViewForPosition(0);
            measureChildWithMargins(v, 0, 0);
            itemUseSpace = getDecoratedMeasurementHorizontal(v);
        }
        /*--------------- 开始布局-----------------*/
        if (mPositions == null) {
            Path p = new Path();
            p.moveTo(0, 0);
            p.addCircle(getWidth() / 2, getWidth() / 2, (getWidth() - itemUseSpace) / 2, Path.Direction.CCW);
            p.close();
            final PathMeasure pathMeasure = new PathMeasure(p, false);
            final float pathLength = pathMeasure.getLength();
            mPositions = new ArrayList<>(getItemCount());
            for (int i = 0; i < getItemCount(); i++) {
                float distance = (pathLength / getItemCount()) * i;
                mPositions.add(new PosTan(pathMeasure, distance));
            }
        }

        for (int i = 0; i < mPositions.size(); i++) {
            float[] pos = mPositions.get(i).moveAndGetNewPosition(realOffset);
            int l = (int) pos[0] - (itemUseSpace / 2);
            int t = (int) pos[1] - (itemUseSpace / 2);
            int r = l + itemUseSpace;
            int b = t + itemUseSpace;
            View item = recycler.getViewForPosition(i);
            addView(item);
            measureChildWithMargins(item, 0, 0);
            layoutDecoratedWithMargins(item, l, t, r, b);
        }
        return (int) realOffset;
    }

    private int getDecoratedMeasurementHorizontal(View v) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
        return getDecoratedMeasuredWidth(v) + params.leftMargin + params.rightMargin;
    }
}
