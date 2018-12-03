package com.team4.anamnesis.db.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.Folder

class FolderModel: ViewModel() {
    val groups: LiveData<List<Folder>> = AppDatabase.instance!!.folderDao().getAll()

    fun createFolder(folder: Folder) {
        AppDatabase.instance!!.folderDao().create(folder)
    }

    fun deleteFolder(folder: Folder) {
        AppDatabase.instance!!.folderDao().delete(folder)
    }

    fun updateFolder(folder: Folder) {
        AppDatabase.instance!!.folderDao().update(folder)
    }

}