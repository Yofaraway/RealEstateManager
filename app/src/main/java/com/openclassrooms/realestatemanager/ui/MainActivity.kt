package com.openclassrooms.realestatemanager.ui


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.Injection.provideViewModelFactory
import com.openclassrooms.realestatemanager.ui.filter.FilterFragment
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment
import com.openclassrooms.realestatemanager.ui.map.MapFragment
import com.openclassrooms.realestatemanager.ui.settings.SettingsFragment
import com.openclassrooms.realestatemanager.utils.TAG_FILTER_FRAGMENT
import com.openclassrooms.realestatemanager.utils.TAG_LIST_VIEW_FRAGMENT
import com.openclassrooms.realestatemanager.utils.TAG_MAP_FRAGMENT
import com.openclassrooms.realestatemanager.utils.TAG_SETTINGS_FRAGMENT
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //val currency:String?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigation()
        setBottomNavigation()

        configureViewModel()
        getCurrency()
        if (savedInstanceState == null) setFirstFragment()
    }

    /********** UI **********/

    private fun setNavigation() {
        main_navigation.bringToFront()
        main_navigation.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_settings -> {
                    setFragment(
                        SettingsFragment.newInstance(), true,
                        TAG_SETTINGS_FRAGMENT
                    )
                    main_layout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
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

    override fun onSupportNavigateUp(): Boolean {
        if (main_layout.isDrawerOpen(GravityCompat.START)) {
            main_layout.closeDrawer(GravityCompat.START)
        } else {
            main_layout.openDrawer(GravityCompat.START)
        }
        return true
    }

    /********** DATA **********/

    private fun configureViewModel() {
        val mViewModelFactory = provideViewModelFactory(this)
        val estatesViewModel: EstatesViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
            EstatesViewModel::class.java
        )
        estatesViewModel.init()
    }

    private fun getCurrency() {
        val prefs: SharedPreferences =
            this.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val currentCurrency = prefs.getString("pref_currency", null)

        // default currency is dollar
        if (currentCurrency.isNullOrEmpty()) {
            editor.putString("pref_currency", resources.getString(R.string.currency_dollar))
            editor.apply()
        } else println(currentCurrency)

    }

    /********** FRAGMENTS **********/

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

    // For fragments which need to hide bottom nav
    fun hideBottomNavigation(boolean: Boolean) {
        if (boolean) bottom_navigation.visibility = View.GONE
        else bottom_navigation.visibility = View.VISIBLE
    }


}
