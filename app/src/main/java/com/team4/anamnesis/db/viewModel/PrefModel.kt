package com.team4.anamnesis.db.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.Pref

class PrefModel: ViewModel() {
    private val prefs: HashMap<String, LiveData<List<Pref>>> = HashMap()

    /**
     * Returns the Pref with a specified name.
     */
    operator fun get(name: String): LiveData<List<Pref>> {
        return if (prefs.containsKey(name)) {
            prefs[name]!!
        } else {
            val pref: LiveData<List<Pref>> = AppDatabase.instance!!.prefDao().getByKey(name)
            prefs[name] = pref
            pref
        }
    }

    /**
     * Sets a Pref, overwriting any with the same key.
     */
    fun set(pref: Pref) {
        val prefLiveData: LiveData<List<Pref>> = this[pref.name]
        if ((prefLiveData.value?.size ?: 0) == 0) {
            AppDatabase.instance!!.prefDao().create(pref)
        } else {
            val prefDb: Pref = prefLiveData.value!![0]
            prefDb.value = pref.value
            AppDatabase.instance!!.prefDao().update(prefDb)
        }
    }

}