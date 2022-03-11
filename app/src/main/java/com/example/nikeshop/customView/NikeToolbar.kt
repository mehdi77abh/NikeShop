package com.example.nikeshop.customView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.example.nikeshop.R
import kotlinx.android.synthetic.main.view_toolbar.view.*

class NikeToolbar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    var onBackBtnClickListener: View.OnClickListener?=null
    set(value) {
        field =value
        backBtn.setOnClickListener(onBackBtnClickListener)
    }
    init {
        inflate(context, R.layout.view_toolbar, this)
        val textView: TextView = findViewById(R.id.toolbarTitleTv)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.NikeToolbar)
            val title = a.getString(R.styleable.NikeToolbar_nt_title)
            if (title != null && title.isNotEmpty())
                textView.text = title

            a.recycle()
        }
    }
}
