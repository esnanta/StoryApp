package com.esnanta.storyapp.utils.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.esnanta.storyapp.R
import com.google.android.material.textfield.TextInputLayout

class EmailText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    init {
        val editText = AppCompatEditText(context)
        editText.id = R.id.emailEditText // Ensure the ID matches the one in the layout
        editText.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        addView(editText, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateEmail() {
        val email = editText?.text.toString()
        if (email.isEmpty()) {
            error = context.getString(R.string.error_email_empty)
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = context.getString(R.string.error_invalid_email)
        } else {
            error = null
        }
    }
}