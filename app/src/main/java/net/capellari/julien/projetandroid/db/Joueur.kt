package net.capellari.julien.projetandroid.db

import androidx.lifecycle.LiveData
import androidx.room.*
import net.capellari.julien.utils.DiffItem
import org.json.JSONObject
import java.util.*

@Entity(
    indices = [
        Index(value = ["api_id"], unique = true)
    ]
)
data class Joueur(@PrimaryKey(autoGenerate = true) var id: Long,
    var nom: String,
    var prenom: String,
    var api_id: String? = null)
        : DiffItem<Joueur> {

    // Attributs
    var updated_at: Date = Date()

    // Méthodes
    override fun isSameItem(other: Joueur)
        = (id == other.id)

    override fun isSameContent(other: Joueur)
        = (nom == other.nom) && (prenom == other.prenom)

    fun toJson(): JSONObject {
        val json = JSONObject()

        // Remplissage
        json.put("nom",    nom)
        json.put("prenom", prenom)

        if (api_id != null) json.put("_id", api_id)

        return json
    }

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

    // Companion
    companion object {
        // Méthodes
        fun fromJson(json: JSONObject): Joueur {
            // Check content
            assert(json.has("nom"))    { "Missing 'nom' in JSON object" }
            assert(json.has("prenom")) { "Missing 'prenom' in JSON object" }
            assert(json.has("_id"))    { "Missing '_id' in JSON object" }

            // Construct object
            return Joueur(0,
                    json.getString("nom"),
                    json.getString("prenom"),
                    json.getString("_id")
            )
        }
    }
}