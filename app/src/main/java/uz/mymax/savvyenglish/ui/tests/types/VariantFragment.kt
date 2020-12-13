package uz.mymax.savvyenglish.ui.tests.types

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_variant.*
import kotlinx.android.synthetic.main.layout_bottom_update_delete.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.ui.tests.TestEvent
import uz.mymax.savvyenglish.ui.tests.TestViewModel
import uz.mymax.savvyenglish.ui.tests.TestsFragmentDirections
import uz.mymax.savvyenglish.ui.tests.adapter.VariantAdapter
import uz.mymax.savvyenglish.ui.tests.admin.AddTestDialog
import uz.mymax.savvyenglish.ui.tests.admin.DialogEvent
import uz.mymax.savvyenglish.utils.*


class VariantFragment : Fragment() {

    private val viewModel: TestViewModel by viewModel()
    private lateinit var adapter: VariantAdapter
    private lateinit var bottomSheet: BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = VariantAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_variant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        variantRecycler.adapter = adapter

        adapter.itemClickListener = {
            val action = TestsFragmentDirections.toQeustionSet()
            action.testId = it.toString()

            findNavController().navigate(
                action
            )
        }
        adapter.onLongClickListener = { test ->
            bottomSheet = createBottomSheet(R.layout.layout_bottom_update_delete)
            bottomSheet.show()

            bottomSheet.deleteButton.setOnClickListener {
                viewModel.setStateTest(TestEvent.DeleteTest(test.id.toString()))
                bottomSheet.dismiss()
            }

            bottomSheet.updateButton.setOnClickListener {
                val fm = childFragmentManager
                val orderDialogFragment =
                    AddTestDialog.newInstance(DialogEvent.UpdateTest(test))
                orderDialogFragment.show(fm, "addTheme")
                orderDialogFragment.addCLick = {
                    viewModel.setStateTest(TestEvent.GetAllTestsOfDTM)
                }
                bottomSheet.dismiss()
            }
        }

        if (!isAdmin()) {
            fabAddTest.gone()
            fabAddTest.setOnClickListener {
                val fm = childFragmentManager
                val testDialog =
                    AddTestDialog.newInstance(DialogEvent.CreateTest)
                testDialog.show(fm, "addTheme")
                testDialog.addCLick = {
                    viewModel.setStateTest(TestEvent.GetAllTestsOfDTM)
                }
            }
        }
        connectObservers()

    }

    private fun connectObservers() {
        viewModel.variantTestState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                    changeUiStateVisibility(true, progressIndicator, variantRecycler)
                }
                is NetworkState.Success -> {
                    changeUiStateVisibility(false, progressIndicator, variantRecycler)
                    adapter.updateList(resource.data)
                }
                is NetworkState.Error -> {
                    changeUiStateVisibility(false, progressIndicator, variantRecycler)
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    changeUiStateVisibility(false, progressIndicator, variantRecycler)
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })

        viewModel.deleteTestState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                }
                is NetworkState.Success -> {
                    viewModel.setStateTest(TestEvent.GetAllTestsOfDTM)
                }
                is NetworkState.Error -> {
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })
    }

    override fun onResume() {
        if (adapter.itemCount == 0)
            viewModel.setStateTest(TestEvent.GetAllTestsOfDTM)
        super.onResume()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VariantFragment()
    }
}