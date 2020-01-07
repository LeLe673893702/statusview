package com.newler.statusview

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.newler.statusview.wrapactivity.WrapActivityActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toWrapActivity(view: View?) {
        startActivity(Intent(this, WrapActivityActivity::class.java))
    }

    fun toWrapView(view: View?) {

    }

    fun toWrapFragment(view: View?) {
    }
}
