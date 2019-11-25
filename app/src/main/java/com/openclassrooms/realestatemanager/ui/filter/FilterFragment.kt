package com.openclassrooms.realestatemanager.ui.filter

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FilterFragmentBinding
import com.openclassrooms.realestatemanager.ui.EstatesViewModel
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment
import com.openclassrooms.realestatemanager.utils.MAX_PHOTOS
import com.openclassrooms.realestatemanager.utils.MIN_PHOTOS
import kotlinx.android.synthetic.main.filter_fragment.*
import java.util.*


class FilterFragment : Fragment() {

    // VIEWMODEL & DATA BINDING
    private lateinit var viewDataBinding: FilterFragmentBinding
    private val filterViewModel: FilterViewModel by lazy {
        ViewModelProviders.of(this).get(FilterViewModel::class.java)
    }
    private val estatesViewModel: EstatesViewModel by lazy {
        ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.filter_fragment, container, false)
        // DATA BINDING
        viewDataBinding = FilterFragmentBinding.bind(rootView).apply {
            this.viewmodel = filterViewModel
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    // TOOLBAR

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Change title
        (activity as MainActivity).supportActionBar?.title =
            context!!.resources.getString(R.string.filter_title)
        // Set back button
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onActivityCreated(savedInstanceState)
        setNearButtons()
        filterViewModel.init()
        onPriceChangedListener()
        onSurfaceChangedListener()
        onPhotoChangedListener()
        onAvailabilityChangedListener()
        onSoldChangedListener()
    }

    private fun onPriceChangedListener() {
        // ToDo : get most expensive estate in data
        val max = 100000
        viewDataBinding.filterPriceSeekbar.setMaxValue(max.toFloat())
        viewDataBinding.filterPriceSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            // Check the price box at the first change
            if (!filterViewModel.isPriceChecked.value!! && minValue.toInt() != 0 || maxValue.toInt() != max)
                filterViewModel.isPriceChecked.value = true
            // Updates values
            filterViewModel.priceMin.value = minValue.toInt()
            filterViewModel.priceMax.value = maxValue.toInt()
        }
    }

    private fun onSurfaceChangedListener() {
        // ToDo : get most biggest estate in data
        val max = 3000
        viewDataBinding.filterSurfaceSeekbar.setMaxValue(max.toFloat())
        viewDataBinding.filterSurfaceSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            // Check the surface box at the first change
            if (!filterViewModel.isSurfaceChecked.value!! && minValue.toInt() != 0 || maxValue.toInt() != max)
                filterViewModel.isSurfaceChecked.value = true
            // Updates values
            filterViewModel.surfaceMin.value = minValue.toInt()
            filterViewModel.surfaceMax.value = maxValue.toInt()
        }
    }

    private fun onPhotoChangedListener() {
        val photoBar = viewDataBinding.filterPhotoSeekbar
        photoBar.setMinValue(MIN_PHOTOS.toFloat())
        photoBar.setMaxValue(MAX_PHOTOS.toFloat())
        photoBar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            // Check the photo box at the first change
            if (!filterViewModel.isPhotoChecked.value!! && minValue.toInt() != MIN_PHOTOS || maxValue.toInt() != MAX_PHOTOS)
                filterViewModel.isPhotoChecked.value = true
            // Updates values
            filterViewModel.photoMin.value = minValue.toInt()
            filterViewModel.photoMax.value = maxValue.toInt()
        }
    }

    private fun onAvailabilityChangedListener() {
        val checkboxes = mutableSetOf(
            viewDataBinding.filterAvailableCheck,
            viewDataBinding.filterAvailableFromCheck
        )
        // On both checkboxes clicked :
        for (box in checkboxes) {
            // Checked = open date picker dialog
            box.setOnClickListener {
                if (filterViewModel.isAvailableChecked.value!!)
                    displayDatePickerPopUp(
                        filterViewModel.fromAvailable
                    )
                // Unchecked = remove the date
                else {
                    filterViewModel.fromAvailable.value = null
                }
            }
        }
        // On date field clicked open date picker dialog
        viewDataBinding.filterAvailableToEt.setOnClickListener {
            displayDatePickerPopUp(
                filterViewModel.fromAvailable
            )
        }
    }

    private fun onSoldChangedListener() {
        // On Sold clicked : checked = After date picker / unchecked = remove any date
        viewDataBinding.filterSoldCheck.setOnClickListener {
            if (filterViewModel.isSoldAfterChecked.value!!) displayDatePickerPopUp(filterViewModel.afterSold)
            else {
                filterViewModel.afterSold.value = null
                filterViewModel.beforeSold.value = null
            }
        }
        // On After or his date field clicked : checked = After date picker / unchecked = remove After date
        viewDataBinding.filterSoldAfterCheck.setOnClickListener {
            if (filterViewModel.isSoldAfterChecked.value!!) displayDatePickerPopUp(
                filterViewModel.afterSold
            )
            else filterViewModel.afterSold.value = null
        }

        // On Before or his date field clicked : checked = Before date picker / unchecked = remove Before date
        viewDataBinding.filterSoldBeforeCheck.setOnClickListener {
            if (filterViewModel.isSoldBeforeChecked.value!!) displayDatePickerPopUp(
                filterViewModel.beforeSold
            )
            else filterViewModel.beforeSold.value = null
        }
    }


    private fun setNearButtons() {
        val centerBTn: Button? = filter_near_center
        val buttons = mutableSetOf(
            centerBTn
        )
        for (button in buttons) {
            button?.setOnClickListener { btToggleClick(button) }
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

    private fun displayDatePickerPopUp(
        dateView: MutableLiveData<Date>
    ) {
        val cldr = Calendar.getInstance()
        DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val datePicked = Calendar.getInstance()
                datePicked.set(year, monthOfYear, dayOfMonth)
                dateView.value = datePicked.time
            },
            cldr.get(Calendar.YEAR),
            cldr.get(Calendar.MONTH),
            cldr.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    companion object {
        fun newInstance() = FilterFragment()
    }

}