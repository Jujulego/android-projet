package net.capellari.julien.projetandroid.db

import androidx.lifecycle.LiveData
import androidx.room.*
import net.capellari.julien.utils.DiffItem
import java.util.*

@Entity
data class Match(@PrimaryKey(autoGenerate = true) var id: Long,
    var titre: String,
    var description: String = "",
    var latitude: Double = .0,
    var longitude: Double = .0,
    var date: Date = Date())
        : DiffItem<Match> {

    // Méthodes
    override fun isSameItem(other: Match)
        = (id == other.id)

    override fun isSameContent(other: Match)
        = (titre == other.titre)
            && (description == other.description)
            && (latitude == other.latitude)
            && (longitude == other.longitude)
            && (date == other.date)

    // Dao
    @Dao
    interface MatchDao {
        // Accès
        @Query("select * from `Match` order by date, titre")
        fun all(): LiveData<Array<Match>>

        @Query("select * from `Match` where id = :id")
        fun getById(id: Long): LiveData<Match>

        // Modifications
        @Insert
        fun insert(match: Match): Long

        @Update
        fun update(match: Match)

        @Delete
        fun delete(match: Match)
    }
}