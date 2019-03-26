package net.capellari.julien.projetandroid.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Joueur(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var nom: String,
    var prenom: String? = null
) {
    // Dao
    @Dao
    interface JoueurDao {
        // Accès
        @Query("select * from Joueur order by nom, prenom")
        fun all(): LiveData<Array<Joueur>>

        @Query("select * from Joueur where id = :id")
        fun getById(id: Int): LiveData<Joueur>

        // Modifications
        @Insert fun insert(joueur: Joueur)
        @Update fun update(joueur: Joueur)
        @Delete fun delete(joueur: Joueur)
    }
}