package em.i.repository

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "entries")
data class Entry(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "hour") var hour: Int = 0,
        @ColumnInfo(name = "minute") var minute: Int = 0,
        @ColumnInfo(name = "type") var type: Int = 0
) {

    companion object {
        val MAX_HOUR = 18
        val MIN_HOUR = 8

        val TYPE_MET = 9
        val TYPE_KITCHEN = 1
        val TYPE_ENTER_OFFICE = 2
    }

    val computedValue: Int
        get() = hour * 60 + minute
}