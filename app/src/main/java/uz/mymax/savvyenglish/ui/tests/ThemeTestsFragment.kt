package uz.mymax.savvyenglish.ui.tests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_theme_tests.*
import kotlinx.android.synthetic.main.layout_bottom_update_delete.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.response.VariantTestResponse
import uz.mymax.savvyenglish.ui.tests.adapter.ThemeTestAdapter
import uz.mymax.savvyenglish.ui.tests.admin.AddTestDialog
import uz.mymax.savvyenglish.ui.tests.admin.DialogEvent
import uz.mymax.savvyenglish.utils.VerticalSpaceItemDecoration
import uz.mymax.savvyenglish.utils.checkViewForAdmin
import uz.mymax.savvyenglish.utils.createBottomSheet
import uz.mymax.savvyenglish.utils.showSnackbar


class ThemeTestsFragment : Fragment() {

    private val args: ThemeTestsFragmentArgs by navArgs()
    private val viewModel: TestViewModel by viewModel()
    private lateinit var adapter: ThemeTestAdapter
    private lateinit var bottomSheet: BottomSheetDialog
    private var clickedId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ThemeTestAdapter()
        viewModel.setStateTheme(ThemeEvent.GetTestsOfTheme(args.themeId))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theme_tests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkViewForAdmin(fabAddTestTheme)
        themeTestRecycler.adapter = adapter
        themeTestRecycler.addItemDecoration(VerticalSpaceItemDecoration(16))
        connectObservers()

        adapter.itemClickListener = {
            if (it.isFree || it.isPaid) {
                val action = ThemeTestsFragmentDirections.toTestSet()
                action.testId = it.id.toString()
                findNavController().navigate(
                    action
                )
            } else {
//                viewModel.checkTopic(false, it.id.toString())
//                clickedId = it.id.toString()
                val fm = childFragmentManager
                val testDialog =
                    AddTestDialog.newInstance(DialogEvent.PayToTest(true, it.id.toString()))
                testDialog.show(fm, "addTheme")
                testDialog.addCLick = {
                    showSnackbar("You can make the payment in Payme!")
                    viewModel.setStateTheme(ThemeEvent.GetAllThemes)
                }

            }

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
                val updateTest = VariantTestResponse()
                updateTest.id = test.id
                updateTest.title = test.title
                updateTest.isFree = test.isFree
                updateTest.price = test.price ?: 0
                updateTest.paymentId = test.paymentId
                updateTest.themeId = args.themeId.toInt()
                val orderDialogFragment =
                    AddTestDialog.newInstance(DialogEvent.UpdateTest(updateTest))
                orderDialogFragment.show(fm, "addTest")
                orderDialogFragment.addCLick = {
                    viewModel.setStateTheme(ThemeEvent.GetTestsOfTheme(args.themeId))
                }
                bottomSheet.dismiss()
            }
        }

        fabAddTestTheme.setOnClickListener {
            val fm = childFragmentManager
            val orderDialogFragment =
                AddTestDialog.newInstance(DialogEvent.CreateThemeTest(args.themeId))
            orderDialogFragment.show(fm, "addTest")
            orderDialogFragment.addCLick = {
                viewModel.setStateTheme(ThemeEvent.GetTestsOfTheme(args.themeId))
            }
        }
    }

    private fun connectObservers() {
        viewModel.testsOfThemeState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                    progressIndicator.show()
                }
                is NetworkState.Success -> {
                    progressIndicator.hide()
                    adapter.updateList(resource.data)
                }
                is NetworkState.Error -> {
                    progressIndicator.hide()
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    progressIndicator.hide()
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })
        viewModel.deleteTestState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                }
                is NetworkState.Success -> {
                    viewModel.setStateTheme(ThemeEvent.GetTestsOfTheme(args.themeId))
                }
                is NetworkState.Error -> {
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })

        viewModel.checkPayState.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is NetworkState.Loading -> {
                    }
                    is NetworkState.Success -> {
                        if (resource.data.contains("true")) {
                            val action = ThemeTestsFragmentDirections.toTestSet()
                            action.testId = clickedId
                            findNavController().navigate(
                                action
                            )
                        } else {
                            val fm = childFragmentManager
                            val testDialog =
                                AddTestDialog.newInstance(DialogEvent.PayToTest(true, clickedId))
                            testDialog.show(fm, "addTheme")
                            testDialog.addCLick = {
                                showSnackbar("You can make the payment in Payme!")
                                viewModel.setStateTheme(ThemeEvent.GetAllThemes)
                            }
                        }
                    }
                    is NetworkState.Error -> {
                        showSnackbar(resource.exception.message.toString())
                    }
                    is NetworkState.GenericError -> {
                        showSnackbar(resource.errorResponse.message)
                    }
                }
            }

        })
    }
}