package com.dev.frequenc.ui_codes.extensions

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData


fun EditText.textChanged(liveData: MutableLiveData<String>) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            // Leaving it blank for now
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Leaving it blank for now
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            liveData.value = s.toString()
        }
    })
}
fun View.showAlertDialog(title: String, message: String, positiveButtonText: String, negativeButtonText: String,
                         positiveButtonClick: () -> Unit, negativeButtonClick: () -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(positiveButtonText) { _, _ -> positiveButtonClick() }
    builder.setNegativeButton(negativeButtonText) { _, _ -> negativeButtonClick() }
    val dialog = builder.create()
    dialog.show()
}

object KeyboardUtils {
    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        activity.currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
