package com.openclassrooms.realestatemanager.ui


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.Injection.provideViewModelFactory
import com.openclassrooms.realestatemanager.ui.filter.FilterFragment
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment
import com.openclassrooms.realestatemanager.ui.map.MapFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBottomNavigation()
        configureViewModel()
        if (savedInstanceState == null) setFirstFragment()

    }

    private fun setBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.bottom_tab_list -> {
                    setFirstFragment()
                    true
                }
                R.id.bottom_tab_map -> {
                    setFragment(MapFragment.newInstance(), false)
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_toolbar_filter -> setFragment(FilterFragment.newInstance(), true)
        }
        return false
    }

    private fun configureViewModel() {
        val mViewModelFactory = provideViewModelFactory(this)
        val estatesViewModel: EstatesViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
            EstatesViewModel::class.java
        )
        estatesViewModel.init()
    }

    //addToBackStack not included in first fragment
    private fun setFirstFragment() {
        setFragment(ListViewFragment.newInstance(), false)
    }

    fun setFragment(fragment: Fragment, addBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, fragment)
        if (addBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

    fun hideBottomNavigation(boolean: Boolean){
        if (boolean) bottom_navigation.visibility = View.GONE
        else bottom_navigation.visibility = View.VISIBLE
    }

}
