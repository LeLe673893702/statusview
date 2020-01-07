package com.newler.statusview;

import android.app.Application;

import com.newler.state.StateManager;
import com.newler.statusview.wrapactivity.adapter.GlobalAdapter;

/**
 * @author billy.qi
 * @since 19/3/19 15:27
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        StateManager.getInstance().initAdapter(new GlobalAdapter());
    }
}
