package com.team4.anamnesis.activity.study

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.team4.anamnesis.R
import com.team4.anamnesis.activity.home.HomeActivity
import com.team4.anamnesis.component.LinearPageLayoutManager
import com.team4.anamnesis.db.entity.Deck
import com.team4.anamnesis.db.entity.Flashcard
import com.team4.anamnesis.db.viewModel.FlashcardModel

enum class StudyMode(val id: Int) {
    SCORED(0),
    UNSCORED(1),
    STEALTH(2)
}

class StudyActivity : AppCompatActivity() {

    private val adapter: StudyAdapter = StudyAdapter(this)
    private lateinit var closeButton: ImageView
    private lateinit var correctButton: Button
    private lateinit var deck: Deck
    private lateinit var incorrectButton: Button
    private val manager: LinearPageLayoutManager = LinearPageLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
    private lateinit var leftButton: ImageView
    private lateinit var model: FlashcardModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var remainingFlashcards: List<Flashcard>
    private lateinit var responseHint: TextView
    private lateinit var rightButton: ImageView
    private var studyMode: Int = 0

    private var correctCount = 0
    private var incorrectCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__study)

        // extract deck from intent
        deck = intent.getSerializableExtra("deck") as Deck
        studyMode = intent.getIntExtra("mode", 0)

        // load views
        recyclerView = findViewById(R.id.study_recyclerview)
        leftButton = findViewById(R.id.study_left_button)
        rightButton = findViewById(R.id.study_right_button)
        correctButton = findViewById(R.id.study_correct_button)
        incorrectButton = findViewById(R.id.study_incorrect_button)
        closeButton = findViewById(R.id.study_close_button)
        responseHint = findViewById(R.id.study__response_hint)

        // instantiate ViewModel
        model = ViewModelProviders.of(this).get(FlashcardModel::class.java)
        model.load(deck)

        // instantiate RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
        PagerSnapHelper().attachToRecyclerView(recyclerView)

        // disable scrolling in scored mode
        if (studyMode == StudyMode.SCORED.id) {
            manager.isScrollingEnabled = false
        }

        // handle RecyclerView page changes
        manager.onPageChange = {
            if (studyMode == StudyMode.SCORED.id) {

                // disable scrolling in scored mode
                manager.isScrollingEnabled = false

                // remove the top card off the remaining stack
                remainingFlashcards = remainingFlashcards.slice(1 until remainingFlashcards.size)
                if (remainingFlashcards.isEmpty()) {
                    close()
                }

                // hide left/right buttons
                leftButton.visibility = View.GONE
                rightButton.visibility = View.GONE

                // hide correct/incorrect buttons
                correctButton.visibility = View.GONE
                incorrectButton.visibility = View.GONE
                responseHint.visibility = View.GONE

            }
        }

        // handle card face changes
        adapter.onCardFaceChange = { _: Flashcard, isBackFaceShowing: Boolean ->

            if (isBackFaceShowing) {

                // show correct/incorrect buttons and hide left/right buttons in scored mode
                if (studyMode == StudyMode.SCORED.id) {

                    // hide left/right buttons
                    leftButton.visibility = View.GONE
                    rightButton.visibility = View.GONE

                    // show correct/incorrect buttons
                    correctButton.visibility = View.VISIBLE
                    incorrectButton.visibility = View.VISIBLE
                    responseHint.visibility = View.VISIBLE

                } else { // not scored mode, show left/right buttons

                    // show left/right buttons
                    leftButton.visibility = View.VISIBLE
                    rightButton.visibility = View.VISIBLE

                }

            } else {

                // hide left/right buttons in scored mode
                if (studyMode == StudyMode.SCORED.id) {
                    leftButton.visibility = View.GONE
                    rightButton.visibility = View.GONE
                }

                // hide correct/incorrect buttons
                correctButton.visibility = View.GONE
                incorrectButton.visibility = View.GONE
                responseHint.visibility = View.GONE

            }
        }

        // hide left/right buttons in scored mode
        if (studyMode == StudyMode.SCORED.id) {
            leftButton.visibility = View.GONE
            rightButton.visibility = View.GONE
        }

        // handle left button click
        leftButton.setOnClickListener {
            val currentItem: Int = manager.currentPosition
            if (currentItem - 1 >= 0) {
                recyclerView.smoothScrollToPosition(currentItem - 1)
            }
        }

        // handle right button click
        rightButton.setOnClickListener {
            val currentItem: Int = manager.currentPosition
            if (currentItem + 1 < adapter.itemCount) {
                recyclerView.smoothScrollToPosition(currentItem + 1)
            }
        }

        // handle correct button
        correctButton.setOnClickListener {
            correctCount++

            // hide correct/incorrect buttons
            correctButton.visibility = View.GONE
            incorrectButton.visibility = View.GONE
            responseHint.visibility = View.GONE

            // move to next card
            val currentItem: Int = manager.currentPosition
            if (currentItem + 1 < adapter.itemCount) {
                manager.isScrollingEnabled = true
                recyclerView.smoothScrollToPosition(currentItem + 1)
            } else {
                close()
            }
        }

        // handle incorrect button
        incorrectButton.setOnClickListener {
            incorrectCount++

            // hide correct/incorrect buttons
            correctButton.visibility = View.GONE
            incorrectButton.visibility = View.GONE
            responseHint.visibility = View.GONE

            // move to next card
            val currentItem: Int = manager.currentPosition
            if (currentItem + 1 < adapter.itemCount) {
                manager.isScrollingEnabled = true
                recyclerView.smoothScrollToPosition(currentItem + 1)
            } else {
                close()
            }
        }

        // handle close button
        closeButton.setOnClickListener {
            close()
        }

        // listen for changes to flashcards
        model.flashcards.observe(this, Observer {
            remainingFlashcards = it
            adapter.setData(it)
        })
    }

    fun close() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}
