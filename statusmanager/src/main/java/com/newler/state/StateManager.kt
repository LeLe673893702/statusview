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
        fun getView(holder: Holder, viewState: Int): StateView
    }

    fun initAdapter(adapter: Adapter) {
        this.adapter = adapter
    }

    fun wrap(activity : Activity) : Holder? {
        adapter?.let {
            return Holder(it, activity.applicationContext, activity.findViewById(android.R.id.content))
        }
        return null
    }

    fun wrap(view : View) : Holder? {
        val parent = view.parent
        parent?.let {
            if (it is RelativeLayout || it is ConstraintLayout) {
                return cover(view, it)
            } else {
                return warp(view, it)
            }
        }

        return null
    }

    /**
     * 如果RelativeLayout或ConstraintLayout平级覆盖
     */
    private fun cover(view : View, parent:ViewParent) :Holder? {
        adapter?.let {
            return Holder(it, view.context.applicationContext, parent as ViewGroup)
        }
        return null
    }

    /**
     * 如果是其他布局，FrameLayout将contentView和stateView全部包裹进来
     */
    private fun warp(view : View, parent:ViewParent) :Holder? {
        adapter?.let {
            val wrapper = FrameLayout(view.context)
            val lp =  view.layoutParams
            lp?.let {
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            }

            // 先移除已经添加原来布局的contentView
            if (parent is ViewGroup) {
                val index = parent.indexOfChild(view)
                parent.removeView(view)
                parent.addView(wrapper, index)
                parent.addView(view, FrameLayout.LayoutParams(-1, -1))
                return Holder(it, view.context.applicationContext, wrapper)
            }
        }

        return null
    }

    class Holder(private val adapter: Adapter, private val context: Context,
                 private val wrapper: ViewGroup) {
        private var currentState : Int = ViewState.CONTENT
        private val stateViewListeners: SparseArray<Runnable> by lazy {
            SparseArray<Runnable>(1)
        }
        private val stateViews : SparseArray<StateView> by lazy {
            SparseArray<StateView>(4)
        }

        fun showState(viewState : Int) {
            if (currentState == viewState) {
                return
            }

            currentState = viewState

            try {
                // 页面中包含该状态view
                if (wrapper.indexOfChild(stateViews[viewState]?.getView()) >= 0) {
                    stateViews.forEach { key, stateView ->
                        stateView.getView().visibility = if (key == viewState) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                    return
                }

                // 如果不包含该view从缓存中取，如果没有获取新的view
                val stateView = stateViews[viewState] ?: adapter.getView(this, viewState)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    stateView.getView().elevation = Float.MAX_VALUE
                }

                // 添加到页面中
                addStateView(stateView.getView(), wrapper)

                stateViews.put(viewState, stateView)
                stateView.showState(viewState)
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
        fun getStateView(viewState: Int) : StateView {
            if (stateViews[viewState] == null) {
                val view = stateViews[viewState]
                stateViews.put(viewState, view)
                return view
            }

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