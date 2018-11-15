package com.team4.anamnesis.activity.editDeck

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.team4.anamnesis.R
import com.team4.anamnesis.component.LinearPageHelper
import com.team4.anamnesis.component.LinearPageLayoutManager
import com.team4.anamnesis.db.entity.Deck
import com.team4.anamnesis.db.entity.Flashcard
import org.jetbrains.anko.doAsync

class EditDeckActivity : AppCompatActivity() {

    private val adapter: EditDeckAdapter = EditDeckAdapter(this)
    private lateinit var deck: Deck
    private lateinit var emptyTitle: TextView
    private lateinit var emptySubtitle: TextView
    private val manager: LinearPageLayoutManager = LinearPageLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
    private lateinit var leftButton: ImageView
    private lateinit var model: EditDeckModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var rightButton: ImageView
    private lateinit var scrollIndicator: TextView

    override fun onBackPressed() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_deck)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // extract deck from intent
        deck = intent.getSerializableExtra("deck") as Deck

        // set title to deck name
        title = String.format(resources.getString(R.string.edit_title), deck.name)

        // load views
        recyclerView = findViewById(R.id.edit_recyclerview)
        emptySubtitle = findViewById(R.id.edit_empty_subtitle)
        emptyTitle = findViewById(R.id.edit_empty_title)
        leftButton = findViewById(R.id.edit_left_button)
        rightButton = findViewById(R.id.edit_right_button)
        scrollIndicator = findViewById(R.id.edit_scroll_indicator)

        // instantiate ViewModel
        model = ViewModelProviders.of(this).get(EditDeckModel::class.java)
        model.load(deck)

        // instantiate create deck button
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view -> // button was clicked
            onCreateFlashcardClicked(view)
        }

        // handle left button click
        leftButton.setOnClickListener {
            val currentItem: Int = manager.findFirstCompletelyVisibleItemPosition()
            if (currentItem - 1 >= 0) {
                recyclerView.smoothScrollToPosition(currentItem - 1)
            }
        }

        // handle right button click
        rightButton.setOnClickListener {
            val currentItem: Int = manager.findFirstCompletelyVisibleItemPosition()
            if (currentItem + 1 < adapter.itemCount) {
                recyclerView.smoothScrollToPosition(currentItem + 1)
            }
        }

        // instantiate RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
        LinearPageHelper().attachToRecyclerView(recyclerView)

        // handle RecyclerView page changes
        manager.onPageChange = {
            scrollIndicator.text = String.format(resources.getString(R.string.edit_scroll_indicator_text), it + 1,
                    adapter.itemCount)
        }

        // handle flashcard changes
        adapter.onFlashcardChanged = {
            onFlashcardChanged(it)
        }

        // handle flashcard deletions
        adapter.onFlashcardDeleteClicked = {
            onFlashcardDeleteClicked(it)
        }

        // listen for changes to flashcards
        model.flashcards.observe(this, Observer {
            adapter.setData(it)

            // show/hide empty text if empty
            if (it.isEmpty()) {
                emptyTitle.visibility = View.VISIBLE
                emptySubtitle.visibility = View.VISIBLE
                leftButton.visibility = View.GONE
                recyclerView.visibility = View.GONE
                rightButton.visibility = View.GONE
                scrollIndicator.visibility = View.GONE
            } else {
                emptyTitle.visibility = View.GONE
                emptySubtitle.visibility = View.GONE
                leftButton.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
                rightButton.visibility = View.VISIBLE
                scrollIndicator.visibility = View.VISIBLE
            }

            // initialize scroll indicator text
            scrollIndicator.text = String.format(resources.getString(R.string.edit_scroll_indicator_text),
                    manager.currentPosition + 1, adapter.itemCount)
        })

    }

    fun onCreateFlashcardClicked(view: View) {

        // create a new flashcard model
        val flashcard = Flashcard(frontText = "", backText =  "", deckId = deck.id)
        doAsync {
            model.createFlashcard(flashcard)
        }

        // display a snackbar confirming the deletion
        Snackbar.make(recyclerView, R.string.edit_snackbar_card_added, Snackbar.LENGTH_LONG).show()

    }

    fun onFlashcardChanged(flashcard: Flashcard) {
        doAsync {
            model.updateFlashcard(flashcard)
        }
    }

    fun onFlashcardDeleteClicked(flashcard: Flashcard) {

        // delete the flashcard
        doAsync {
            model.deleteFlashcard(flashcard)
        }

        // display a snackbar confirming the deletion
        Snackbar.make(recyclerView, R.string.edit_snackbar_card_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.home_snackbar_deleted_group_action) {
                    doAsync {
                        model.createFlashcard(flashcard)
                    }
                }
                .show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
