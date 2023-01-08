package com.example.yourdictionary.presentation.recyclerview.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yourdictionary.R
import com.example.yourdictionary.domain.model.DetailInfo


class DetailViewAdapter(private val context: Context,
                        private var list: ArrayList<DetailInfo>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = when(viewType){
            RUSSIAN_WORD -> R.layout.details_russian
            FOREIGN_WORD -> R.layout.details_foreign
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]


        if (holder is MyViewHolder) {
            if(model.language.toInt() == RUSSIAN_WORD){
                val pos = holder.itemView.findViewById<TextView>(R.id.position)
                pos.text = String.format(context.resources.getString(R.string.russian_detail_translation),
                    position + 1,
                    model.transcription,
                    model.partOfSpeech
                )
            }else {
                val transcription = holder.itemView.findViewById<TextView>(R.id.transcription)
                val translation = holder.itemView.findViewById<TextView>(R.id.translation)
                val partOfSpeech = holder.itemView.findViewById<TextView>(R.id.part_of_speech)


                val translations = model.translation.dropLast(1).split("&")
                val genders = model.gender.dropLast(1).split("&")
                var translationString = ""
                for(i in translations.indices){
                    translationString += if(model.partOfSpeech == "сущ.") {
                        String.format(
                            context.resources.getString(R.string.foreign_detail_translation_item),
                            translations.getOrElse(i) { "" },
                            genders.getOrElse(i) { "" },
                        )
                    }else{
                        String.format(
                            context.resources.getString(R.string.foreign_detail_translation_item_not_noun),
                            translations.getOrElse(i) { "" }
                        )
                    }
                }
                translation.text = String.format(context.resources.getString(R.string.foreign_detail_translation),translationString)
                partOfSpeech.text = model.partOfSpeech
                transcription.text = String.format(context.resources.getString(R.string.foreign_detail_transcr),  model.transcription)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun String.toInt(): Int{
        return if(this == "русский") {
            RUSSIAN_WORD
        } else {
            FOREIGN_WORD
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return item.language.toInt()
    }
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object{
        const val RUSSIAN_WORD = 0
        const val FOREIGN_WORD = 1
    }
}