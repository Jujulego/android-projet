package net.capellari.julien.projetandroid.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Entity
data class Match(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var titre: String,
    var description: String,
    var date: Date
) {
    // Dao
    @Dao
    interface MatchDao {
        // Accès
        @Query("select * from `Match` order by titre")
        fun all(): LiveData<Array<Match>>

        @Query("select * from `Match` where id = :id")
        fun getById(id: Int): LiveData<Match>

        // Modifications
        @Insert
        fun insert(match: Match)

        @Update
        fun update(match: Match)

        @Delete
        fun delete(match: Match)
    }
}