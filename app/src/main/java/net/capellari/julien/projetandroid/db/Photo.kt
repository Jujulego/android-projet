package net.capellari.julien.projetandroid.db

import androidx.lifecycle.LiveData
import androidx.room.*
import net.capellari.julien.utils.DiffItem
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Match::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("match_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = arrayOf("match_id"))
    ]
)
class Photo(@PrimaryKey(autoGenerate = true) var id: Long,
    var match_id: Long,
    var photo: String,
    var date: Date = Date())
        : DiffItem<Photo> {

    // Méthodes
    override fun isSameItem(other: Photo)
        = (id == other.id)

    override fun isSameContent(other: Photo)
        = (match_id == other.match_id) && (photo == other.photo) && (date == other.date)

    // Dao
    @Dao
    interface PhotoDao {
        // Accès
        @Query("select * from Photo where match_id = :match order by date")
        fun allByMatch(match: Long): LiveData<Array<Photo>>

        @Query("select * from Photo where id = :id")
        fun getById(id: Long): LiveData<Photo>

        // Modification
        @Insert
        fun insert(photo: Photo)

        @Update
        fun update(photo: Photo)

        @Delete
        fun delete(photo: Photo)
    }
}