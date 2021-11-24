package hu.bme.aut.android.onlab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import hu.bme.aut.android.onlab.databinding.ActivitySecondBinding
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.onlab.ui.favourites.FavouritesFragment
import hu.bme.aut.android.onlab.ui.recipies.RecipiesFragment
import hu.bme.aut.android.onlab.ui.shoppinglist.ShoppinglistFragment

class SecondActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_recipies, R.id.nav_favourites, R.id.nav_shopping_list, R.id.nav_settings, R.id.nav_logout
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_recipies -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_global_nav_recipies)
            }
            R.id.nav_favourites -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_global_nav_favourites)
            }
            R.id.nav_shopping_list -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_global_nav_shopping_list)
            }
            R.id.nav_settings -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_global_nav_settings)
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}


//package hu.bme.aut.android.onlab
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.ui.AppBarConfiguration
//import hu.bme.aut.android.onlab.databinding.ActivitySecondBinding
//import android.os.Bundle
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import androidx.core.view.GravityCompat
//import com.google.android.material.navigation.NavigationView
//import androidx.navigation.findNavController
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import com.google.firebase.auth.FirebaseAuth
//import hu.bme.aut.android.onlab.ui.favourites.FavouritesFragment
//import hu.bme.aut.android.onlab.ui.recipies.RecipiesFragment
//import hu.bme.aut.android.onlab.ui.shoppinglist.ShoppinglistFragment
//
//class SecondActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivitySecondBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivitySecondBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(binding.appBarMain.toolbar)
//
//        startNavigation()
//
//    }
//
//    fun startNavigation(){
////        Log.d("START:", "startnavigation")
//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_recipies, R.id.nav_favourites, R.id.nav_shopping_list, R.id.nav_settings, R.id.nav_logout
//            ), drawerLayout
//        )
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//        navView.setNavigationItemSelectedListener(this)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
////        Log.d("create:", "onCreateOptionsMenu")
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
////        Log.d("support:", "onSupportNavigateUp")
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
////        Log.d("ON:", "onnavigationitemselected")
//////        var last_fragment = R.id.nav_host_fragment_content_main
////        var fragment: Fragment? = null
////        val manager = supportFragmentManager
////        val transaction = manager.beginTransaction()
////        when (item.itemId) {
////            R.id.nav_recipies -> {
////                fragment = RecipiesFragment()
//////                startNavigation()
////            }
////            R.id.nav_favourites -> {
////                fragment = FavouritesFragment()
//////                val tmp = last_fragment
//////                last_fragment = R.id.action_nav_home_to_nav_gallery
//////                findNavController(tmp).navigate(R.id.action_nav_home_to_nav_gallery)
////            }
////            R.id.nav_shopping_list -> {
////                fragment = ShoppinglistFragment()
//////                val tmp = last_fragment
//////                last_fragment = R.id.action_nav_recipies_to_nav_shopping_list
//////                findNavController(tmp).navigate(R.id.action_nav_recipies_to_nav_shopping_list)
////            }
////            R.id.nav_settings -> {
//////                Log.d("NAV:", "heyho settings")
////                fragment = SettingsFragment()
//////                val tmp = last_fragment
//////                last_fragment = R.id.action_nav_recipies_to_nav_settings
//////                findNavController(tmp).navigate(R.id.action_nav_recipies_to_nav_settings)
////            }
////            R.id.nav_logout -> {
////                FirebaseAuth.getInstance().signOut()
////                startActivity(Intent(this, MainActivity::class.java))
////                finish()
////            }
////        }
////        if(fragment != null){
//////            Log.d("NAV:", "fragment not null")
////            transaction.replace(R.id.nav_host_fragment_content_main, fragment)
////        }
//
//        binding.drawerLayout.closeDrawer(GravityCompat.START)
//        return true
//    }
//}
