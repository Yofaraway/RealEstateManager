package com.openclassrooms.realestatemanager.ui.listview

import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.ui.EstatesViewModel
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.addestate.AddEstateFragment
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment
import com.openclassrooms.realestatemanager.ui.filter.FilterFragment
import com.openclassrooms.realestatemanager.utils.TAG_ADD_ESTATE_FRAGMENT
import com.openclassrooms.realestatemanager.utils.TAG_DETAILS_FRAGMENT
import com.openclassrooms.realestatemanager.utils.TAG_FILTER_FRAGMENT
import kotlinx.android.synthetic.main.list_view_fragment.*


class ListViewFragment : Fragment() {

    // List
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val rootView = inflater.inflate(R.layout.list_view_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.list_view_rv)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = ListAdapter(context!!, mutableListOf()) { id -> onEstateClick(id!!) }
        recyclerView.adapter = adapter

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        (activity as MainActivity).supportActionBar?.show()
        menu.clear()
        inflater.inflate(R.menu.menu_main, menu)
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.app_name)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()
        addBtnListener()

    }

    private fun addBtnListener() {
        val addEstateFab: FloatingActionButton = list_add_estate_fab
        addEstateFab.setOnClickListener {
            (activity as MainActivity).setFragment(
                AddEstateFragment.newInstance(),
                true,
                TAG_ADD_ESTATE_FRAGMENT
            )
        }
    }


    private fun configureViewModel() {
        val layoutWhenEmpty: RelativeLayout = layout_empty
        val estatesViewModel: EstatesViewModel =
            ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)

        val args = arguments
        val isFilterOn = args?.getBoolean(KEY_FILTER)

        if (isFilterOn == null) estatesViewModel.getEstates()
            .observe(viewLifecycleOwner, Observer {
                if (!it.isNullOrEmpty()) {
                    layoutWhenEmpty.visibility = View.GONE
                    this.onListReceived(it)
                } else layoutWhenEmpty.visibility = View.VISIBLE
            })
        else estatesViewModel.estatesFiltered.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                layoutWhenEmpty.visibility = View.GONE
                this.onListReceived(it)
            } else layoutWhenEmpty.visibility = View.VISIBLE
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_toolbar_filter -> (activity as MainActivity).setFragment(
                FilterFragment.newInstance(),
                true, TAG_FILTER_FRAGMENT
            )
        }
        return  super.onOptionsItemSelected(item)
    }


    private fun onListReceived(estates: List<Estate>) {
        adapter = ListAdapter(context!!, estates) { id -> onEstateClick(id!!) }
        recyclerView.adapter = adapter
        // if tablet & list not empty, display first item details
        if (resources.getBoolean(R.bool.twoPaneMode) && estates.isNotEmpty()) onEstateClick(1)
    }


    private fun onEstateClick(id: Long) {
        (activity as MainActivity).setFragment(
            DetailsFragment.newInstance(id),
            true,
            TAG_DETAILS_FRAGMENT
        )
    }


    companion object {
        fun newInstance() = ListViewFragment()

        fun filteredInstance(): ListViewFragment {
            val fragment = ListViewFragment()
            val args = Bundle()
            args.putBoolean(KEY_FILTER, true)
            fragment.arguments = args
            return fragment
        }

        const val KEY_FILTER = "KEY_FILTER"

    }
}
