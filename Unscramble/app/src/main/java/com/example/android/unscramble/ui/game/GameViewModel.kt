package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.*

class GameViewModel : ViewModel() {

    private val _score = MutableLiveData(0)
    val score : LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount : LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()

    val currentScrambledWord: LiveData<Spannable> = _currentScrambledWord.map {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }
    private var wordsList: MutableList<String> = mutableListOf() // list of words used in the game, to avoid repetition
    private lateinit var currentWord: String // current word the player is trying to unscramble

    private fun getNextWord(){
        currentWord = allWordsList.random() // get a random word from the list
        val tempWord = currentWord.toCharArray() // an array has a fixed size, unlike a list!
        tempWord.shuffle() // change the order of letters to create a "scrambled" word

        while(String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle() // keep changing the order of letters if it is equal to the answer
        }

        if(wordsList.contains(currentWord) and wordsList.isNotEmpty()) { // if current word has already been displayed on screen
            getNextWord() // pick a random word again
        } else {
            _currentWordCount.value = (_currentWordCount.value)?.inc() // increase the word count (with null-safety)
            wordsList.add(currentWord) // add current word to the list
            _currentScrambledWord.value = String(tempWord) // current word to unscramble is the one that was picked and shuffled
        }
    }

    fun nextWord() : Boolean{
        return if(_currentWordCount.value!! < MAX_NO_OF_WORDS){
            getNextWord() // gets another word if the maximum number of words is not exceeded
            true
        } else false
    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE) // plus() method ensures null-safety
    }

    fun isUserWordCorrect(playerWord: String) : Boolean {
        if(playerWord.equals(currentWord, true)){
            increaseScore()
            return true
        }
        return false
    }

    /*
    * re-initialises the game data to restart the game
     */
    fun reinitializeData(){
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

    init { // initializer block
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord() // makes sure that the first displayed word is (almost) always different
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed")
    }


}