package com.openclassrooms.realestatemanager.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter

object CalculatorBindingAdapters {


    @BindingAdapter(value = ["amount", "interest"], requireAll = true)
    @JvmStatic
    fun getResult(tv: TextView, amount: String?, interest: String?) {

        val amountDouble: Double = if (!amount.isNullOrEmpty()) amount.toDouble()
        else 0.0
        val interestDouble: Double = if (!interest.isNullOrEmpty()) interest.toDouble()
        else 0.0

        val result = amountDouble * interestDouble
        tv.text = result.toString()
    }


}