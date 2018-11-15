package com.team4.anamnesis.activity.editDeck

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.team4.anamnesis.R
import com.team4.anamnesis.db.entity.Flashcard
import kotlinx.android.synthetic.main.nav_header_main.view.*

class EditDeckAdapter(context: AppCompatActivity): RecyclerView.Adapter<EditDeckAdapter.EditDeckHolder>() {
    private val c: AppCompatActivity = context

    private var flashcards: List<Flashcard> = ArrayList() // list of flashcards

    /**
     * A listener that fires when the user clicks the "delete" button.
     */
    var onFlashcardDeleteClicked: ((flashcard: Flashcard) -> Unit)? = null

    /**
     * A listener that fires then the flashcard is changed.
     */
    var onFlashcardChanged: ((flashcard: Flashcard) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditDeckHolder {
        val view: View = LayoutInflater.from(c).inflate(R.layout.item_flashcard_edit, parent, false)

        // set width to parent view width
        val params: ViewGroup.LayoutParams = view.layoutParams
        val displayMetrics = DisplayMetrics()
        c.windowManager.defaultDisplay.getMetrics(displayMetrics)
        params.width = displayMetrics.widthPixels
        view.layoutParams = params

        return EditDeckHolder(view)
    }

    override fun getItemCount(): Int {
        return flashcards.size
    }

    override fun onBindViewHolder(holder: EditDeckHolder, position: Int) {
        holder.bind(flashcards[position])
    }

    fun setData(flashcards: List<Flashcard>) {
        this.flashcards = flashcards
        notifyDataSetChanged()
    }

    inner class EditDeckHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val bottomText: TextView = itemView.findViewById(R.id.item_flashcard_edit_bottom_text)
        val deleteButton: ImageView = itemView.findViewById(R.id.item_flashcard_edit_delete)
        val topText: TextView = itemView.findViewById(R.id.item_flashcard_edit_top_text)

        fun bind(flashcard: Flashcard) {
            topText.text = flashcard.frontText
            bottomText.text = flashcard.backText

            // handle delete button click
            deleteButton.setOnClickListener {
                onFlashcardDeleteClicked?.invoke(flashcard)
            }

            // handle top text change
            topText.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused) {
                    flashcard.frontText = topText.text.toString()
                    onFlashcardChanged?.invoke(flashcard)
                }
            }

            // handle bottom text change
            bottomText.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused) {
                    flashcard.backText = bottomText.text.toString()
                    onFlashcardChanged?.invoke(flashcard)
                }
            }

        }

    }

}