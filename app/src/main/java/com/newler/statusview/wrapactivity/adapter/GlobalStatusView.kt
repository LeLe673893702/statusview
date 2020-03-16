package com.newler.statusview.wrapactivity.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.newler.state.StateManager
import com.newler.state.ViewState
import com.qmuiteam.qmui.widget.QMUIEmptyView

/**
 *
 * @what
 * @author newler
 * @date 2020/2/17
 *
 */
class GlobalStatusView(context:Context,
                       private var viewState:Int?,
                       private var holder: StateManager.Holder
) : QMUIEmptyView(context)  {

    init {
        showState()
    }

    private fun showState() {
        when(viewState) {
            ViewState.LOADING ->
                show(true)
            ViewState.EMPTY_DATA ->
                show("空页面", null)
            ViewState.LOAD_FAILED ->
                show(false,
                    "加载失败",
                    null,
                    "点击重试"
                ) { holder.getViewStateListener(ViewState.LOAD_FAILED) }
        }
    }

}