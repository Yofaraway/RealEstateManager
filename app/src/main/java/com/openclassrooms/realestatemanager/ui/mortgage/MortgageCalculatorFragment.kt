package com.openclassrooms.realestatemanager.ui.mortgage

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.MortgageCalculatorFragmentBinding
import com.openclassrooms.realestatemanager.ui.MainActivity

class MortgageCalculatorFragment : Fragment() {

    // VIEW MODELS & DATA BINDING
    private lateinit var viewDataBinding: MortgageCalculatorFragmentBinding
    private val viewModel: MortgageCalculatorViewModel by lazy {
        ViewModelProviders.of(this).get(MortgageCalculatorViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.mortgage_calculator_fragment, container, false)
        (activity as MainActivity).hideBottomNavigation(true)
        setHasOptionsMenu(true)
        // DATA BINDING
        viewDataBinding = MortgageCalculatorFragmentBinding.bind(rootView).apply {
            this.viewmodel = viewModel
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.apply {
            // back button
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)

            // title
            title =
                context!!.resources.getString(R.string.mortgage_title)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                fragmentManager?.popBackStack()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    companion object {
        fun newInstance() = MortgageCalculatorFragment()
    }

}
