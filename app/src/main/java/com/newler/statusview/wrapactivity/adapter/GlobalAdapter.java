package com.newler.statusview.wrapactivity.adapter;

import android.view.View;

import com.newler.state.StateManager;
import com.newler.state.ViewState;
import com.newler.statusview.wrapactivity.adapter.view.GlobalLoadingStatusView;

import org.jetbrains.annotations.NotNull;


/**
 * @author billy.qi
 * @since 19/3/18 18:37
 */
public class GlobalAdapter implements StateManager.Adapter {


    @NotNull
    @Override
    public View getView(@NotNull StateManager.Holder holder, @ViewState int viewState) {
        return new GlobalLoadingStatusView(holder.getContext(),
                holder.getViewStateListener(ViewState.LOAD_FAILED), viewState);
    }
}
