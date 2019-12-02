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
import com.openclassrooms.realestatemanager.utils.TAG_FILTER_FRAGMENT
import com.openclassrooms.realestatemanager.utils.TAG_LIST_VIEW_FRAGMENT
import com.openclassrooms.realestatemanager.utils.TAG_MAP_FRAGMENT
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
                    setFragment(MapFragment.newInstance(), false, TAG_MAP_FRAGMENT)
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
            R.id.menu_toolbar_filter -> setFragment(
                FilterFragment.newInstance(),
                true,
                TAG_FILTER_FRAGMENT
            )
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
        setFragment(ListViewFragment.newInstance(), false, TAG_LIST_VIEW_FRAGMENT)
    }

    fun setFragment(fragment: Fragment, addBackStack: Boolean, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, fragment, tag)
        if (addBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

    // Used with the fragment to rotate photo after the camera intent
    fun setFragmentOnTopOfView(fragment: Fragment, addBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction().add(R.id.main_frame, fragment)
        if (addBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

    fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().remove(fragment).commit()
        supportFragmentManager.popBackStack()
    }

    fun hideBottomNavigation(boolean: Boolean) {
        if (boolean) bottom_navigation.visibility = View.GONE
        else bottom_navigation.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}
