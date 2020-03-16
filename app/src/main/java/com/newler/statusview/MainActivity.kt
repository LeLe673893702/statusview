package com.newler.statusview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.newler.statusview.wrapactivity.WrapActivityActivity
import kotlinx.android.synthetic.main.empty_view.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emptyView.show()
    }

    private val emptyView by lazy {
        LayoutInflater.from(this).inflate(R.layout.empty_view, null, false).emptyView
    }

    fun toWrapActivity(view: View?) {
        startActivity(Intent(this, WrapActivityActivity::class.java))
    }

    fun toWrapView(view: View?) {

    }

    fun toWrapFragment(view: View?) {
    }
}
