package com.xvwilliam.simpledict

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.xvwilliam.simpledict.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dictionary: Dictionary
    private var isLightTheme = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 根据系统设置应用主题
        isLightTheme = resources.getBoolean(R.bool.is_light_status_bar)
        if (isLightTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            dictionary = Dictionary(applicationContext)
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to initialize dictionary", e)
            return
        }

        // 设置状态栏和导航栏样式
        setSystemBarsStyle()

        binding.searchButton.setOnClickListener {
            val searchText = binding.searchInputEditText.text.toString().trim()
            searchWord(searchText)
        }


        val textColor = if (isLightTheme) {
            resources.getColor(R.color.my_text_color_light, theme) // 需要 theme 参数
        } else {
            resources.getColor(R.color.my_text_color_dark, theme)
        }
        binding.searchInputEditText.hint = getString(R.string.search_hint)

        setSupportActionBar(binding.toolbar)
    }

    private fun searchWord(word: String) {
        val wordEntry = dictionary.search(word)
        binding.phoneticTextView.text = wordEntry?.phonetic ?: getString(R.string.no_phonetic)
        binding.definitionTextView.text = wordEntry?.definition ?: getString(R.string.no_definition)
        binding.translationTextView.text = wordEntry?.translation ?: getString(R.string.no_translation)

        if(wordEntry == null && word.isNotEmpty()){
            binding.phoneticTextView.text = ""
            binding.definitionTextView.text = getString(R.string.not_found)
            binding.translationTextView.text = getString(R.string.not_found)
        }else if(word.isEmpty()){
            binding.phoneticTextView.text = ""
            binding.definitionTextView.text = getString(R.string.empty_search_text)
            binding.translationTextView.text = getString(R.string.empty_search_text)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setSystemBarsStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            var systemUiVisibility = decorView.systemUiVisibility
            systemUiVisibility = if (isLightTheme) {
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = systemUiVisibility
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            val decorView = window.decorView
            var systemUiVisibility = decorView.systemUiVisibility
            systemUiVisibility = if (isLightTheme) {
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            decorView.systemUiVisibility = systemUiVisibility
        }
    }
}