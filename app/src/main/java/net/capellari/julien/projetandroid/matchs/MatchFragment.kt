package net.capellari.julien.projetandroid.matchs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_match.view.*
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Match
import net.capellari.julien.projetandroid.db.Score

class MatchFragment : Fragment() {
    // Attributs
    private lateinit var data: DataModel

    private var match: Match? = null
    private var scores: Array<Score>? = null

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // view model !
        data = ViewModelProviders.of(requireActivity())[DataModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getLong("match_id")?.let { id ->
            // Match
            data.getMatch(id)
                    .observe(this, Observer {
                        match = it

                        // Remplissage
                        view.titre.text = it.titre
                    })

            // Scores
            data.allScoresByMatch(id)
                    .observe(this, Observer {
                        scores = it

                        // Remplissage
                        view.score_j1.text = it[0].score.toString()
                        view.score_j2.text = it[1].score.toString()
                    })
        }
    }
}