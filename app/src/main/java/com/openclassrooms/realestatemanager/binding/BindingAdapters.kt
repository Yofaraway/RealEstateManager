package com.openclassrooms.realestatemanager.binding

import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.Utils
import java.text.SimpleDateFormat
import java.util.*


object BindingAdapters {

    // TO SHOW ERROR MESSAGE AFTER CLICK WHEN TEXT IS MISSING IN AN EDIT TEXT
    @BindingAdapter(value = ["errorText", "rule", "enableError"], requireAll = true)
    @JvmStatic
    fun enableErrorMessage(
        textInputLayout: TextInputLayout,
        errorMsg: String,
        empty: Boolean,
        init: Boolean
    ) {
        textInputLayout.error = null
        if (empty) textInputLayout.error = errorMsg
        textInputLayout.isErrorEnabled = init
    }


    // FORMAT PRICE FOR TEXTVIEW (from "1000" to "$1,000")
    @BindingAdapter(value = ["formatPrice", "currency"], requireAll = true)
    @JvmStatic
    fun getFormattedPrice(tv: TextView, price: Int, currency: String) {
       tv.text = Utils.formatPrice(price, currency)
    }

    // FORMAT SURFACE FOR TEXTVIEW (from "1000" to "1,000 m²")
    @BindingAdapter(value = ["formatSurface"])
    @JvmStatic
    fun getFormattedSurface(tv: TextView, surface: Int) {
        tv.text = Utils.formatSurface(surface)
    }


    // FORMAT DATE FOR EDITTEXT
    @BindingAdapter(value = ["formatDate"])
    @JvmStatic
    fun getFormattedDate(textInput: TextInputLayout, date: Date?) {
        if (date != null) {
            val cal = Calendar.getInstance()
            cal.time = date
            textInput.editText!!.setText(Utils.dateToString(cal))
            Utils.dateToString(cal)
        } else {
            textInput.editText!!.setText("")
        }
    }

    // FORMAT DATE FOR TEXTVIEW
    @BindingAdapter(value = ["formatDateLiteral"])
    @JvmStatic
    fun getFormattedDateLiteral(textView: TextView, date: Date?) {
        if (date != null) {
            val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            textView.text = formatter.format(date)
        } else {
            textView.text = ""
        }
    }

    // CHECK AND UNCHECK BEHAVIOUR OF CHECKBOXES : SOLD (PARENT) / AFTER (CHILD1) / BEFORE (CHILD2)
    @BindingAdapter(value = ["checkChild1", "checkChild2"], requireAll = true)
    @JvmStatic
    fun checkAndUncheckParent(
        checkParent: CheckBox,
        checkChild1: MutableLiveData<Boolean>,
        checkChild2: MutableLiveData<Boolean>
    ) {
        // Parent is checked if one his child is checked
        checkParent.isChecked = (checkChild1.value!! || checkChild2.value!!)

        checkParent.setOnCheckedChangeListener { _, isChecked ->
            // If parent is unchecked, both child are unchecked
            if (!isChecked) {
                checkChild1.value = false
                checkChild2.value = false
            }
            // If parent is checked, child1 is checked
            else if (!checkChild2.value!! && !checkChild1.value!!) checkChild1.value = true

        }
    }

    // FORMAT DATE FOR EDITVIEW
    @BindingAdapter(value = ["emptyIfUnchecked"])
    @JvmStatic
    fun checkedIfNotEmpty(editText: TextInputEditText, checked: MutableLiveData<Boolean>) {
        checked.value = (editText.text.toString() != "")
    }


    // CURRENCY
    @BindingAdapter(value = ["currency"])
    @JvmStatic
    fun adaptCurrencyIcon(textInputLayout: TextInputLayout, currency: String) {
        when (currency) {
            "Dollar" -> textInputLayout.setStartIconDrawable(R.drawable.ic_attach_money_black_24dp)
            "Euro" -> textInputLayout.setStartIconDrawable(R.drawable.ic_euro_symbol_black_24dp)
            else -> textInputLayout.setStartIconDrawable(R.drawable.ic_attach_money_black_24dp)
        }
    }


}

