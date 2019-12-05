package com.openclassrooms.realestatemanager.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    val prefs by lazy { context!!.getSharedPreferences("preferences", Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).hideBottomNavigation(true)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.apply {
            // back button
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)

            // title
            title =
                context!!.resources.getString(R.string.settings_title)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getCurrentCurrency()
        onCurrencySelectedListener()
    }

    private fun getCurrentCurrency() {
        val currency = prefs.getString("pref_currency", null)
        val isDollar: Boolean =
            (currency!! == context!!.resources.getString(R.string.currency_dollar))
        settings_dollar_btn.isChecked = isDollar
        settings_euro_btn.isChecked = !isDollar
    }

    private fun onCurrencySelectedListener() {
        settings_dollar_btn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                prefs.edit().putString("pref_currency", "Dollar").apply()

            } else prefs.edit().putString("pref_currency", "Euro").apply()

            // Snackbar
            var str: String = context!!.resources.getString(R.string.settings_currency_changed)
            val currency = prefs.getString("pref_currency", null)
            str = "$str $currency"
            Snackbar.make(view!!, str, Snackbar.LENGTH_SHORT).show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                fragmentManager?.popBackStack()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }


    companion object {
        fun newInstance() = SettingsFragment()
    }
}
