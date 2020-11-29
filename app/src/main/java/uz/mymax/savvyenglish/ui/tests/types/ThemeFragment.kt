package uz.mymax.savvyenglish.ui.tests.types

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_theme.*
import kotlinx.android.synthetic.main.layout_bottom_update_delete.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.ui.tests.TestViewModel
import uz.mymax.savvyenglish.ui.tests.TestsFragmentDirections
import uz.mymax.savvyenglish.ui.tests.ThemeEvent
import uz.mymax.savvyenglish.ui.tests.adapter.ThemeAdapter
import uz.mymax.savvyenglish.ui.tests.admin.AddTestDialog
import uz.mymax.savvyenglish.ui.tests.admin.DialogEvent
import uz.mymax.savvyenglish.utils.VerticalSpaceItemDecoration
import uz.mymax.savvyenglish.utils.createBottomSheet
import uz.mymax.savvyenglish.utils.showSnackbar

class ThemeFragment : Fragment() {

    private val viewModel: TestViewModel by viewModel()
    private lateinit var adapter: ThemeAdapter
    private lateinit var bottomSheet: BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ThemeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        themeRecycler.adapter = adapter
        themeRecycler.addItemDecoration(VerticalSpaceItemDecoration(16))
        adapter.itemClickListener = {
            findNavController().navigate(TestsFragmentDirections.toThemeTest(it.toString()))
        }

        adapter.onLongClickListener = { theme ->
            bottomSheet = createBottomSheet(R.layout.layout_bottom_update_delete)
            bottomSheet.show()

            bottomSheet.deleteButton.setOnClickListener {
                viewModel.setStateTheme(ThemeEvent.DeleteTheme(theme.id.toString()))
                bottomSheet.dismiss()
            }

            bottomSheet.updateButton.setOnClickListener {
                val fm = childFragmentManager
                val orderDialogFragment =
                    AddTestDialog.newInstance(DialogEvent.UpdateTheme(theme))
                orderDialogFragment.show(fm, "addTheme")
                orderDialogFragment.addCLick = {
                    viewModel.setStateTheme(ThemeEvent.GetAllThemes)
                }
                bottomSheet.dismiss()
            }
        }

        fabAddTheme.setOnClickListener {
            val fm = childFragmentManager
            val orderDialogFragment =
                AddTestDialog.newInstance(DialogEvent.CreateTheme)
            orderDialogFragment.show(fm, "addTheme")
            orderDialogFragment.addCLick = {
                viewModel.setStateTheme(ThemeEvent.GetAllThemes)
            }
        }
        connectObservers()
    }

    fun connectObservers() {
        viewModel.allThemeState.observe(viewLifecycleOwner, Observer { resource ->
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
        viewModel.deleteThemeState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                }
                is NetworkState.Success -> {
                    viewModel.setStateTheme(ThemeEvent.GetAllThemes)
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
            viewModel.setStateTheme(ThemeEvent.GetAllThemes)
        super.onResume()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ThemeFragment()
    }
}