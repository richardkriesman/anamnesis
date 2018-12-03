package com.team4.anamnesis.activity.modeSelect

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.team4.anamnesis.R
import com.team4.anamnesis.activity.study.StudyActivity
import com.team4.anamnesis.activity.study.StudyMode
import com.team4.anamnesis.db.entity.Deck

class ModeSelectActivity : AppCompatActivity() {

    private lateinit var deck: Deck
    private lateinit var modeDesc: TextView
    private lateinit var modeTitle: TextView
    private lateinit var modeIcon: ImageView
    private lateinit var radioGroup: RadioGroup
    private lateinit var startButton: Button

    override fun onBackPressed() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__mode_select)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // extract deck from intent
        deck = intent.getSerializableExtra("deck") as Deck
        title = String.format(resources.getString(R.string.mode_activity_title), deck.name)

        // get views
        modeDesc = findViewById(R.id.mode_desc)
        modeTitle = findViewById(R.id.mode_title)
        modeIcon = findViewById(R.id.mode__icon)
        radioGroup = findViewById(R.id.mode_radio_group)
        startButton = findViewById(R.id.mode_start)

        // handle radio button option change
        radioGroup.setOnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.mode_scored -> {
                    modeTitle.text = resources.getText(R.string.mode_option_scored)
                    modeDesc.text = resources.getText(R.string.mode_option_scored_desc)
                    modeIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_grade_24px, theme))
                }
                R.id.mode_unscored -> {
                    modeTitle.text = resources.getText(R.string.mode_option_unscored)
                    modeDesc.text = resources.getText(R.string.mode_option_unscored_desc)
                    modeIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_weekend_48px, theme))
                }
                R.id.mode_stealth -> {
                    modeTitle.text = resources.getText(R.string.mode_option_stealth)
                    modeDesc.text = resources.getText(R.string.mode_option_stealth_desc)
                    modeIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_incognito, theme))
                }
            }
        }

        // handle start button
        startButton.setOnClickListener {
            val intent = Intent(this, StudyActivity::class.java)
            intent.putExtra("deck", deck)
            when (radioGroup.checkedRadioButtonId) { // add intent extra specifying the type of study
                R.id.mode_scored -> intent.putExtra("mode", StudyMode.SCORED.id)
                R.id.mode_unscored -> intent.putExtra("mode", StudyMode.UNSCORED.id)
                R.id.mode_stealth -> intent.putExtra("mode", StudyMode.STEALTH.id)
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
