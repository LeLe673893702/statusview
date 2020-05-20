package com.newler.statusview.wrapactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.newler.state.StateManager
import com.newler.statusview.BaseActivity
import com.newler.statusview.R
import kotlinx.android.synthetic.main.activity_view_pager.*
import kotlinx.android.synthetic.main.fragment_state.view.*

class ViewPagerActivity : BaseActivity() {
    private val tabTitles by lazy {
        mutableListOf<String>("1", "2", "3", "4", "5")
    }

    private val holder by lazy {
        StateManager.instance.wrap(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        holder?.showEmpty()
        holder?.showContent()
        vpState.adapter = TabPagAdapter(supportFragmentManager)

        tlState.setupWithViewPager(vpState)
    }


    inner class TabPagAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = StateFragment.newInstance()

        override fun getCount(): Int  = tabTitles.size

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }

    }

}
