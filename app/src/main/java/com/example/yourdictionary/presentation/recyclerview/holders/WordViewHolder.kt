package com.example.yourdictionary.presentation.recyclerview.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yourdictionary.R

class WordViewHolder(val view: View) : RecyclerView.ViewHolder(view)  {
    lateinit var originalText: TextView
    lateinit var translationText: TextView
    lateinit var time: TextView
    init{
        val indicator = view.findViewById<View>(R.id.indicator).tag as String
        if(indicator == "0") {
            originalText  = view.findViewById(R.id.tvVariantFirstUpperLeft)
            translationText  = view.findViewById(R.id.tvVariantFirstLowerLeft)
            time  = view.findViewById(R.id.tv_request_time)
        }
        else{
            originalText  = view.findViewById(R.id.tvVariantSecondUpperLeft)
            translationText  = view.findViewById(R.id.tvVariantSecondLowerLeft)
            time  = view.findViewById(R.id.tv_request_time)
        }
    }
}