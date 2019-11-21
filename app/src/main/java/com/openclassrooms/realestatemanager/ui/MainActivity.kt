package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.Injection.provideViewModelFactory
import com.openclassrooms.realestatemanager.ui.addestate.AddEstateFragment
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureViewModel()
        //set first fragment if the bundle is null
        if (savedInstanceState == null) setFragment(
            ListViewFragment.newInstance()
        )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_estate_toolbar -> setFragment(
                AddEstateFragment.newInstance()
            )
        }
        return true
    }

    private fun configureViewModel(){
        val mViewModelFactory = provideViewModelFactory(this)
        val estatesViewModel:EstatesViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
            EstatesViewModel::class.java
        )
        estatesViewModel.init()
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, fragment)
            .addToBackStack(null).commit()

    }


}
