package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {
    private val _quantity = MutableLiveData<Int>()
    val quantity : LiveData<Int> = _quantity

    private val _flavour = MutableLiveData<String>()
    val flavour : LiveData<String> = _flavour

    private val _date = MutableLiveData<String>()
    val date : LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    val price : LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it) // currency formatting using a lambda
    }

    val dateOptions = getPickUpOptions()

    init {
        resetOrder()
    }

    // public setter methods accessed from the outside
    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice() // when the quantity is selected, update the price
    }

    fun setFlavour(desiredFlavour: String) {
        _flavour.value = desiredFlavour
    }

    fun hasNoFlavourSet() : Boolean {
        return _flavour.value.isNullOrEmpty() // check if the flavour has been set or not
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    private fun getPickUpOptions() : List<String> {
        val options = mutableListOf<String>()
        // E - day name in week, MMM - month name, d - day number
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance() // current date and time

        // create a list of dates (from current date to 3 following dates)
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1) // adds one day to the calendar
        }
        return options
    }

    fun resetOrder() {
        _quantity.value = 0
        _price.value = 0.0
        _flavour.value = ""
        _date.value = dateOptions[0]
    }

    private fun updatePrice() {
        var calculatedPrice = (quantity.value?: 0) * PRICE_PER_CUPCAKE
        // if the user selected the first option for pickup (today), charge extra
        if (dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }

        _price.value = calculatedPrice
    }
}