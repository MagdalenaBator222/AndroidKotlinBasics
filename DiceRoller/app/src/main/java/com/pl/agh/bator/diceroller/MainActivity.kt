package com.pl.agh.bator.diceroller

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView

@SuppressLint("StaticFieldLeak")
lateinit var optionsRow1 : RadioGroup
@SuppressLint("StaticFieldLeak")
lateinit var optionsRow2 : RadioGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton: Button = findViewById(R.id.button)
        rollButton.setOnClickListener { rollDice() }

        optionsRow1 = findViewById(R.id.dice_options_row1)
        optionsRow2 = findViewById(R.id.dice_options_row2)
        setOnCheckedChangeListener()
    }

    private fun rollDice() {
        val numSides = checkDie()
        if(numSides == 0){
            return
        }

        val dice = Dice(numSides)
        val diceRoll = dice.roll()
        val resultTextView: TextView = findViewById(R.id.textView)
        resultTextView.text = diceRoll.toString()
    }

    private fun setOnCheckedChangeListener(){
        optionsRow1.setOnCheckedChangeListener { _, i ->
            if(i > 0){
                optionsRow2.clearCheck()
                optionsRow1.check(i)
            }
        }
        optionsRow2.setOnCheckedChangeListener { _, i ->
            if(i > 0){
                optionsRow1.clearCheck()
                optionsRow2.check(i)
            }
        }
    }

    private fun checkDie() : Int {
        val checkedId1 : Int = optionsRow1.checkedRadioButtonId
        val checkedId2 : Int = optionsRow2.checkedRadioButtonId

        if((checkedId1 == -1) and (checkedId2 == -1)){
            return 0
        }

        val numSides = when(if(checkedId1 == -1) checkedId2 else checkedId1){
            R.id.d4 -> 4
            R.id.d6 -> 6
            R.id.d8 -> 8
            R.id.d10 -> 10
            R.id.d12 -> 12
            else -> 20
        }
        return numSides
    }
}

class Dice(private val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}