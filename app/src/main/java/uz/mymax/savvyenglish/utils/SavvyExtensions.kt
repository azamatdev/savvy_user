package uz.mymax.savvyenglish.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.dialog_view.view.*
import org.w3c.dom.Text
import uz.mymax.savvyenglish.BuildConfig
import uz.mymax.savvyenglish.MainActivity
import uz.mymax.savvyenglish.R
import java.util.ArrayList

/**
 * Log Extentions
 */

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}


fun Fragment.log(message: String, tag: String = "DefaultTag") {
    Log.d(tag, message)
}

fun Context.log(message: String, tag: String = "DefaultTag") {
    Log.d(tag, message)
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


/**
 * Animating view with slide animation
 */
fun View.slideDown() {
    this.animate().translationY(500f).alpha(0.0f).setDuration(50).setInterpolator(
        AccelerateDecelerateInterpolator()
    )
    this.hideVisibility()
}

fun View.slideUp() {
    this.makeVisible()
    this.animate().translationY(0f).alpha(1.0f).setDuration(150).setInterpolator(
        AccelerateDecelerateInterpolator()
    )
}

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hideVisibility(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
        Log.d("DefaultTag", "actually hidden")
    }
    return this
}

fun View.makeVisible(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}


/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideSoftKeyboard()
}

fun Fragment.showKeyboard(view: View) {
    activity?.showKeyboard(view)
}

fun Activity.showKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}


/**
 * Toolbar
 */
fun Fragment.initToolbar(toolbar: Toolbar) {
    if (activity is MainActivity) {
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
    }
}

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, snackbarText, timeLength).show()
}


fun Fragment.showBottomDialog(title: String, msg: String) {
    val factory = LayoutInflater.from(context)
    val dialog = factory.inflate(R.layout.dialog_view, null)
    val builder = AlertDialog.Builder(context!!).create()
    //builder.setTitle(title)
    // builder.setMessage(msg)
    builder.setView(dialog)
    dialog.dialog_title.text = msg
    dialog.dialog_ok.setOnClickListener {
        builder.dismiss()
    }

    val back = ColorDrawable(Color.TRANSPARENT)
    val inset = InsetDrawable(back, 28)
    builder.window!!.setBackgroundDrawable(inset)

//    val alertdialog = builder.create()
//    alertdialog.setContentView()
    builder.show()
    builder.getWindow()?.setGravity(Gravity.BOTTOM)
}


fun Context.dpToPx(valueInDp: Float): Float {
    val metrics = resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}

fun EditText.hideErrorIfFilled() {
    this.doOnTextChanged { text, start, before, count ->
        if (this.text.toString().isNotEmpty() and !this.error.isNullOrEmpty()) {
            this.error = null
        }
    }
}


fun TextInputEditText.showErrorIfNotFilled() {
    if (this.text.toString().isEmpty()) {
        this.error = context.getString(R.string.warning_fill_the_fields)
    }
}

fun EditText.showErrorIfNotFilled() {
    if (this.text.toString().isEmpty()) {
        this.error = context.getString(R.string.warning_fill_the_fields)
    }
}

fun Fragment.showSnackbar(message: String) =
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()

fun SwipeRefreshLayout.hideLoading() {
    this.isRefreshing = false
}

fun SwipeRefreshLayout.showLoading() {
    this.isRefreshing = true
}

fun Fragment.setToolbarTitle(title: String) {
    requireActivity().findViewById<Toolbar>(R.id.toolbar).title = title
}

fun Fragment.changeUiStateEnabled(isLoading: Boolean, progressBar: View, viewButton: View) {
    viewButton.isEnabled = !isLoading
    if (isLoading) progressBar.visible() else progressBar.gone()
}

fun Fragment.changeUiStateVisibility(
    isLoading: Boolean,
    progressView: ProgressBar,
    hiddenView: View
) {
    if (isLoading) {
        progressView.visible()
        hiddenView.gone()
    } else {
        progressView.gone()
        hiddenView.visible()
    }
}

fun Context.getDisplayMetrics(): DisplayMetrics {
    val metrics = DisplayMetrics()
    (this as MainActivity).windowManager.defaultDisplay.getMetrics(metrics)
    return metrics
}


fun Fragment.createBottomSheet(layout: Int): BottomSheetDialog {
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    bottomSheetDialog.setContentView(layout)
    val bottomSheetInternal = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
    BottomSheetBehavior.from<View?>(bottomSheetInternal!!).peekHeight =
        (requireContext().getDisplayMetrics().heightPixels * 0.8).toInt()

    return bottomSheetDialog
}


fun ArrayList<TextInputEditText>.showErrorIfNotFilled(textInputLayout: ArrayList<TextInputLayout>) {
    this.forEachIndexed { index, it ->
        if (it.text.toString().isEmpty()) {
            textInputLayout[index].error = it.context.getString(R.string.warning_fill_the_fields)
        }
    }
}

fun TextInputEditText.getStringText() = this.text.toString()

fun ArrayList<TextInputEditText>.hideErrorIfFilled(textInputLayout: ArrayList<TextInputLayout>) {
    this.forEachIndexed { index, it ->
        it.doOnTextChanged { text, start, before, count ->
            if (it.text.toString().isNotEmpty() and !textInputLayout[index].error.isNullOrEmpty()) {
                textInputLayout[index].error = null
            }
        }
    }
}

fun ArrayList<TextInputEditText>.areAllFieldsFilled(): Boolean {
    var filled = false
    for (counter in this.indices)
        if (this[counter].text.toString().isNotEmpty())
            filled = true

    return filled
}

fun Fragment.isAdmin() = BuildConfig.FLAVOR == "Admin"

fun Context.isAdmin() = BuildConfig.FLAVOR == "Admin"

fun Fragment.checkViewForAdmin(view: View) {
    if (isAdmin())
        view.visible()
    else
        view.gone()
}