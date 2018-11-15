package com.team4.anamnesis.activity.editDeck

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.Deck
import com.team4.anamnesis.db.entity.Flashcard

class EditDeckModel: ViewModel() {
    lateinit var flashcards: LiveData<List<Flashcard>>

    fun load(deck: Deck) {
        flashcards = AppDatabase.instance!!.flashcardDao().getByDeckId(deck.id)
    }

    fun createFlashcard(flashcard: Flashcard) {
        AppDatabase.instance!!.flashcardDao().create(flashcard)
    }

    fun deleteFlashcard(flashcard: Flashcard) {
        AppDatabase.instance!!.flashcardDao().delete(flashcard)
    }

    fun updateFlashcard(flashcard: Flashcard) {
        AppDatabase.instance!!.flashcardDao().update(flashcard)
    }

}