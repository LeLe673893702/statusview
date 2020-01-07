package com.newler.statusview.wrapactivity.adapter;

import com.newler.state.StateManager;
import com.newler.state.StateView;
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
    public StateView getView(StateManager.Holder holder, int status) {
        return new GlobalLoadingStatusView(holder.getContext(),
                holder.getViewStateListener(ViewState.LOAD_FAILED));
    }

}
