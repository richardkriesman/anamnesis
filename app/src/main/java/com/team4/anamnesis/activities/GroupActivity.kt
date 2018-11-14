package com.team4.anamnesis.activities

import android.os.Bundle
import android.app.Activity
import com.team4.anamnesis.R

import kotlinx.android.synthetic.main.activity_group.*

class GroupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
