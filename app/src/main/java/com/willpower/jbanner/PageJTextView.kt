package com.willpower.jbanner

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import com.willpower.widget.entity.IContent
import com.willpower.widget.entity.IImage
import kotlinx.android.synthetic.main.activity_jtextview.*

class PageJTextView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jtextview)
        supportActionBar?.title = "JTextView"
        mTv.appendContent(IContent("JTextView支持设置SpannableString，例如：\n\n").textScale(0.8f).textColor(Color.DKGRAY))
                .appendContent(IContent("红色字体\n").textColor(Color.RED))
                .appendContent(IContent("字体加粗，倾斜\n").style(Typeface.BOLD_ITALIC))
                .appendContent(IContent("带下划线\n").underline())
                .appendContent(IContent("过期\n").strikeThrough())
                .appendImage(IImage(this, R.drawable.icon_040_cover, IImage.imageSnugSize(mTv)))
                .appendContent(IContent("文字开头插入图片\n"))
                .appendContent(IContent("文字上移").upper())
                .appendContent(IContent("---水平线---"))
                .appendContent(IContent("文字下移").lower())
                .appendContent(IContent("字体大小23px").textSize(23))
                .appendContent(IContent("放大1倍").textScale(2f))
                .appendContent(IContent("点击弹出Toast")
                        .clickListener(mTv,object :ClickableSpan(){
                            override fun onClick(widget: View) {
                                Toast.makeText(this@PageJTextView, "点击！", Toast.LENGTH_SHORT).show()
                            }
                }))
    }


}