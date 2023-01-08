package com.example.yourdictionary.presentation.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.example.yourdictionary.domain.model.WordInfo

class WordDiffCallBack: DiffUtil.ItemCallback<WordInfo>() {
    override fun areItemsTheSame(oldItem: WordInfo, newItem: WordInfo): Boolean {
        return oldItem.timeRequest == newItem.timeRequest
    }

    override fun areContentsTheSame(oldItem: WordInfo, newItem: WordInfo): Boolean {
        return oldItem == newItem
    }
}