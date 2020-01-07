package com.newler.state

import androidx.annotation.IntDef

/**
 *
 * @what 状态枚举类
 * @author newler
 * @date 2020/1/7
 *
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(ViewState.LOADING, ViewState.CONTENT, ViewState.LOAD_FAILED, ViewState.EMPTY_DATA)
annotation class ViewState {
    companion object {
        const val LOADING = 1
        const val CONTENT = 2
        const val LOAD_FAILED = 3
        const val EMPTY_DATA = 4
    }
}