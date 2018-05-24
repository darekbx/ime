package em.i.repository

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface EntryDao {

    @Query("SELECT * FROM entries")
    fun fetch() : LiveData<List<Entry>>

    @Insert
    fun add(entry: Entry): Long
}