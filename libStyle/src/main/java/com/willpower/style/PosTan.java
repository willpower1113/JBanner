package com.willpower.style;

import android.graphics.PathMeasure;

public class PosTan {
    private float[] pos;
    private PathMeasure mPathMeasure;
    private float mDistance;

    public PosTan(PathMeasure pathMeasure, float distance) {
        this.mPathMeasure = pathMeasure;
        this.mDistance = distance;
    }

    public float[] moveAndGetNewPosition(float offset) {
        mDistance += offset;
        //边界处理
        if (mDistance < 0) {
            mDistance += mPathMeasure.getLength();
        } else if (mDistance > mPathMeasure.getLength()) {
            mDistance -= mPathMeasure.getLength();
        }
        pos = new float[2];
        mPathMeasure.getPosTan(mDistance, pos, new float[2]);
        return pos;
    }

}
