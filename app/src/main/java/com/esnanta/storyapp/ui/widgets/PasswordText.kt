package com.esnanta.storyapp.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.esnanta.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@SuppressLint("ClickableViewAccessibility")
class PasswordText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputLayout(context, attrs) {

    private lateinit var editText: TextInputEditText

    init {
        // Set the end icon mode for password toggle
        endIconMode = END_ICON_PASSWORD_TOGGLE
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        editText = findViewById(R.id.passwordEditText)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswordLength(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkPasswordLength(password: String) {
        if (password.length < 8) {
            error = "Password must be at least 8 characters"
        } else {
            error = null
        }
    }
}