package com.team4.anamnesis.activity.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.Deck
import com.team4.anamnesis.db.entity.Group

class GroupModel: ViewModel() {
    lateinit var decks: LiveData<List<Deck>>

    fun load(group: Group) {
        decks = AppDatabase.instance!!.flashcardDeckDao().getByGroupId(group.id)
    }

    fun createDeck(deck: Deck) {
        AppDatabase.instance!!.flashcardDeckDao().create(deck)
    }

    fun deleteDeck(deck: Deck) {
        AppDatabase.instance!!.flashcardDeckDao().delete(deck)
    }
}