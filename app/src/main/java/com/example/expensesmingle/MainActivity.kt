package com.example.expensesmingle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.expensesmingle.databinding.ActivityMainBinding
import com.example.expensesmingle.viewmodel.ExpensesMingleViewModel
import com.google.android.gms.auth.api.identity.Identity

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationHost =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navigationHost.navController
        setSupportActionBar(findViewById(R.id.toolbar))
        val config = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, config)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navigationHost =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navigationHost.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                runBlocking {
                    FirebaseAuth.getInstance().signOut()
                    Identity.getSignInClient(this@MainActivity).signOut()
                }
                Toast.makeText(this,"You have been logged out!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}