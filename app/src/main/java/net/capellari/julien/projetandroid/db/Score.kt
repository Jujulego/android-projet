package net.capellari.julien.projetandroid.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = Match::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("match_id")
        ),
        ForeignKey(entity = Joueur::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("joueur_id")
        )
    ]
)
class Score(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var score: Int,
    var match_id: Int,
    var joueur_id: Int
)