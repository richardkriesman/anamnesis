package com.team4.anamnesis.db.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.Deck
import com.team4.anamnesis.db.entity.Folder

class DeckModel: ViewModel() {
    lateinit var decks: LiveData<List<Deck>>

    fun load(group: Folder) {
        decks = AppDatabase.instance!!.deckDao().getByGroupId(group.id)
    }

    fun createDeck(deck: Deck) {
        AppDatabase.instance!!.deckDao().create(deck)
    }

    fun deleteDeck(deck: Deck) {
        AppDatabase.instance!!.deckDao().delete(deck)
    }
}