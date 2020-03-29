package uz.mymax.savvyenglish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import uz.mymax.savvyenglish.utils.log
import uz.mymax.savvyenglish.utils.setupWithNavController
import uz.mymax.savvyenglish.utils.slideDown
import uz.mymax.savvyenglish.utils.slideUp


class MainActivity : AppCompatActivity() {
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setUpBottomNavigation()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation() {
        val navGraphIds =
            listOf(R.navigation.topic, R.navigation.test, R.navigation.extra, R.navigation.profile)
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds,
            supportFragmentManager,
            R.id.navHostFragment,
            intent
        )
        var appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.topicsFragment,
                R.id.testsFragment,
                R.id.extrasFragment,
                R.id.profileFragment
            )
        )

        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController, appBarConfiguration)
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.id) {
                    R.id.topicsFragment,
                    R.id.testsFragment,
                    R.id.extrasFragment,
                    R.id.profileFragment -> {
                        log("UP")
                        bottomNavigationView.slideUp()
                    }
                    else -> {
                        log("Down")
                        bottomNavigationView.slideDown()
                    }
                }
            }
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
