package com.esnanta.storyapp.utils.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.esnanta.storyapp.R
import com.google.android.material.textfield.TextInputLayout

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateEmail() {
        val email = text.toString()
        val emailLayout = this.parent.parent as? TextInputLayout
        if (email.isEmpty()) {
            emailLayout?.error = context.getString(R.string.error_email_empty)
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout?.error = context.getString(R.string.error_invalid_email)
        } else {
            emailLayout?.error = null
        }
    }
}