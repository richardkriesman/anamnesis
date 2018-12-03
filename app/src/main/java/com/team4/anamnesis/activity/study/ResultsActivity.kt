package com.team4.anamnesis.activity.study

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.team4.anamnesis.R

class ResultsActivity : AppCompatActivity() {

    private lateinit var closeButton: ImageView
    private lateinit var finishButton: Button
    private lateinit var ratio: TextView
    private lateinit var time: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__results)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // find views
        closeButton = findViewById(R.id.results__close_button)
        finishButton = findViewById(R.id.results__finish)
        ratio = findViewById(R.id.results__ratio)
        time = findViewById(R.id.results__time)

        // extract results from intent
        ratio.text = String.format(resources.getString(R.string.results__ratio),
                intent.getIntExtra("correctCount", 0),
                intent.getIntExtra("totalCount", 0))
        time.text = String.format(resources.getString(R.string.results__time), intent.getStringExtra("time"))

        // dismiss when close or finish button is clicked
        closeButton.setOnClickListener {

        }
        finishButton.setOnClickListener {
            finish()
        }

    }

}
