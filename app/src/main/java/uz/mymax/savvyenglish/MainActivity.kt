package uz.mymax.savvyenglish

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import uz.mymax.savvyenglish.utils.*


class MainActivity : AppCompatActivity() {

    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {

        showBottomNavigationOnlyAtTopLevel()

        hideBottomNavigationForSearch()
    }
    private lateinit var mainNavController : NavController;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mainToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mainToolbar.setNavigationIcon(R.drawable.ic_back)

        setUpBottomNavigation()

        rootContainerView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }


    private fun setUpBottomNavigation() {
        mainNavController = mainNavHostFragment.findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_topics,
                R.id.navigation_tests,
                R.id.navigation_extras,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(mainNavController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(mainNavController)
        mainNavController.addOnDestinationChangedListener { controller, destination, arguments ->
             //set it here for all the destinations, or inside the switch statement if you want to change it based on destination
            when(destination.id){
                R.id.navigation_topics,
                R.id.navigation_tests,
                R.id.navigation_extras,
                R.id.navigation_profile ->toolbar.setNavigationIcon(null)
                else ->toolbar.setNavigationIcon(R.drawable.ic_back)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return mainNavController.navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        rootContainerView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    private fun showBottomNavigationOnlyAtTopLevel(){
        when (mainNavController.currentDestination?.id) {
            R.id.navigation_topics,
            R.id.navigation_tests,
            R.id.navigation_extras,
            R.id.navigation_profile -> bottomNavigationView.slideUp()
            else -> bottomNavigationView.slideDown()
        }
    }

    private fun hideBottomNavigationForSearch(){
        val heightDiff = rootContainerView.rootView.height - rootContainerView.height
        if (heightDiff > this@MainActivity.dpToPx(200f)) { // if more than 200 dp, it's probably a keyboard...
            bottomNavigationView.hide()
        } else {
            bottomNavigationView.show()
        }
    }
}
