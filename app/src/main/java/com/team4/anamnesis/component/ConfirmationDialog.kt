package com.team4.anamnesis.component

import android.content.Context
import android.text.InputType
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.team4.anamnesis.R
import com.team4.anamnesis.utils.ScreenUtils

abstract class ConfirmationDialogCancelledListener {
    abstract fun onCancelled()
}

abstract class ConfirmationDialogConfirmedListener {
    abstract fun onComplete()
}

/**
 * A UI dialog that prompts the user for confirmation.
 *
 * @param context The parent activity's context.
 * @param title Prompt text to display to the user.
 */
class ConfirmationDialog(context: Context, title: Int, confirmationText: Int, cancellationText: Int) {

    /**
     * A listener that is fired when the ConfirmationDialog has been cancelled.
     */
    var onCancelledListener: ConfirmationDialogCancelledListener? = null

    /**
     * A listener that is fired when the ConfirmationDialog has been confirmed.
     */
    var onConfirmedListener: ConfirmationDialogConfirmedListener? = null

    private val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    private val dialog: AlertDialog
    private val inputLayout: TextInputLayout = TextInputLayout(context)
    private var isCancelled: Boolean = false

    init {

        // set the dialog's title
        builder.setTitle(title)

        // configure the InputLayout around the EditText
        val layoutPadding: Int = ScreenUtils.pxToDp(context, 16)
        inputLayout.setPadding(layoutPadding, 0, layoutPadding, 0)
        inputLayout.isErrorEnabled = true
        builder.setView(inputLayout)

        // create positive action button
        val positiveButtonText: String = context.resources.getString(confirmationText)
        builder.setPositiveButton(positiveButtonText) { _, _ ->
            run {}
        }

        // create negative action button
        val negativeButtonText: String = context.resources.getString(cancellationText)
        builder.setNegativeButton(negativeButtonText) { dialogInterface, _ ->
            run {
                dialogInterface.cancel()
            }
        }

        // listen for dialog confirmation
        builder.setOnDismissListener {
            if (!isCancelled) {
                onConfirmedListener?.onComplete()
            }
        }

        // listen for dialog cancellation
        builder.setOnCancelListener {
            isCancelled = true
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
    }

}