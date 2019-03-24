package net.capellari.julien.projetandroid.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Match(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var titre: String,
    var description: String,
    var date: Date
)