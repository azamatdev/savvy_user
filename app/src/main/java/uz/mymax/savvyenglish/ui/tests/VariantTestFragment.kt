package uz.mymax.savvyenglish.ui.tests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.mymax.savvyenglish.R


class VariantTestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_variant_test, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VariantTestFragment()
    }
}