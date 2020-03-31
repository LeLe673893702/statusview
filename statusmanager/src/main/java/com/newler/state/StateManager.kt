package com.newler.state

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.util.forEach
import java.lang.Exception

/**
 *
 * @what
 * @author newler
 * @date 2020/1/7
 *
 */
class StateManager {
    private var adapter: Adapter ?= null

    companion object {
        @JvmStatic
        val instance : StateManager by lazy {
            StateManager()
        }

        fun from(adapter: Adapter) : StateManager {
            val stateManager = StateManager()
            stateManager.adapter = adapter
            return stateManager
        }
    }

    interface Adapter {
        fun getView(holder: Holder, @ViewState viewState: Int): View
    }

    fun initAdapter(adapter: Adapter) {
        this.adapter = adapter
    }

    fun wrap(activity : Activity) : Holder? {
        val rootViewGroup = activity.window?.decorView?.findViewById<View>(android.R.id.content) as? ViewGroup
        if (rootViewGroup?.childCount?:0 >0) {
            val contentView = rootViewGroup?.getChildAt(0)
            contentView?.apply {
                adapter?.let {
                    return Holder(it, activity,
                        activity.findViewById(android.R.id.content), this)
                }
            }
        }

        return null
    }

    /**
     * 包裹contentView
     */
    fun wrap(contentView : View) : Holder? {
        val parent = contentView.parent
        parent?.let {
            if (it is RelativeLayout || it is ConstraintLayout) {
                return cover(contentView, it)
            } else {
                return warp(contentView, it)
            }
        }

        return null
    }

    /**
     * 如果RelativeLayout或ConstraintLayout平级覆盖
     */
    private fun cover(contentView : View, parent:ViewParent) :Holder? {
        adapter?.let {
            return Holder(it, contentView.context, parent as ViewGroup, contentView)
        }
        return null
    }

    /**
     * 如果是其他布局，FrameLayout将contentView和stateView全部包裹进来
     */
    private fun warp(contentView : View, parent:ViewParent) :Holder? {
        adapter?.let {
            val wrapper = FrameLayout(contentView.context)
            val lp =  contentView.layoutParams
            lp?.let {
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            }

            // 先移除已经添加原来布局的contentView
            if (parent is ViewGroup) {
                val index = parent.indexOfChild(contentView)
                parent.removeView(contentView)
                parent.addView(wrapper, index, FrameLayout.LayoutParams(-1, -1))
            }
            wrapper.addView(contentView, FrameLayout.LayoutParams(-1, -1))
            return Holder(it, contentView.context, wrapper, contentView)
        }

        return null
    }

    class Holder(private val adapter: Adapter, private val context: Context,
                 private val wrapper: ViewGroup, private val contentView: View) {
        private var currentState : Int = ViewState.CONTENT
        private val stateViewListeners: SparseArray<Runnable> by lazy {
            SparseArray<Runnable>(1)
        }
        private val stateViews : SparseArray<View> by lazy {
            SparseArray<View>(4)
        }

        init {
            stateViews.put(ViewState.CONTENT, contentView)
        }

        fun showState(viewState : Int) {
            if (currentState == viewState) {
                return
            }

            currentState = viewState

            try {
                // 页面中包含该状态view
                if (wrapper.indexOfChild(stateViews[viewState]) >= 0) {
                    stateViews.forEach { key, stateView ->
                        stateView.visibility = if (key == viewState) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                    return
                }

                // 先从缓存view中获取，没有缓存则通过adapter获取新的view
                val stateView = stateViews[viewState] ?: adapter.getView(this, viewState)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    stateView.elevation = Float.MAX_VALUE
                }

                // 添加到页面中
                addStateView(stateView, wrapper)

                stateViews.put(viewState, stateView)
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }

        /**
         * RelativeLayout和ConstraintLayout直接覆盖
         */
        private fun addStateView(view: View, wrapper: ViewGroup) {
            if (wrapper is RelativeLayout) {
                val lp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    RelativeLayout.LayoutParams(view.layoutParams as RelativeLayout.LayoutParams)
                } else {
                    RelativeLayout.LayoutParams(view.layoutParams)
                }
                wrapper.addView(view, lp)
                return
            }

            if (wrapper is ConstraintLayout) {
                val lp = ConstraintLayout.LayoutParams(view.layoutParams as ViewGroup.LayoutParams)
                lp.leftToLeft = view.id
                lp.rightToRight = view.id
                lp.topToTop = view.id
                lp.bottomToBottom = view.id
                wrapper.addView(view, lp)
                return
            }

            wrapper.addView(view)
            val lp = view.layoutParams
            lp?.let {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }

        fun showEmpty() {
            showState(ViewState.EMPTY_DATA)
        }

        fun showContent() {
            showState(ViewState.CONTENT)
        }

        fun showLoadFailed() {
            showState(ViewState.LOAD_FAILED)
        }

        fun showLoading() {
            showState(ViewState.LOADING)
        }

        /**
         * 获取stateView，用于部分页面修改控件某些参数的需求
         */
        fun getStateView(viewState: Int) : View? {
            return stateViews[viewState]
        }

        fun withRetryListener(retryListener: Runnable) : Holder {
            stateViewListeners.put(ViewState.LOAD_FAILED, retryListener)
            return this
        }

        fun withExtendListener(viewState: Int, listener: Runnable): Holder {
            stateViewListeners.put(viewState, listener)
            return this
        }

        fun getViewStateListener(viewState: Int) : Runnable? {
            return stateViewListeners[viewState]
        }

        fun getContext() : Context {
            return context
        }
    }
}