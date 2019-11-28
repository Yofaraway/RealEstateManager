package com.openclassrooms.realestatemanager.ui.filter

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FilterFragmentBinding
import com.openclassrooms.realestatemanager.model.Estate
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

    private lateinit var estates: List<Estate>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.filter_fragment, container, false)
        (activity as MainActivity).hideBottomNavigation(true)
        // DATA BINDING
        viewDataBinding = FilterFragmentBinding.bind(rootView).apply {
            this.viewmodel = filterViewModel
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    // TOOLBAR

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.apply {
            // back button
            setDisplayHomeAsUpEnabled(true)
            // title
            title =
                context!!.resources.getString(R.string.filter_title)
        }
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onActivityCreated(savedInstanceState)
        filterViewModel.init()
        getEstates()
        // Checkboxes listeners
        onPriceChangedListener()
        onSurfaceChangedListener()
        onPhotoChangedListener()
        onAvailabilityChangedListener()
        onSoldChangedListener()
        onNearChangedListener()

        onSearchButtonClicked()
    }


    private fun onPriceChangedListener() {
        // Get highest price in db
        estatesViewModel.getEstates()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
                var maxPrice = list.maxBy { it.priceDollars }?.priceDollars
                if (maxPrice == null) maxPrice = 100000

                viewDataBinding.filterPriceSeekbar.setMaxValue(maxPrice.toFloat())
                viewDataBinding.filterPriceSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
                    // Check the price box at the first change
                    if (!filterViewModel.isPriceChecked.value!! && minValue.toInt() != 0 || maxValue.toInt() != maxPrice)
                        filterViewModel.isPriceChecked.value = true
                    // Updates values
                    filterViewModel.priceMin.value = minValue.toInt()
                    filterViewModel.priceMax.value = maxValue.toInt()
                }
            })
    }

    private fun onSurfaceChangedListener() {
        // Get highest surface in db
        estatesViewModel.getEstates()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
                var maxSurface = list.maxBy { it.surface }?.surface
                if (maxSurface == null) maxSurface = 1000

                viewDataBinding.filterSurfaceSeekbar.setMaxValue(maxSurface.toFloat())
                viewDataBinding.filterSurfaceSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
                    // Check the surface box at the first change
                    if (!filterViewModel.isSurfaceChecked.value!! && minValue.toInt() != 0 || maxValue.toInt() != maxSurface)
                        filterViewModel.isSurfaceChecked.value = true
                    // Updates values
                    filterViewModel.surfaceMin.value = minValue.toInt()
                    filterViewModel.surfaceMax.value = maxValue.toInt()
                }
            })
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
            viewDataBinding.filterAvailableFromCheck,
            viewDataBinding.filterAvailableToEt
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
        // On After (or his date field) clicked : checked = After date picker / unchecked = remove After date
        viewDataBinding.filterSoldAfterCheck.setOnClickListener {
            if (filterViewModel.isSoldAfterChecked.value!!) displayDatePickerPopUp(
                filterViewModel.afterSold
            )
            else filterViewModel.afterSold.value = null
        }
        viewDataBinding.filterSoldAfterEt.setOnClickListener {
            displayDatePickerPopUp(
                filterViewModel.afterSold
            )
        }
        // On Before (or his date field clicked) : checked = Before date picker / unchecked = remove Before date
        viewDataBinding.filterSoldBeforeCheck.setOnClickListener {
            if (filterViewModel.isSoldBeforeChecked.value!!) displayDatePickerPopUp(
                filterViewModel.beforeSold
            )
            else filterViewModel.beforeSold.value = null
        }
        viewDataBinding.filterSoldBeforeEt.setOnClickListener {
            displayDatePickerPopUp(
                filterViewModel.beforeSold
            )
        }
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

    private fun onNearChangedListener() {
        val nearPlaces = mutableSetOf(
            filter_near_center,
            filter_near_cinema,
            filter_near_hospital,
            filter_near_library,
            filter_near_nightlife,
            filter_near_park,
            filter_near_pool,
            filter_near_restaurants,
            filter_near_school,
            filter_near_supermarket,
            filter_near_train
        )
        for (button in nearPlaces) {
            button?.setOnClickListener { onNearButtonClicked(button) }
        }
        viewDataBinding.filterNearCheck.setOnClickListener {
            if (!filterViewModel.isNearChecked.value!!) {
                for (button in nearPlaces) {
                    button.setTextColor(ContextCompat.getColor(context!!, R.color.grey_60))
                    button?.isSelected = false
                    filterViewModel.nearPlaces.value = mutableListOf()
                }
            }
        }
    }

    private fun onNearButtonClicked(b: Button) {
        // When unchecked
        if (b.isSelected) {
            b.setTextColor(ContextCompat.getColor(context!!, R.color.grey_60))
            filterViewModel.nearPlaces.value?.remove(b.text.toString())
            // When checked
        } else {
            b.setTextColor(Color.WHITE)
            filterViewModel.nearPlaces.value?.add(b.text.toString())
        }
        b.isSelected = !b.isSelected
        filterViewModel.isNearChecked.value = !filterViewModel.nearPlaces.value.isNullOrEmpty()
    }


    private fun onSearchButtonClicked() {
        viewDataBinding.filterSearchFab.setOnClickListener {
            // Get list filtered and set it in the main viewModel
            if (filterViewModel.atLeastOneChecked()) {
                estatesViewModel.estatesFiltered.value =
                    filterViewModel.getListFiltered(estates)
                (activity as MainActivity).setFragment(
                    ListViewFragment.filteredInstance(),
                    false
                )

            }
            // If nothing checked
            else Snackbar.make(view!!, R.string.filter_no_filter, Snackbar.LENGTH_SHORT)
                .setAnchorView(viewDataBinding.filterSearchFab)
                .show()
        }
    }

    private fun getEstates() {
        estatesViewModel.getEstates().observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { t -> if (!t.isNullOrEmpty()) estates = t })
    }



    companion object {
        fun newInstance() = FilterFragment()
    }

}