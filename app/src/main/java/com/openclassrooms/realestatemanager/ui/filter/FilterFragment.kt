package com.openclassrooms.realestatemanager.ui.filter

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment
import kotlinx.android.synthetic.main.filter_fragment.*


class FilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.filter_fragment, container, false)

        return rootView

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
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

    // TOOLBAR

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // change title
        (activity as MainActivity).supportActionBar?.title =
            context!!.resources.getString(R.string.filter_title)
        // set back button
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_toolbar_filter -> {
                (activity as MainActivity).setFragment(ListViewFragment.newInstance())
                return true
            }
            android.R.id.home -> {
                fragmentManager?.popBackStack()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    companion object {
        fun newInstance() = FilterFragment()
    }

}