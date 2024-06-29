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

    private var clearButtonImage: Drawable
    private lateinit var editText: TextInputEditText

    init {
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        editText = findViewById(R.id.passwordEditText)

        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (editText.right - editText.compoundDrawables[2].bounds.width())) {
                    editText.text?.clear()
                    return@setOnTouchListener true
                }
            }
            false
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    showClearButton()
                    checkPasswordLength(s.toString())
                } else {
                    hideClearButton()
                    editText.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showClearButton() {
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, clearButtonImage, null)
    }

    private fun hideClearButton() {
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
    }

    private fun checkPasswordLength(password: String) {
        if (password.length < 8) {
            editText.error = "Password must be at least 8 characters"
        } else {
            editText.error = null
        }
    }
}