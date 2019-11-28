package com.openclassrooms.realestatemanager.ui.listview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.list_view_fragment.*

class ListViewFragment : Fragment() {

    // List
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).hideBottomNavigation(false)
        setToolBar()
        val rootView = inflater.inflate(R.layout.list_view_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.list_view_rv)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = ListAdapter(context!!, mutableListOf()) { id -> onEstateClick(id!!) }
        recyclerView.adapter = adapter

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).supportActionBar?.show()

        configureViewModel()
        addBtnListener()
    }

    private fun addBtnListener() {
        val addEstateFab: FloatingActionButton = list_add_estate_fab
        addEstateFab.setOnClickListener {
            (activity as MainActivity).setFragment(AddEstateFragment.newInstance(), true)
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
                true
            )
        }
        super.onOptionsItemSelected(item)
        return true
    }


    private fun setToolBar() {
        // change title
        (activity as MainActivity).supportActionBar?.title =
            context!!.resources.getString(R.string.app_name)
        // disable back button
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun onListReceived(estates: List<Estate>) {
        adapter = ListAdapter(context!!, estates) { id -> onEstateClick(id!!) }
        recyclerView.adapter = adapter
    }


    private fun onEstateClick(id: Long) {
        (activity as MainActivity).setFragment(DetailsFragment.newInstance(id), true)
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
