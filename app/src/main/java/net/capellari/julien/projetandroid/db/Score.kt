package net.capellari.julien.projetandroid.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(
    foreignKeys = [
        ForeignKey(entity = Match::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("match_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(entity = Joueur::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("joueur_id"),
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["match_id"]),
        Index(value = ["joueur_id"])
    ]
)
class Score(@PrimaryKey(autoGenerate = true) var id: Long,
    var score: Int,
    var match_id: Long,
    var joueur_id: Long? = null) {

    // Dao
    @Dao
    interface ScoreDao {
        // Accès
        @Query("select * from Score where match_id = :match order by id")
        fun allByMatch(match: Long): LiveData<Array<Score>>

        @Query("select * from Score where id = :id")
        fun getById(id: Long): LiveData<Score>

        // Modification
        @Insert
        fun insert(vararg scores: Score)

        @Update
        fun update(vararg scores: Score)
    }
}