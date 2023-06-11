package com.pl.agh.bator.tiptime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pl.agh.bator.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    // variable binding will be used across multiple methods within the MainActivity class,
    // so it has to be initialised in this way:
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialise the binding object
        binding = ActivityMainBinding.inflate(layoutInflater)

        // set the content view of the activity
        // specifies the root of the view hierarchy - the root connects to all children
        // and parents
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener{ calculateTip()}
    }

    private fun calculateTip(){
        val stringInTextField = binding.costOfService.text.toString()
        val cost = stringInTextField.toDoubleOrNull()
        if (cost == null) {
            binding.tipResult.text = ""
            return
        }

        val tipPercentage = when(binding.tipOptions.checkedRadioButtonId){
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        var tip = cost * tipPercentage

        if(binding.roundUpSwitch.isChecked){
            tip = kotlin.math.ceil(tip)
        }

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }
}