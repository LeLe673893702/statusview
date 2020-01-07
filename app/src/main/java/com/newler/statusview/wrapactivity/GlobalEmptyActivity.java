package com.newler.statusview.wrapactivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.newler.statusview.BaseActivity;

/**
 * demo: loading and load success but empty
 * @author billy.qi
 * @since 19/3/19 21:09
 */
public class GlobalEmptyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
        startLoadData();
    }

    private void startLoadData() {
        showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showEmpty();
            }
        }, 2000);
    }

    @Override
    protected void onLoadRetry() {
        startLoadData();
    }
}
