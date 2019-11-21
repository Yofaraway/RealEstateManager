package com.openclassrooms.realestatemanager.ui.listview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.ui.EstatesViewModel
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment

class ListViewFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setTitle()
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

    }

    private fun configureViewModel() {
        val estatesViewModel: EstatesViewModel =
            ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)
        estatesViewModel.getEstates()
            .observe(viewLifecycleOwner, Observer<List<Estate>> { this.onListReceived(it) })



    }

    private fun setTitle() {
        (activity as AppCompatActivity).supportActionBar?.title =
            context!!.resources.getString(R.string.app_name)
    }

    private fun onListReceived(estates: List<Estate>) {
        println(estates.size)
        adapter = ListAdapter(context!!, estates, { id -> onClick(id!!) })
        recyclerView.adapter = adapter
    }

    private fun onClick(id: Long) {
        (activity as MainActivity).setFragment(DetailsFragment.newInstance(id))
    }

    companion object {
        fun newInstance() = ListViewFragment()
    }


}
