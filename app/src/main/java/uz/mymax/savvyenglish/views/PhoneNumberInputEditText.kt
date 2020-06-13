package uz.mymax.savvyenglish.views

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class PhoneNumberInputEditText : TextInputEditText {
    private val MAX_LENGTH = 12
    private val watcher = PhoneNumberTW(this)

    companion object {
        var pos: Int = -1
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        this.addTextChangedListener(watcher)
        this.filters = arrayOf(filter, InputFilter.LengthFilter(MAX_LENGTH))
    }

    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
        this.filters = arrayOf(filter, InputFilter.LengthFilter(MAX_LENGTH))
        this.addTextChangedListener(watcher)
    }


    var filter: InputFilter = object : InputFilter {
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            if (source == null) return null
            if (source.equals(" ")) return ""

            val text = this@PhoneNumberInputEditText.text.toString().replace("[ ]".toRegex(), "")
            val f1 = dstart == 2 || dstart == 6 || dstart == 9
            if (text.length == 9 && f1) return " "
            if (source.length == 1 && f1) {
                pos = dstart + 2
            } else pos = -1

            return null
        }
    }

    private class PhoneNumberTW(edittext: TextInputEditText) : TextWatcher {
        private val edittext: TextInputEditText = edittext
        var previousCleanString: String = ""
        var prevFormattedString: String = ""

        override fun afterTextChanged(e: Editable?) {
            val str = e.toString()
            val cleanString = str.replace("[ ]".toRegex(), "")
            // for prevent afterTextChanged recursive call
            if (cleanString == previousCleanString || cleanString.isEmpty()) {
                return
            }
            previousCleanString = cleanString

            var formattedString: String

            formattedString = formatPhoneNumber(cleanString)
            if (prevFormattedString == formattedString)
                return
            prevFormattedString = formattedString
            var filters = edittext.filters
            edittext.filters = arrayOf<InputFilter>()
            e?.replace(0, e.length, formattedString)
            edittext.filters = filters

            if (pos != -1) edittext.setSelection(pos)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        private fun formatPhoneNumber(input: String): String {
            val res = StringBuilder()
            repeat(input.length) {
                if (it == 2 || it == 5 || it == 7)
                    res.append(" ")
                res.append(input[it])
            }
            return res.toString()
        }
    }
}