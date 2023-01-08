package com.example.yourdictionary.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.yourdictionary.R
import com.example.yourdictionary.databinding.FragmentWordDetailBinding
import com.example.yourdictionary.domain.model.DetailInfo
import com.example.yourdictionary.domain.model.WordInfo
import com.example.yourdictionary.presentation.recyclerview.adapters.DetailViewAdapter
import com.example.yourdictionary.presentation.recyclerview.adapters.WordListAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WordDetailFragment : Fragment() {
    private var _binding: FragmentWordDetailBinding? = null
    private val binding: FragmentWordDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentWordDetailBinding == null")
    private lateinit var word: WordInfo
    private lateinit var detailAdapter: DetailViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
    }

    private fun parseParams() {
        val args = requireArguments()
        if (args.containsKey(WORD)) {
            word =
                args.getParcelable<WordInfo>(WORD) ?: throw RuntimeException("Param word is absent")
            showDetailInfo()
        } else if (args.containsKey(WORD_TO_SEARCH)) {
            viewModel.getWordInfo(
                args.getString(WORD_TO_SEARCH)
                    ?: throw RuntimeException("Param word as string is absent")
            )
            viewModel.wordInfo.observe(viewLifecycleOwner){
                word = it
                showDetailInfo()
            }
        }
    }

    private fun showDetailInfo(){
        removeLastSymbols()
        Log.i("wordinfo", word.toString())
        if(word.originalLanguage == "русский"){
            binding.translationToLanguages.visibility = View.VISIBLE
        }
        binding.mainWord.text = String.format(requireContext().resources.getString(R.string.detail_the_word),
            word.text
            )
        binding.language.text = word.originalLanguage
        binding.firstRequest.text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date(word.timeRequest))

        val translations = word.translations.split("/")
        binding.translationToLanguages.text = String.format(requireContext().resources.getString(R.string.russian_detail_translations),
            translations.getOrElse(0){""}.lowercase().dropLast(1),
            translations.getOrElse(1){""}.lowercase(),
            translations.getOrElse(2){""}.lowercase(),
        )
        val detailList = getDetailList()
        setupRecyclerView(detailList)
    }

    private fun getDetailList(): ArrayList<DetailInfo>{
        Log.i("afterErase", word.toString())
        val partsOfSpeech = word.partOfSpeech.split("/")
        val transcriptions = word.transcription.split("/")
        val genders = word.gender.split("/")
        val translations = word.translations.split("/")
        val list = ArrayList<DetailInfo>()
        for(i in partsOfSpeech.indices){
            list.add(DetailInfo(
                language = word.originalLanguage,
                partOfSpeech = partsOfSpeech.getOrElse(i) {""},
                transcription = transcriptions.getOrElse(i){""},
                gender = genders.getOrElse(i){""},
                translation = translations.getOrElse(i){""},
            ))
        }
        return list
    }
    private fun removeLastSymbols(){
        word.partOfSpeech = dropLastSymbol(word.partOfSpeech)
        word.transcription = dropLastSymbol(word.transcription)
        word.translations =  dropLastSymbol(word.translations)
        word.gender = dropLastSymbol(word.gender)
    }

    private fun dropLastSymbol(s: String): String{
        var str = s
        if(str.isEmpty()) return str
        else{
            if(str[str.lastIndex] == '/' || str[str.lastIndex] == '&'){
                str = str.dropLast(1)
            }
        }
        return str
    }

    private fun setupRecyclerView(detailList: ArrayList<DetailInfo>) {
        val rvWordList = binding.rvDetail
        with(rvWordList) {
            detailAdapter = DetailViewAdapter(requireContext(), detailList)
            adapter = detailAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val WORD_TO_SEARCH = "word_to_search"
        private const val WORD = "word"
        private lateinit var viewModel: MainViewModel

        fun newInstance(vm: MainViewModel, word: String): WordDetailFragment {
            viewModel = vm
            return WordDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(WORD_TO_SEARCH, word)
                }
            }
        }

        fun newInstance(vm: MainViewModel, word: WordInfo): WordDetailFragment {
            viewModel = vm
            return WordDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(WORD, word)
                }
            }
        }
    }
}