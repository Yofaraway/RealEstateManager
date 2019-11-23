package com.openclassrooms.realestatemanager.ui.filter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.filter2_fragment.*


class FilterFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.filter2_fragment, container, false)

        return rootView

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setNearButtons()
    }

    private fun setNearButtons() {
        val centerBTn: Button? = filter_near_center
        val buttons = mutableSetOf(
            centerBTn
        )
        for (button in buttons) {
            button?.setOnClickListener{btToggleClick(button)}
        }
    }

    private fun btToggleClick(b: Button) {
        if (b.isSelected) {
            b.setTextColor(Color.parseColor("#666666"))
        } else {
            b.setTextColor(Color.WHITE)
        }
        b.isSelected = !b.isSelected

    }

    companion object {
        fun newInstance() = FilterFragment2()
    }

}