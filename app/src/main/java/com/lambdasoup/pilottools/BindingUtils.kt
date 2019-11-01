package com.lambdasoup.pilottools

import androidx.databinding.BindingConversion

@BindingConversion
fun formatHeading(heading: Float) = "%.0f".format(heading)
