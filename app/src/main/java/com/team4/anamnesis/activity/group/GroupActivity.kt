package com.team4.anamnesis.activity.group

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.team4.anamnesis.R
import com.team4.anamnesis.activity.editDeck.EDIT_DECK_REQUEST_CODE
import com.team4.anamnesis.activity.editDeck.EditDeckActivity
import com.team4.anamnesis.activity.modeSelect.ModeSelectActivity
import com.team4.anamnesis.component.TextInputDialog
import com.team4.anamnesis.component.TextInputDialogCompletedListener
import com.team4.anamnesis.component.TextInputDialogValidationListener
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.Deck
import com.team4.anamnesis.db.entity.Flashcard
import com.team4.anamnesis.db.entity.Group
import com.team4.anamnesis.db.viewModel.DeckModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class GroupActivity : AppCompatActivity() {

    private val adapter: GroupDeckAdapter = GroupDeckAdapter(this)
    private lateinit var emptyTitle: TextView
    private lateinit var emptySubtitle: TextView
    private lateinit var group: Group
    private val manager: GridLayoutManager = GridLayoutManager(this, 2)
    private lateinit var model: DeckModel
    private lateinit var recyclerView: RecyclerView

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            EDIT_DECK_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null) { // affirm to the user that the result was saved
                    val deck: Deck = data.getSerializableExtra("deck") as Deck // the deck that was saved
                    val snackbarText: String = String.format(resources.getString(R.string.group_snackbar_edit_success),
                            deck.name)
                    Snackbar.make(recyclerView, snackbarText, Snackbar.LENGTH_LONG)
                            .show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // extract group from intent
        group = intent.getSerializableExtra("group") as Group

        // set activity title to group name
        title = group.name

        // instantiate ViewModel
        model = ViewModelProviders.of(this).get(DeckModel::class.java)
        model.load(group)

        // instantiate create deck button
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view -> // button was clicked
            onCreateDeckClicked(view)
        }

        // instantiate recyclerview
        recyclerView = findViewById(R.id.group_recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
        emptyTitle = findViewById(R.id.group_empty_title)
        emptySubtitle = findViewById(R.id.group_empty_subtitle)

        // listen for deck click
        adapter.onDeckClicked = {
            onDeckClicked(it)
        }

        // listen for deck delete option click
        adapter.onDeckDeleteClicked = {
            onDeckDeleteClicked(it)
        }

        // listen for deck edit option click
        adapter.onDeckEditClicked = {
            onDeckEditClicked(it)
        }

        // listen for changes to decks
        model.decks.observe(this, Observer {
            adapter.setData(it)

            // show/hide empty text if empty
            if (it.isEmpty()) {
                emptyTitle.visibility = View.VISIBLE
                emptySubtitle.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyTitle.visibility = View.GONE
                emptySubtitle.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })

    }

    fun onCreateDeckClicked(view: View) {

        // prompt the user to name the new deck
        val dialog = TextInputDialog(this, R.string.group_dialog_new_deck_title,
                R.string.group_dialog_new_group_hint)

        // validate text input when the user taps "Ok"
        dialog.onValidateListener = object : TextInputDialogValidationListener() {
            override fun onValidate(text: String): String? {
                return if (text.isNotEmpty())
                    null else "Type a name"
            }
        }

        // listen for completion
        dialog.onCompletedListener = object : TextInputDialogCompletedListener() {
            override fun onComplete(text: String) { // we now have the name of the deck

                // create a new deck
                val deck = Deck(name = text, groupId = group.id)
                doAsync {
                    model.createDeck(deck)
                }

                // display a snackbar affirming the new deck was created
                val snackbarText = String.format(resources.getString(R.string.group_snackbar_new_deck), text)
                Snackbar.make(view, snackbarText, Snackbar.LENGTH_LONG).show()

            }
        }

        // show the dialog
        dialog.show()

    }

    fun onDeckClicked(deck: Deck) {
        val intent = Intent(this, ModeSelectActivity::class.java)
        intent.putExtra("deck", deck)
        startActivity(intent)
    }

    fun onDeckDeleteClicked(deck: Deck) {

        // delete the deck
        doAsync {
            model.deleteDeck(deck)
        }

        // show a snackbar about the deck being deleted
        val snackbarText = String.format(resources.getString(R.string.group_snackbar_deleted_deck), deck.name)
        Snackbar.make(recyclerView, snackbarText, Snackbar.LENGTH_LONG)
                .setAction(R.string.home_snackbar_deleted_group_action) {
                    doAsync {
                        model.createDeck(deck) // user clicked undo button, recreate the deck
                    }
                }
                .show()

    }

    fun onDeckEditClicked(deck: Deck) {
        val intent = Intent(this, EditDeckActivity::class.java)
        intent.putExtra("deck", deck)
        startActivityForResult(intent, EDIT_DECK_REQUEST_CODE)
    }

}
