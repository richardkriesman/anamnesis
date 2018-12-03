package com.team4.anamnesis.component

import android.content.Context
import android.text.InputType
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.team4.anamnesis.R
import com.team4.anamnesis.utils.ScreenUtils

abstract class TextInputDialogCancelledListener {
    abstract fun onCancelled()
}

abstract class TextInputDialogCompletedListener {
    abstract fun onComplete(text: String)
}

abstract class TextInputDialogValidationListener {
    abstract fun onValidate(text: String): String?
}

/**
 * A UI dialog that prompts the user to enter text.
 *
 * @param context The parent activity's context.
 * @param title Prompt text to display to the user.
 */
class TextInputDialog(context: Context, title: Int, hint: Int) {

    /**
     * A listener that is fired when the TextInputDialog has been cancelled.
     */
    var onCancelledListener: TextInputDialogCancelledListener? = null

    /**
     * A listener that is fired when the TextInputDialog has been completed. That is, the user
     * has entered text and dismissed the dialog.
     */
    var onCompletedListener: TextInputDialogCompletedListener? = null

    /**
     * A listener that is fired when the user tries to complete the dialog. If a String is returned, the value of that
     * String is used to display a validation error. If null, the validation will be considered to have succeeded.
     */
    var onValidateListener: TextInputDialogValidationListener? = null

    /**
     * The user-provided text. This field will be `null` unless and until the user
     * completes the dialog.
     */
    var text: String? = null
        private set(value) {
            field = value
        }

    private val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    private val dialog: AlertDialog
    private val input: TextInputEditText = TextInputEditText(context)
    private val inputLayout: TextInputLayout = TextInputLayout(context)

    init {

        // set the dialog's title
        builder.setTitle(title)

        // configure the input EditText
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.hint = context.resources.getString(hint)
        inputLayout.addView(input)

        // configure the InputLayout around the EditText
        val layoutPadding: Int = ScreenUtils.pxToDp(context, 16)
        inputLayout.setPadding(layoutPadding, 0, layoutPadding, 0)
        inputLayout.isErrorEnabled = true
        builder.setView(inputLayout)

        // create positive action button
        val positiveButtonText: String = context.resources.getString(R.string.textinputdialog__button__positive)
        builder.setPositiveButton(positiveButtonText) { _, _ ->
            run {}
        }

        // create negative action button
        val negativeButtonText: String = context.resources.getString(R.string.textinputdialog__button__negative)
        builder.setNegativeButton(negativeButtonText) { dialogInterface, _ ->
            run {
                dialogInterface.cancel()
            }
        }

        // listen for dialog dismissal
        builder.setOnDismissListener {
            if (text != null) {
                onCompletedListener?.onComplete(text!!) // dialog only dismisses when text is not null
            }
        }

        // listen for dialog cancellation
        builder.setOnCancelListener {
            onCancelledListener?.onCancelled()
        }

        // construct the final dialog
        dialog = builder.create()
    }

    /**
     * Presents the dialog to the user. This function is non-blocking.
     */
    fun show() {
        dialog.show()

        // override the pawsitive button handler - this lets us suppress the closing of the dialog if validation fails
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            run {
                val text: String = input.text.toString() // user text input

                // run validation and dismiss if it succeeds
                val validationErrorText: String? = this@TextInputDialog.onValidateListener?.onValidate(text)
                val didValidationSucceed: Boolean = validationErrorText == null
                if (didValidationSucceed) { // validation succeeded, update text property and dismiss the dialog
                    this@TextInputDialog.text = input.text.toString() // set text property
                    dialog.dismiss()
                } else { // validation failed, set error text on the input
                    inputLayout.error = validationErrorText
                }
            }
        }

    }

}