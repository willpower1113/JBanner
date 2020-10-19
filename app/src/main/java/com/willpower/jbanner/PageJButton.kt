package com.willpower.jbanner

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.willpower.widget.entity.IContent
import com.willpower.widget.entity.IImage
import kotlinx.android.synthetic.main.activity_jbutton.*

class PageJButton : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jbutton)
        mTv.appendContent(IContent("JTextView支持设置SpannableString，例如：\n"))
                .appendContent(IContent("红色字体\n").textColor(Color.RED))
                .appendContent(IContent("字体加粗，倾斜\n").style(Typeface.BOLD_ITALIC))
                .appendContent(IContent("带下划线\n").underline())
                .appendContent(IContent("过期\n").strikeThrough())
                .appendImage(IImage(this,R.drawable.icon_040_cover))
                .appendContent(IContent("文字开头插入图片\n"))
    }
}