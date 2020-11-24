package uz.mymax.savvyenglish.ui.tests.types

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_variant_test.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.ui.tests.TestViewModel
import uz.mymax.savvyenglish.ui.tests.adapter.VariantTestAdapter
import uz.mymax.savvyenglish.utils.hideLoading
import uz.mymax.savvyenglish.utils.showLoading
import uz.mymax.savvyenglish.utils.showSnackbar


class VariantTestFragment : Fragment() {

    private val viewModel: TestViewModel by viewModel()
    private lateinit var adapter: VariantTestAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = VariantTestAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_variant_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        variantTestRecycler.adapter = adapter
        viewModel.variantTestState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                }
                is NetworkState.Success -> {
                    adapter.updateList(resource.data)
                }
                is NetworkState.Error -> {
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })

        adapter.itemClickListener = {
            findNavController().navigate(
                R.id.destTestSet,
                bundleOf("fromVariant" to true, "setId" to it)
            )
        }
    }

    override fun onResume() {
        if (adapter.itemCount == 0)
            viewModel.fetchVariantTests()
        super.onResume()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VariantTestFragment()
    }
}