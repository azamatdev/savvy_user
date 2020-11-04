package uz.mymax.savvyenglish.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.dialog_view.view.*
import uz.mymax.savvyenglish.MainActivity
import uz.mymax.savvyenglish.R

/**
 * Log Extentions
 */

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

fun TextInputEditText.hideErrorIfFilled() {
    this.doOnTextChanged { text, start, before, count ->
        if (this.text.toString().isNotEmpty() and !this.error.isNullOrEmpty()) {
            this.error = null
        }
    }
}

fun EditText.hideErrorIfFilled() {
    if (this.text.toString().isEmpty()) {
        this.error = context.getString(R.string.warning_fill_the_fields)
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

