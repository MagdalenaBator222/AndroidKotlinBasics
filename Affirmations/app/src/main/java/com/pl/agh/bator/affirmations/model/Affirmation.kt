package com.pl.agh.bator.affirmations.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

// annotations ('@' symbol) - added in order to ensure correct types of resource ID
data class Affirmation(
    @StringRes val stringResourceId : Int,
    @DrawableRes val imageResourceId: Int)