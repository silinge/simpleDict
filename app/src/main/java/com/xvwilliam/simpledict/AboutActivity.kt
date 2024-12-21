package com.xvwilliam.simpledict

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.noties.markwon.Markwon

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val textView = findViewById<TextView>(R.id.aboutTextView)
        val markdown = assets.open("about.md").bufferedReader().use { it.readText() }
        Markwon.create(this).setMarkdown(textView, markdown)

    }
}