package com.team4.anamnesis.activity.studyMode

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.team4.anamnesis.R
import com.team4.anamnesis.db.entity.Deck

class StudyModeActivity : AppCompatActivity() {

    private lateinit var deck: Deck
    private lateinit var modeDesc: TextView
    private lateinit var modeTitle: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var startButton: Button

    override fun onBackPressed() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_mode)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // extract deck from intent
        deck = intent.getSerializableExtra("deck") as Deck
        title = String.format(resources.getString(R.string.mode_activity_title), deck.name)

        // get views
        modeDesc = findViewById(R.id.mode_desc)
        modeTitle = findViewById(R.id.mode_title)
        radioGroup = findViewById(R.id.mode_radio_group)
        startButton = findViewById(R.id.mode_start)

        // handle radio button option change
        radioGroup.setOnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.mode_scored -> {
                    modeTitle.text = resources.getText(R.string.mode_option_scored)
                    modeDesc.text = resources.getText(R.string.mode_option_scored_desc)
                }
                R.id.mode_unscored -> {
                    modeTitle.text = resources.getText(R.string.mode_option_unscored)
                    modeDesc.text = resources.getText(R.string.mode_option_unscored_desc)
                }
                R.id.mode_stealth -> {
                    modeTitle.text = resources.getText(R.string.mode_option_stealth)
                    modeDesc.text = resources.getText(R.string.mode_option_stealth_desc)
                }
            }
        }

        // handle start button
        startButton.setOnClickListener {
            val intent = Intent(this, null) // TODO: Change null to study activity
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            when (radioGroup.checkedRadioButtonId) { // add intent extra specifying the type of study
                R.id.mode_scored -> {
                    // TODO: Start scored study
                }
                R.id.mode_unscored -> {
                    // TODO: Start unscored study
                }
                R.id.mode_stealth -> {
                    // TODO: Start stealth study
                }
            }
            startActivity(intent)
        }

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
