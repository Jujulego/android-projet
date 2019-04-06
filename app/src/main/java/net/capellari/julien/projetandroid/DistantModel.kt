package net.capellari.julien.projetandroid

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.FuelJson
import com.github.kittinunf.fuel.json.responseJson
import net.capellari.julien.projetandroid.db.Joueur
import net.capellari.julien.utils.sharedPreference
import net.capellari.julien.utils.success

class DistantModel(app: Application) : AndroidViewModel(app) {
    // Propriétés
    val adresse by sharedPreference("adresse", app, "10.0.2.2")
    val port    by sharedPreference("port", app, "3000")

    val app: Application get() = getApplication()
    val base_url get() = "http://$adresse:$port/api"

    // Méthodes
    fun allJoueurs(): LiveData<Array<Joueur>> {
        val live = MutableLiveData<Array<Joueur>>()

        // Requete
        "$base_url/joueur/all".httpGet()
                .responseJson { _, _, result ->
                    result.fold(
                        success<FuelJson,FuelError> { res ->
                            val array = res.array()
                            var joueurs = arrayOf<Joueur>()

                            for (i in 0 until array.length()) {
                                joueurs += Joueur.fromJson(array.getJSONObject(i))
                            }

                            live.postValue(joueurs)
                        } fail { err ->
                            Log.d("DistantModel", err.message, err)
                        }
                    )
                }

        return live
    }
}