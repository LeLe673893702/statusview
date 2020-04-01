package com.newler.statusview.wrapactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.newler.statusview.BaseActivity
import com.newler.statusview.R
import kotlinx.android.synthetic.main.activity_view_pager.*

class ViewPagerActivity : BaseActivity() {
    private val tabTitles by lazy {
        mutableListOf<String>("1", "2", "3", "4", "5")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

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
