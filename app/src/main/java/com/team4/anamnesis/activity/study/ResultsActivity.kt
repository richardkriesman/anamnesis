package com.team4.anamnesis.activity.study

import android.os.Bundle
import android.app.Activity
import com.team4.anamnesis.R

import kotlinx.android.synthetic.main.activity_results.*

class ResultsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
