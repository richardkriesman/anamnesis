package com.team4.anamnesis.activity.study

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.team4.anamnesis.R
import com.team4.anamnesis.activity.settings.PREF_FONT_SIZE
import com.team4.anamnesis.component.LinearPageLayoutManager
import com.team4.anamnesis.db.entity.Deck
import com.team4.anamnesis.db.entity.Flashcard
import com.team4.anamnesis.db.viewModel.FlashcardModel
import com.team4.anamnesis.db.viewModel.PrefModel
import java.util.*
import kotlin.random.Random

enum class StudyMode(val id: Int) {
    SCORED(0),
    UNSCORED(1),
    STEALTH(2)
}

class StudyActivity : AppCompatActivity() {

    private lateinit var adapter: StudyAdapter
    private lateinit var closeButton: ImageView
    private lateinit var correctButton: Button
    private lateinit var deck: Deck
    private lateinit var incorrectButton: Button
    private val manager: LinearPageLayoutManager = LinearPageLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
    private lateinit var leftButton: ImageView
    private lateinit var model: FlashcardModel
    private lateinit var prefModel: PrefModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var remainingFlashcards: List<Flashcard>
    private lateinit var responseHint: TextView
    private lateinit var rightButton: ImageView
    private lateinit var root: ConstraintLayout
    private var studyMode: Int = 0
    private lateinit var time: TextView

    private var correctCount: Int = 0
    private var incorrectCount: Int = 0
    private var timeElapsed: Int = 0

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
        root = findViewById(R.id.study__root)
        time = findViewById(R.id.study__time)

        // instantiate ViewModel
        model = ViewModelProviders.of(this).get(FlashcardModel::class.java)
        model.load(deck)
        prefModel = ViewModelProviders.of(this).get(PrefModel::class.java)

        // instantiate RecyclerView
        adapter = StudyAdapter(this, studyMode == StudyMode.STEALTH.id)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
        PagerSnapHelper().attachToRecyclerView(recyclerView)

        // disable scrolling in scored mode
        if (studyMode == StudyMode.SCORED.id) {
            manager.isScrollingEnabled = false
        }

        // stealth mode modifications
        if (studyMode == StudyMode.STEALTH.id) {

            // change background to black
            root.background = ColorDrawable(resources.getColor(android.R.color.black, theme))

            // hide status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

            // change buttons to light grey
            closeButton.setColorFilter(resources.getColor(R.color.stealthModeAccent, theme))
            leftButton.setColorFilter(resources.getColor(R.color.stealthModeAccent, theme))
            rightButton.setColorFilter(resources.getColor(R.color.stealthModeAccent, theme))
            time.setTextColor(resources.getColor(R.color.stealthModeAccent, theme))

        }

        // handle RecyclerView page changes
        manager.onPageChange = {
            if (studyMode == StudyMode.SCORED.id) {

                // disable scrolling in scored mode
                manager.isScrollingEnabled = false

                // remove the top card off the remaining stack
                remainingFlashcards = remainingFlashcards.slice(1 until remainingFlashcards.size)
                if (remainingFlashcards.isEmpty()) {
                    completeSession()
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
                completeSession()
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
                completeSession()
            }
        }

        // handle close button
        closeButton.setOnClickListener {
            finish()
        }

        // listen for changes to flashcards
        model.flashcards.observe(this, Observer {

            // randomly sort cards in scored mode
            val flashcards: List<Flashcard>
            if (studyMode == StudyMode.SCORED.id) {
                flashcards = ArrayList()
                for (card in it) {
                    val index: Int = if (flashcards.isEmpty()) {
                        0
                    } else {
                        Random(System.currentTimeMillis() / 1000L).nextInt(0, flashcards.size)
                    }
                    flashcards.add(index, card)
                }
            } else {
                flashcards = it
            }

            // update flashcard set
            remainingFlashcards = flashcards
            adapter.setData(flashcards)
        })

        // load font size
        prefModel[PREF_FONT_SIZE].observe(this, Observer {
            if (it.isNotEmpty()) {
                adapter.fontSize = it[0].value.toInt()
            }
        })

        // increment time every second
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                timeTick()
            }
        }, 1000)

    }

    fun completeSession() {
        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra("correctCount", correctCount)
        intent.putExtra("totalCount", correctCount + incorrectCount)
        intent.putExtra("time", time.text)
        startActivity(intent)
    }

    fun timeTick() {
        timeElapsed++ // increment time elapsed

        // build time elapsed string
        var hours = 0
        var minutes = 0
        var seconds = timeElapsed
        while (seconds >= 60) {
            when {
                seconds >= 3600 -> {
                    hours++
                    seconds -= 3600
                }
                seconds >= 60 -> {
                    minutes++
                    seconds -= 60
                }
            }
        }

        // schedule the next tick
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                timeTick()
            }
        }, 1000)


        // update time elapsed in ui
        runOnUiThread {
            time.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
    }

}
