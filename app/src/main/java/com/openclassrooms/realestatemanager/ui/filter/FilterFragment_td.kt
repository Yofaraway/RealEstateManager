package com.openclassrooms.realestatemanager.ui.filter

import androidx.fragment.app.Fragment

class FilterFragment_td : Fragment() {


//    // DATA BINDING & VIEW MODELS
//    private lateinit var viewDataBinding: FilterFragmentBinding
//    private val filterViewModel: FilterViewModel by lazy {
//        ViewModelProviders.of(this).get(FilterViewModel::class.java)
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val rootView = inflater.inflate(R.layout.filter_fragment, container, false)
//
//        // DATA BINDING
//        viewDataBinding = FilterFragmentBinding.bind(rootView).apply {
//            this.viewmodel = filterViewModel
//        }
//        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
//
//        return viewDataBinding.root
//
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        setHasOptionsMenu(true)
//        super.onActivityCreated(savedInstanceState)
//        initViewComponents()
//    }
//
//
//    private fun initViewComponents() {
//        datesCheckboxListeners()
//        datePickedListener()
//    }
//
//    private fun datesCheckboxListeners() {
//        // Available
//        viewDataBinding.filterAvailableCheck.setOnCheckedChangeListener { _, isChecked ->
//            filterViewModel.isAvailableChecked.value = isChecked
//        }
//        // Sold
//        viewDataBinding.filterSoldCheck.setOnCheckedChangeListener { _, isChecked ->
//            filterViewModel.isSoldChecked.value = isChecked
//        }
//    }
//
//    private fun datePickedListener() {
//        val editText = mutableSetOf(
//            viewDataBinding.filterAvailableFromEt,
//            viewDataBinding.filterAvailableToEt,
//            viewDataBinding.filterSoldAfterEt,
//            viewDataBinding.filterSoldBeforeEt
//        )
//        for (editTextClicked in editText) {
//            editTextClicked.setOnClickListener { displayDatePickerPopUp(editTextClicked) }
//        }
//    }
//
//
//    private fun displayDatePickerPopUp(editText: EditText) {
//        val cldr = Calendar.getInstance()
//        val d = cldr.get(Calendar.DAY_OF_MONTH)
//        val m = cldr.get(Calendar.MONTH)
//        val y = cldr.get(Calendar.YEAR)
//
//        // date picker dialog
//        DatePickerDialog(
//            context!!,
//            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
//                val datePicked = ("$dayOfMonth/$monthOfYear/$year")
//                editText.setText(datePicked)
//
//            }, y, m, d
//        ).show()
//    }
//
//
//    // TOOLBAR
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        // change title
//        (activity as MainActivity).supportActionBar?.title =
//            context!!.resources.getString(R.string.filter_title)
//        // set back button
//        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menu_toolbar_filter -> {
//                (activity as MainActivity).setFragment(ListViewFragment.newInstance())
//                return true
//            }
//            android.R.id.home -> {
//                fragmentManager?.popBackStack()
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//        return false
//    }
//
//
//    companion object {
//        fun newInstance() = FilterFragment()
//    }

}
