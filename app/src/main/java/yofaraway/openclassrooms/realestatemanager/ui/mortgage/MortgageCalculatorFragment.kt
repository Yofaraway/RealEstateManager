package yofaraway.openclassrooms.realestatemanager.ui.mortgage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.mortgage_calculator_fragment.*
import yofaraway.openclassrooms.realestatemanager.R
import yofaraway.openclassrooms.realestatemanager.databinding.MortgageCalculatorFragmentBinding
import yofaraway.openclassrooms.realestatemanager.ui.MainActivity

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
        getCurrency()
        getSeekBarValue()
    }

    private fun getCurrency() {
        val prefs: SharedPreferences =
            (activity as MainActivity).getSharedPreferences("preferences", Context.MODE_PRIVATE)
        viewModel.currency.value = prefs.getString("pref_currency", null)
    }

    private fun getSeekBarValue() {
        mortgage_years_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                viewModel.years.value = i + 1
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
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
