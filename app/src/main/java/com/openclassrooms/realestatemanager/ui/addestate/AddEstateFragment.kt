package com.openclassrooms.realestatemanager.ui.addestate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.add_estate_fragment.*


class AddEstateFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.add_estate_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewComponents()
    }

    fun setViewComponents() {
        val typeInput = add_estate_toolbar_type_et


        val confirmBtn = add_the_estate_confirm_btn
        confirmBtn.setOnClickListener {
            val str = typeInput.editText?.text.toString()
            println(str)
        }
    }

    companion object {
        fun newInstance() = AddEstateFragment()
    }

}
