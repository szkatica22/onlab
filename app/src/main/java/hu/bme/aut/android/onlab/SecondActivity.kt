package hu.bme.aut.android.onlab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import hu.bme.aut.android.onlab.databinding.ActivitySecondBinding
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth

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
                R.id.nav_recipies, R.id.nav_favourites, R.id.nav_shares, R.id.nav_shopping_list, R.id.nav_settings, R.id.nav_logout
            ), drawerLayout
        )



        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
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
            R.id.nav_shares -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_global_nav_shares)
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