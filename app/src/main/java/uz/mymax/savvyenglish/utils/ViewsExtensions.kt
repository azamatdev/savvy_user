package uz.mymax.savvyenglish.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
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


/**
 * Animating view with slide animation
 */
fun View.slideDown() {
    this.animate().translationY(250f).alpha(0.0f).setDuration(50).setInterpolator(
        AccelerateDecelerateInterpolator()
    )
    this.hide()
}

fun View.slideUp() {
    this.show()
    this.animate().translationY(0f).alpha(1.0f).setDuration(150).setInterpolator(
        AccelerateDecelerateInterpolator()
    )
}

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun View.show(): View {
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