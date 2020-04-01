package com.newler.statusview.wrapactivity


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newler.state.StateManager

import com.newler.statusview.R
import kotlinx.android.synthetic.main.fragment_state.view.*

/**
 * A simple [Fragment] subclass.
 */
class StateFragment : Fragment() {
   private val holder by lazy {
        view?.let {
            StateManager.instance.wrap(it.tvState)
        }
    }
    companion object {
        fun newInstance() : StateFragment = StateFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        holder?.showLoadFailed()
        holder?.showContent()
    }

}
