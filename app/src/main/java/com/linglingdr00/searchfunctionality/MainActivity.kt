package com.linglingdr00.searchfunctionality

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.linglingdr00.searchfunctionality.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topToolbar = binding.topToolbar
        setSupportActionBar(topToolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the options menu from XML.
        val inflater = menuInflater
        inflater.inflate(R.menu.app_bar_menu, menu)

        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        // Get the MenuItem for the action item.
        val dateItem = menu.findItem(R.id.date)

        // add search suggestion
        val suggestions = SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)

        // Get the SearchView and set the searchable configuration.
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.apply {
            // Assumes current activity is the searchable activity.
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Don't iconify the widget. Expand it by default.

            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "query text submit: $query")
                    // save search suggestion
                    suggestions.saveRecentQuery(query, null)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "query text change: $newText")
                    return true
                }

            })

        }

        // Configure the search info and add any event listeners.
        // Define the listener.
        val expandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Do something when the action item collapses.
                Log.d(TAG, "search view collapse")
                dateItem.setVisible(false)
                return true // Return true to collapse the action view.
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do something when it expands.
                Log.d(TAG, "search view expand")
                dateItem.setVisible(true)
                return true // Return true to expand the action view.
            }
        }

        // Assign the listener to that action item.
        searchItem.setOnActionExpandListener(expandListener)

        // For anything else you have to do when creating the options menu,
        // do the following:

        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}