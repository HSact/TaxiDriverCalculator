package com.example.taxidrivercalculator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.taxidrivercalculator.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object
    {
        lateinit var botNav: BottomNavigationView

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadSettings()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val classSettingsActivity = SettingsActivity.create()
        val settings = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language: String = settings.getString("My_Lang", "").toString()
        classSettingsActivity.setLocate(language)*/


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        botNav=navView

        //var a = SettingsActivity.create()
        //a.loadLocate()

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



    /*private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language: String = sharedPreferences.getString("My_Lang", "").toString()
        SettingsActivity.setLocate(language)
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_log -> {
                val logIntent = Intent(this, LogActivity::class.java)
                startActivity(logIntent)
                true
            }
            R.id.action_settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)

                true
            }
            R.id.action_about -> {
                val aboutIntent = Intent (this, AboutActivity::class.java)
                startActivity(aboutIntent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();


        return super.onSupportNavigateUp()
    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/

    private fun loadSettings()
    {
        //loadTheme()
        loadLocate()
    }

    private fun setLocate(lang: String) {
        if (lang != "en" && lang != "ru") {
            return
        }
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()

    }

    private fun loadTheme()
    {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val theme: String = sharedPreferences.getString("Theme", "").toString()
        if (theme=="dark")
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            //var a = AppCompatDelegate.getDefaultNightMode()
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun loadLocate() {
        //val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language: String = sharedPreferences.getString("My_Lang", "").toString()
        setLocate(language)
    }

}