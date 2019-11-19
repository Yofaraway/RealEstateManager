package com.openclassrooms.realestatemanager.binding

import androidx.databinding.BindingAdapter


object Bindings {


    @BindingAdapter(value = ["errorText", "rule", "enableError"], requireAll = true)
    @JvmStatic
    fun enableErrorMessage(textInputLayout: com.google.android.material.textfield.TextInputLayout, errorMsg: String, empty: Boolean, init: Boolean){
        textInputLayout.error = null
        if (empty) textInputLayout.error = errorMsg
        textInputLayout.isErrorEnabled = init
    }




}

