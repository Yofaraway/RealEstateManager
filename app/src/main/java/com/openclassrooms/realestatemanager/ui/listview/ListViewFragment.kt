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
import com.openclassrooms.realestatemanager.ui.filter.FilterFragment2
import kotlinx.android.synthetic.main.list_view_fragment.*

class ListViewFragment : Fragment() {

    // Ui
    private val layoutWhenEmpty: RelativeLayout? = layout_empty

    // List
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setToolBar()
        val rootView = inflater.inflate(R.layout.list_view_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.list_view_rv)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()
        addBtnListener()
    }

    private fun addBtnListener() {
        val addEstateFab: FloatingActionButton = list_add_estate_fab
        addEstateFab.setOnClickListener {
            (activity as MainActivity).setFragment(AddEstateFragment.newInstance())
        }
    }


    private fun configureViewModel() {
        val estatesViewModel: EstatesViewModel =
            ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)
        estatesViewModel.getEstates()
            .observe(viewLifecycleOwner, Observer<List<Estate>> {
                this.onListReceived(it)
                if (it.isNotEmpty()) layoutWhenEmpty?.visibility = View.GONE
                else layoutWhenEmpty?.visibility = View.VISIBLE
            })

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_toolbar_filter -> (activity as MainActivity).setFragment(FilterFragment2.newInstance())
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
        adapter = ListAdapter(context!!, estates, { id -> onEstateClick(id!!) })
        recyclerView.adapter = adapter
    }


    private fun onEstateClick(id: Long) {
        (activity as MainActivity).setFragment(DetailsFragment.newInstance(id))
    }

    companion object {
        fun newInstance() = ListViewFragment()
    }


}
