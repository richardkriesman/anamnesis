package com.team4.anamnesis.activity.study

import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.team4.anamnesis.R
import com.team4.anamnesis.db.entity.Flashcard

class StudyAdapter(context: AppCompatActivity, isStealthMode: Boolean):
        RecyclerView.Adapter<StudyAdapter.EditDeckHolder>() {

    /**
     * A listener that fires when the card face has been changed.
     */
    var onCardFaceChange: ((flashcard: Flashcard, isBackFaceShowing: Boolean) -> Unit)? = null

    private val c: AppCompatActivity = context
    private var flashcards: List<Flashcard> = ArrayList() // list of flashcards
    private val isStealthModeEnabled: Boolean = isStealthMode

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditDeckHolder {
        val view: View = LayoutInflater.from(c).inflate(R.layout.item__card_study, parent, false)

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
        private val card: CardView = itemView.findViewById(R.id.item_flashcard_study_card)
        private val text: TextView = itemView.findViewById(R.id.item_flashcard_study_text)

        private var isBackFaceShowing: Boolean = false // whether the back face of the card is showing

        fun bind(flashcard: Flashcard) {

            // show front/back card face
            updateCardFace(flashcard)

            // handle card taps
            card.setOnClickListener {
                isBackFaceShowing = !isBackFaceShowing
                updateCardFace(flashcard)
                onCardFaceChange?.invoke(flashcard, isBackFaceShowing)
            }

            // change colors if in stealth mode
            if (isStealthModeEnabled) {

                // set background to black
                card.background = ColorDrawable(c.resources.getColor(android.R.color.black, c.theme))

                // set text to dark grey
                text.setTextColor(c.resources.getColor(R.color.stealthModeAccent, c.theme))

            }

        }

        /**
         * Update the card's text to display the front/back face of the card based on which side should be visible.
         */
        private fun updateCardFace(flashcard: Flashcard) {
            if (isBackFaceShowing) {
                text.text = flashcard.backText
            } else {
                text.text = flashcard.frontText
            }
        }
    }

}