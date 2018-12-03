package com.team4.anamnesis.activity.editDeck

import android.text.Editable
import android.text.TextWatcher
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

class EditDeckAdapter(context: AppCompatActivity): RecyclerView.Adapter<EditDeckAdapter.EditDeckHolder>() {

    /**
     * When this value is false, the first data set has already been loaded.
     */
    var isFirstLoad: Boolean = true
        private set(value) {
            field = value
        }

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
        val view: View = LayoutInflater.from(c).inflate(R.layout.item__card_edit, parent, false)

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
        val isFlashcardAddedOrRemoved: Boolean = this.flashcards.size != flashcards.size
        this.flashcards = flashcards
        if (isFirstLoad || isFlashcardAddedOrRemoved) { // card was added or removed, reload data set
            isFirstLoad = false
            notifyDataSetChanged()
        }
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
            topText.addTextChangedListener(object: TextWatcher {

                override fun afterTextChanged(p0: Editable?) {
                    flashcard.frontText = p0?.toString() ?: ""
                    onFlashcardChanged?.invoke(flashcard)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            })

            // handle bottom text change
            bottomText.addTextChangedListener(object: TextWatcher {

                override fun afterTextChanged(p0: Editable?) {
                    flashcard.backText = p0?.toString() ?: ""
                    onFlashcardChanged?.invoke(flashcard)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            })

        }

    }

}