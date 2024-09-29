package uqac.dim.xtremclicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import uqac.dim.xtremclicker.databinding.ActivityMainBinding
import uqac.dim.xtremclicker.save.Save
import uqac.dim.xtremclicker.sound.SoundsManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var save: Save

    @Inject
    lateinit var soundsManager: SoundsManager

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_clicker, R.id.navigation_store
            )
        )
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
        navView.setupWithNavController(navHostFragment.navController)
        // Hide top action bar
        supportActionBar?.hide()

        save.musicEnabled.observe(this) { musicEnabled ->
            if (musicEnabled) {
                soundsManager.startMusic()
            } else {
                soundsManager.pauseMusic()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (save.musicEnabled.value == true) soundsManager.startMusic()
    }

    override fun onPause() {
        super.onPause()

        soundsManager.pauseMusic()

        save.writeSave()
    }
}