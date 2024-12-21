package com.xvwilliam.simpledict

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Locale

class Dictionary(context: Context) {

    private val dictionary: MutableMap<String, WordEntry> = mutableMapOf()

    data class WordEntry(val phonetic: String?, val definition: String?, val translation: String?)

    init {
        try {
            context.assets.open("simpleDict.csv").use { inputStream ->
                InputStreamReader(inputStream, StandardCharsets.UTF_8).use { inputStreamReader ->
                    BufferedReader(inputStreamReader).use { reader ->
                        reader.forEachLine { line ->
                            val parts = parseCSVLine(line)
                            if (parts.size >= 4) {
                                val word = parts[0].trim()
                                val phonetic = parts[1].trim()
                                val definition = parts[2].trim()
                                val translation = parts[3].trim()
                                if (word.isNotEmpty()) {
                                    dictionary[word.lowercase(Locale.getDefault())] = WordEntry(phonetic, definition, translation)
                                } else {
                                    Log.w("Dictionary", "发现空单词行：$line")
                                }
                            } else {
                                if (line.isNotBlank()) {
                                    Log.w("Dictionary", "发现格式错误的行（少于 4 列）：$line")
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("Dictionary", "加载词典文件失败", e)
        }
    }

    fun search(word: String): WordEntry? {
        val cleanedWord = word.trim().lowercase(Locale.getDefault())
        return dictionary[cleanedWord]
    }

    private fun parseCSVLine(line: String): List<String> {
        val fields = mutableListOf<String>()
        var inQuotes = false
        var currentField = StringBuilder()
        var i = 0
        while (i < line.length) {
            when (line[i]) {
                '"' -> {
                    if (inQuotes) {
                        if (i + 1 < line.length && line[i + 1] == '"') {
                            currentField.append('"')
                            i += 1
                        } else {
                            inQuotes = false
                        }
                    } else {
                        inQuotes = true
                    }
                    i += 1
                }
                ',' -> {
                    if (inQuotes) {
                        currentField.append(',')
                    } else {
                        fields.add(currentField.toString())
                        currentField.clear()
                    }
                    i += 1
                }
                else -> {
                    currentField.append(line[i])
                    i += 1
                }
            }
        }
        fields.add(currentField.toString())
        return fields
    }
}