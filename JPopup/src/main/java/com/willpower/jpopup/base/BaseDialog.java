package com.willpower.jpopup.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.willpower.jpopup.R;

import java.util.Objects;

public abstract class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        this(context,R.style.JDialog);
        init();
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }


    private void init(){
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void setContentView(@IdRes int layoutId) {
        setContentView(View.inflate(getContext(), layoutId, null));
    }

    @Override
    public void setContentView(View v) {
        FrameLayout root = (FrameLayout) View.inflate(getContext(), R.layout.layout_base_dialog, null);
        root.addView(v, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        super.setContentView(root);
    }
}
