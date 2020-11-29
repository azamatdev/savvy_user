package uz.mymax.savvyenglish.ui.tests.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.ui.tests.types.ThemeFragment
import uz.mymax.savvyenglish.ui.tests.types.VariantFragment

class TestViewPagerAdapter(fragmentManager: FragmentManager, var context: Context) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var PAGE_NUM = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ThemeFragment.newInstance()
            1 -> VariantFragment.newInstance()
            else -> ThemeFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): String {
        return when (position) {
            0 -> context.resources.getString(R.string.topicRelated)
            1 -> context.resources.getString(R.string.variant)
            else -> context.resources.getString(R.string.topicRelated)
        }
    }

    override fun getCount() = PAGE_NUM
}