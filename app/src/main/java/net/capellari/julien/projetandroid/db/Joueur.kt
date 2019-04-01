package net.capellari.julien.projetandroid.db

import androidx.lifecycle.LiveData
import androidx.room.*
import net.capellari.julien.utils.DiffItem

@Entity
data class Joueur(@PrimaryKey(autoGenerate = true) var id: Long,
    var nom: String,
    var prenom: String)
        : DiffItem<Joueur> {

    // Méthodes
    override fun isSameItem(other: Joueur)
        = (id == other.id)

    override fun isSameContent(other: Joueur)
        = (nom == other.nom) && (prenom == other.prenom)

    // Dao
    @Dao
    interface JoueurDao {
        // Accès
        @Query("select * from Joueur order by nom, prenom")
        fun all(): LiveData<Array<Joueur>>

        @Query("select j.* from Joueur j inner join Score _success on j.id = _success.joueur_id where _success.match_id = :match order by _success.id")
        fun allByMatch(match: Long): LiveData<Array<Joueur>>

        @Query("select * from Joueur where id = :id")
        fun getById(id: Long): LiveData<Joueur>

        // Modifications
        @Insert fun insert(joueur: Joueur)
        @Update fun update(joueur: Joueur)
        @Delete fun delete(joueur: Joueur)
    }
}