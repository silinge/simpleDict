package com.xvwilliam.simpledict

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.xvwilliam.simpledict.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dictionary: Dictionary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            dictionary = Dictionary(applicationContext)
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to initialize dictionary", e)
            return
        }

        binding.searchButton.setOnClickListener {
            val searchText = binding.searchInputEditText.text.toString().trim()
            if (searchText.isNotEmpty()) {
                searchWord(searchText)
            } else {
                binding.phoneticTextView.text = ""
                binding.definitionTextView.text = getString(R.string.empty_search_text)
                binding.translationTextView.text = getString(R.string.empty_search_text)
            }
        }

        binding.searchInputEditText.hint = getString(R.string.search_hint)
        binding.phoneticTextView.text = ""
        binding.definitionTextView.text = ""
        binding.translationTextView.text = ""

        setSupportActionBar(binding.toolbar) // 将 Toolbar 设置为 ActionBar
    }

    private fun searchWord(word: String) {
        val wordEntry = dictionary.search(word)
        if (wordEntry != null) {
            binding.phoneticTextView.text = wordEntry.phonetic ?: getString(R.string.no_phonetic)
            binding.definitionTextView.text = wordEntry.definition ?: getString(R.string.no_definition)
            binding.translationTextView.text = wordEntry.translation ?: getString(R.string.no_translation)
        } else {
            binding.phoneticTextView.text = ""
            binding.definitionTextView.text = getString(R.string.not_found)
            binding.translationTextView.text = getString(R.string.not_found)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // ... (其他代码)
}