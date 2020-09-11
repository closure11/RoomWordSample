package com.wmadao11.roomwordsample.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.wmadao11.roomwordsample.data.Word
import com.wmadao11.roomwordsample.data.WordRepository
import com.wmadao11.roomwordsample.data.WordRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<Word>>

    init {
        val wordDao = WordRoomDatabase.getInstance(application).wordDao()
        repository = WordRepository(wordDao)
        allWords = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWord(word)
        }
    }
}