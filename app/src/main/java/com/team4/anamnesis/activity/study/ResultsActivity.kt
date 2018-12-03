package com.team4.anamnesis.activity.study

import android.os.Bundle
import android.app.Activity
import com.team4.anamnesis.R

class ResultsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__results)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
