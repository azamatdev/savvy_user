package uz.mymax.savvyenglish.ui.tests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_tests.*

import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.ui.tests.adapter.TestViewPagerAdapter

/**
 * A simple [Fragment] subclass.
 */
class TestsFragment : Fragment() {


    lateinit var viewPagerAdapter: TestViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPagerAdapter = TestViewPagerAdapter(childFragmentManager, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testViewPager.adapter = viewPagerAdapter

//        val iconList = listOf(
//            R.drawable.ic_home,
//            R.drawable.ic_heart_filled,
//            R.drawable.ic_shopping_bag
//        )

        marketPlaceTabLayout.setupWithViewPager(testViewPager)
//        testViewPager.offscreenPageLimit = iconList.size
//        for (counter in iconList.indices) {
//            marketPlaceTabLayout.getTabAt(counter)?.setIcon(iconList[counter])
//        }
//
//        for (i in 0 until marketPlaceTabLayout.getTabCount()) {
//            val tab: TabLayout.Tab? = marketPlaceTabLayout.getTabAt(i)
//            tab?.setCustomView(R.layout.layout_custom_tab_view)
//        }

    }
}
