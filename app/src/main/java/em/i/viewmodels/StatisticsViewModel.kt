package em.i.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import em.i.repository.Entry
import em.i.repository.ImeDatabase

class StatisticsViewModel(val database: ImeDatabase) : ViewModel() {

    var entries: LiveData<List<Entry>>? = null

    fun loadEntries() {
        entries = database.getEntryDao().fetch()
    }

    fun addEntry(entry: Entry) {
        database.getEntryDao().add(entry)
    }
}