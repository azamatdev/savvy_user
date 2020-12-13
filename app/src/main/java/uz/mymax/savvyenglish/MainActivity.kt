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

//        hideBottomNavigationForSearch()
    }
    private lateinit var mainNavController: NavController;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_back)

        setUpBottomNavigation()

        rootContainerView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }


    private fun setUpBottomNavigation() {
        mainNavController = mainNavHostFragment.findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.destTopics,
                R.id.destTests,
                R.id.destExtra,
                R.id.destProfile
            )
        )
        setupActionBarWithNavController(mainNavController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(mainNavController)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        mainNavController.addOnDestinationChangedListener { controller, destination, arguments ->
            //set it here for all the destinations, or inside the switch statement if you want to change it based on destination
            toolbar.title = destination.label
            toolbar.makeVisible()
            bottomNavigationView.makeVisible()
            timeLayout.gone()
            when (destination.id) {
                R.id.destTestFinished,
                R.id.destSignUp,
                R.id.destLogin -> {
                    log("Login ")
                    toolbar.hideVisibility()
                    bottomNavigationView.slideDown()
                }
                R.id.destQuestionSet -> {
                    timeLayout.visible()
                }
                else -> {
                }
            }

            when (destination.id) {
                R.id.destTests -> {
                    toolbar.elevation = 0f
                }
                else -> {
                    toolbar.elevation = 3f
                }
            }
            log("DestinationID: ${destination.label}")

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return mainNavController.navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        rootContainerView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    private fun showBottomNavigationOnlyAtTopLevel() {
        when (mainNavController.currentDestination?.id) {
            R.id.destTopics,
            R.id.destTests,
            R.id.destExtra,
            R.id.destProfile -> bottomNavigationView.slideUp()
            else -> bottomNavigationView.slideDown()
        }
    }

    private fun hideBottomNavigationForSearch() {
        val heightDiff = rootContainerView.rootView.height - rootContainerView.height
        if (heightDiff > this@MainActivity.dpToPx(200f)) { // if more than 200 dp, it's probably a keyboard...
            bottomNavigationView.hideVisibility()
        } else {
            bottomNavigationView.makeVisible()
        }
    }
}
