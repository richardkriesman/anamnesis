package com.team4.anamnesis.activity.group

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.team4.anamnesis.R
import com.team4.anamnesis.component.TextInputDialog
import com.team4.anamnesis.component.TextInputDialogCompletedListener
import com.team4.anamnesis.component.TextInputDialogValidationListener
import com.team4.anamnesis.db.entity.Deck
import com.team4.anamnesis.db.entity.Group
import org.jetbrains.anko.doAsync

class GroupActivity : AppCompatActivity() {

    private val adapter: GroupDeckAdapter = GroupDeckAdapter(this)
    private lateinit var group: Group
    private val manager: GridLayoutManager = GridLayoutManager(this, 2)
    private lateinit var model: GroupModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // extract group from intent
        group = intent.getSerializableExtra("group") as Group

        // set activity title to group name
        title = group.name

        // instantiate ViewModel
        model = ViewModelProviders.of(this).get(GroupModel::class.java)
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
            onDeckEditCLicked(it)
        }

        // listen for changes to decks
        model.decks.observe(this, Observer {
            adapter.setData(it)
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

    fun onDeckEditCLicked(deck: Deck) {

    }

}
