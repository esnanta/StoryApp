package com.esnanta.storyapp.utils.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.esnanta.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    private lateinit var editText: TextInputEditText

    override fun onFinishInflate() {
        super.onFinishInflate()
        editText = findViewById(R.id.emailEditText)

        // Set the input type directly here
        editText.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        // Add text change listener for email validation
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateEmail() {
        val email = editText.text.toString()
        error = if (email.isEmpty()) {
            context.getString(R.string.error_email_empty)
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            context.getString(R.string.error_invalid_email)
        } else {
            null
        }
    }
}