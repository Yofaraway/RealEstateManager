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
import com.openclassrooms.realestatemanager.di.Injection
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.ui.EstateViewModel

class ListViewFragment : Fragment() {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: EstateViewModel


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
        viewModelFactory = Injection.provideViewModelFactory(context!!)
        viewModel =
            ViewModelProviders.of(this, this.viewModelFactory).get(EstateViewModel::class.java)
        viewModel.getEstates()
            .observe(viewLifecycleOwner, Observer<List<Estate>> { this.onListReceived(it) })
    }

    private fun setTitle(){
        (activity as AppCompatActivity).supportActionBar?.title = context!!.resources.getString(R.string.app_name)
    }

    private fun onListReceived(estates: List<Estate>) {
        adapter = ListAdapter(estates)
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance() = ListViewFragment()
    }


}
