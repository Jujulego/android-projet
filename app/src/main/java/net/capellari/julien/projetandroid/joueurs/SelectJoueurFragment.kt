package net.capellari.julien.projetandroid.joueurs

import android.content.Context
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import net.capellari.julien.projetandroid.db.Joueur
import net.capellari.julien.projetandroid.db.Score

class SelectJoueurFragment : JoueursFragment() {
    // Attributs
    private var score: Score? = null

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        arguments?.getLong("score_id")?.let {
            data.getScoreById(it).observe(this, Observer {
                score = it
            })
        }
    }
    // Méthodes
    override fun onJoueurClick(joueur: Joueur) {
        score?.let {
            it.joueur_id = joueur.id
            data.update(it)

            findNavController().navigateUp()
        }
    }
}