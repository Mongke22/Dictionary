package com.example.yourdictionary.presentation

import android.app.Application
import androidx.lifecycle.*
import com.example.yourdictionary.data.repository.SharedPreferencesRepositoryImpl
import com.example.yourdictionary.data.repository.WordRepositoryImpl
import com.example.yourdictionary.domain.model.WordInfo
import com.example.yourdictionary.domain.usecase.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WordRepositoryImpl(application)
    private val spRepository = SharedPreferencesRepositoryImpl(application)
    private val getWordInfoListUseCase = GetWordInfoListUseCase(repository)
    private val getRussianWordInfoListUseCase = GetRussianWordInfoListUseCase(repository)
    private val getWordInfoUseCase = GetWordInfoUseCase(repository)
    private val getRussianWordInfoUseCase = GetRussianWordInfoUseCase(repository)
    private val loadDataUseCase = LoadDataUseCase(repository)

    private val _wordInfo = MutableLiveData<WordInfo>()
    val wordInfo: LiveData<WordInfo>
        get() = _wordInfo


    private val _foreign = MutableLiveData<Boolean>()
    val foreign: LiveData<Boolean>
        get() = _foreign

    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded: LiveData<Boolean>
        get() = _dataLoaded

    private val _language = MutableLiveData<String>()
    val language: LiveData<String>
        get() = _language

    init {
        setCurrentStyle(spRepository.getCurrentStyle())
        setCurrentLanguage(spRepository.getCurrentLanguage())
    }

    private var _wordList =
        if (_foreign.value == true) getWordInfoListUseCase() else getRussianWordInfoListUseCase()
    val wordList: LiveData<List<WordInfo>>
        get() = _wordList

    fun getWordInfo(wordString: String){
        viewModelScope.launch {
            var item: WordInfo = if(wordString[0] in 'а'..'я'){
                getRussianWordInfoUseCase(wordString)
            } else{
                getWordInfoUseCase("$wordString-${language.value}")
            }
            _wordInfo.value = item
        }
    }


    fun switchLanguageToRussian() {
        _foreign.value = false
        _wordList = getRussianWordInfoListUseCase()
    }

    fun switchLanguageToForeign() {
        _foreign.value = true
        _wordList = getWordInfoListUseCase()
    }

    fun addWord(language: String, word: String) {
        viewModelScope.launch {
            _dataLoaded.value = loadDataUseCase.loadData(language, word)
        }
    }

    private fun setCurrentStyle(style: String) {
        _foreign.value = style != "from_russian"
    }

    fun setCurrentLanguage(lang: String){
        _language.value = lang
    }
}