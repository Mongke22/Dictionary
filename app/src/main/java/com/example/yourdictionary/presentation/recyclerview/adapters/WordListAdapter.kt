package com.example.yourdictionary.presentation.recyclerview.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.yourdictionary.R
import com.example.yourdictionary.domain.model.WordInfo
import com.example.yourdictionary.presentation.recyclerview.WordDiffCallBack
import com.example.yourdictionary.presentation.recyclerview.holders.WordViewHolder
import java.text.SimpleDateFormat
import java.util.*

class WordListAdapter : ListAdapter<WordInfo, WordViewHolder>(WordDiffCallBack()) {

    var wordItemOnClickListener: ((WordInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val layout = when (viewType) {
            FROM_RUSSIAN -> R.layout.russian_first_cardview
            TO_RUSSIAN -> R.layout.foreign_first_cardview
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return WordViewHolder(view)
    }


    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = getItem(position)
        holder.originalText.text = word.text.uppercase()
        holder.time.text = SimpleDateFormat("dd/MM/yyyy").format(Date(word.timeRequest ))
        holder.translationText.text = splitTranslations(word.translations)
        holder.view.setOnClickListener{
            wordItemOnClickListener?.invoke(word)
        }
    }

    private fun splitTranslations(translations: String): String{
        var result = ""
        for(words in translations.split("/")){
            for(word in words.split("&")){
                result += "$word/"
                break
            }
        }
        result += "//"
        return result.substringBefore("//")
    }

    private fun String.toInt(): Int {
        return if (this != "русский") {
            TO_RUSSIAN
        } else {
            FROM_RUSSIAN
        }
    }

    override fun getItemViewType(position: Int): Int {
        val word = getItem(position)
        return word.originalLanguage.toInt()
    }

    companion object {
        const val FROM_RUSSIAN = 1
        const val TO_RUSSIAN = 0
        const val POOL_COUNT = 15
    }

}