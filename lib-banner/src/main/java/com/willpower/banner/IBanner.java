package com.willpower.banner;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public interface IBanner {

    String TAG = "IBanner";

    int FAST = 1;

    int DELAY = 2;

    int VIDEO = 1;

    int IMAGE = 2;

    int DEFAULT_BANNER_SIZE = 4;

    IBanner bind(FragmentActivity activity);

    IBanner directory(String directory);

    IBanner defaultBanners(int... defaultBanners);

    IBanner interval(long interval);

    IBanner animator(Animator animator);

    IBanner addVideo(String path);

    IBanner addImage(String path);

    IBanner addImage(int res);

    IBanner turnDuration(int duration);

    IBanner clickListener(@NonNull View.OnClickListener listener);

    void next();

    void start();

    void stop();

    void release();

    void refreshData();

    enum Animator {
        ROTATE, TRANSITION3D, TRANSITION_X, TRANSITION_Y, ALPHA, SCALE, NONE
    }
}
