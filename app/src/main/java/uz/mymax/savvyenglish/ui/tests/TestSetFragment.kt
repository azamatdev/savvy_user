package uz.mymax.savvyenglish.ui.tests

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.SnapHelper
import kotlinx.android.synthetic.main.fragment_test_set.*
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.ui.tests.adapter.TestSetAdapter
import uz.mymax.savvyenglish.utils.OnSnapPositionChangeListener
import uz.mymax.savvyenglish.utils.SnapHelperOneByOne
import uz.mymax.savvyenglish.utils.SnapOnScrollListener
import uz.mymax.savvyenglish.utils.attachSnapHelperWithListener
import uz.mymax.savvyenglish.utils.decorations.LinearVerticalDecoration


class TestSetFragment : Fragment() {

    private lateinit var adapter: TestSetAdapter
    private lateinit var snapHelper: SnapHelper
    private var currentItem = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TestSetAdapter()
        snapHelper = SnapHelperOneByOne()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_set, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testSetRecycler.adapter = adapter
        snapHelper.attachToRecyclerView(testSetRecycler)
        testSetRecycler.addItemDecoration(LinearVerticalDecoration())


        testSetRecycler.attachSnapHelperWithListener(snapHelper, SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE, object :
            OnSnapPositionChangeListener {
            override fun onSnapPositionChange(position: Int) {
                Log.d("CurrentItemTag", position.toString())
                currentItem = position
            }
        })


    }
}