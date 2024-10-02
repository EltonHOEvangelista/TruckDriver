package com.example.truckdriver_v02

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.truckdriver_v02.databinding.ActivityMainBinding
import com.example.truckdriver_v02.ui.dstatus.driver_status

class MainActivity : AppCompatActivity() {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var binding: ActivityMainBinding? = null

    var lbl_SignedIn_Name: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //Button on bottom right corner to set availability
        setSupportActionBar(binding!!.appBarMain.toolbar)
        binding!!.appBarMain.btnChangeStatus.setOnClickListener { //Snackbar.make(view, "", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            //show driver status dialog fragment
            showDrivingStatusFragment()
        }

        val drawer = binding!!.drawerLayout
        val navigationView = binding!!.navView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_profile, R.id.nav_driver_activity
        )
            .setOpenableLayout(drawer)
            .build()
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        setupWithNavController(navigationView, navController)

        //get nav_header_main element
        val headerView = navigationView.getHeaderView(0)
        lbl_SignedIn_Name = headerView.findViewById(R.id.lbl_SignedIn_Name)

        //calling the method to set initial environment.
        setInitialEnvironment()

        //Start service that runs every minute.
        val serviceIntent = Intent(this, serviceUpdate::class.java)
        startService(serviceIntent)
    }

    //Show the fragment to set driving availability
    private fun showDrivingStatusFragment() {
        val driverStatus = driver_status()
        driverStatus.show(supportFragmentManager, "fragment_DriverStatus")
    }

    //method to set initial environment.
    private fun setInitialEnvironment() {
        val extras = intent.extras
        val auxiliar = extras!!.getString("FistName")
        lbl_SignedIn_Name!!.text = auxiliar
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        return (navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }
}