package com.openclassrooms.realestatemanager.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.openclassrooms.realestatemanager.utils.formatPrice
import kotlin.math.pow
import kotlin.math.roundToInt

object CalculatorBindingAdapters {

    /**

    For reference, the equation of a mortgage payment :

    M = P[r(1+r)^n/((1+r)^n)-1)]

    M = the total monthly mortgage payment
    P = loan amount
    r = monthly interest rate (interest / 12)
    n = years * 12

     */


    @BindingAdapter(value = ["amountT", "interestT", "yearsT", "currencyT"], requireAll = true)
    @JvmStatic
    fun getTotalMortgage(
        tv: TextView,
        amountStr: String?,
        interestStr: String?,
        years: Int,
        currency: String
    ) {


        val amount: Double = if (!amountStr.isNullOrEmpty()) amountStr.toDouble()
        else 0.0
        val interestYear: Double = if (!interestStr.isNullOrEmpty()) interestStr.toDouble()
        else 0.0

        val interest = interestYear / 100 / 12
        val months = years.times(12)
        val result: Int = if (amount == 0.0 || interestYear == 0.0) 0
        else
            ((amount * (interest * (1 + interest).pow(months) / ((1 + interest).pow(months) - 1))) * months).roundToInt()

        tv.text = formatPrice(result,currency )

    }


    @BindingAdapter(value = ["amount", "interest", "years", "currency"], requireAll = true)
    @JvmStatic
    fun getMonthlyPayment(tv: TextView, amountStr: String?, interestStr: String?, years: Int, currency: String) {

        val amount: Double = if (!amountStr.isNullOrEmpty()) amountStr.toDouble()
        else 0.0
        val interestYear: Double = if (!interestStr.isNullOrEmpty()) interestStr.toDouble()
        else 0.0

        val interest = interestYear / 100 / 12
        val months = years.times(12)
        val result: Int = if (amount == 0.0 || interestYear == 0.0) 0
        else
            (amount * (interest * (1 + interest).pow(months) / ((1 + interest).pow(months) - 1))).roundToInt()



        tv.text = formatPrice(result, currency)
    }


}