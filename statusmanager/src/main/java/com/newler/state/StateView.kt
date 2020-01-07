package com.newler.state

import android.view.View

/**
 *
 * @what
 * @author 17173
 * @date 2020/1/7
 *
 */
interface StateView {
    fun getView() : View
    fun showState(state: Int)
}