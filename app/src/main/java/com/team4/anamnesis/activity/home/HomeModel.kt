package com.team4.anamnesis.activity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.Group

class HomeModel: ViewModel() {
    val groups: LiveData<List<Group>> = AppDatabase.instance!!.groupDao().getAll()

    fun createGroup(group: Group) {
        AppDatabase.instance!!.groupDao().create(group)
    }

    fun deleteGroup(group: Group) {
        AppDatabase.instance!!.groupDao().delete(group)
    }

    fun updateGroup(group: Group) {
        AppDatabase.instance!!.groupDao().update(group)
    }

}