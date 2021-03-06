package net.capellari.julien.projetandroid.matchs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.part_match_description.view.*
import kotlinx.android.synthetic.main.part_match_scores.view.*
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Joueur
import net.capellari.julien.projetandroid.db.Score
import net.capellari.julien.utils.findViewsByTag
import org.jetbrains.anko.bundleOf

class MatchFragment : Fragment() {
    // Companion
    companion object {
        const val TAG = "MatchFragment"
    }

    // Attributs
    private lateinit var data: DataModel
    private lateinit var match: MatchModel

    private var playersViews: MutableMap<String,Map<String,View>> = mutableMapOf()

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // view models !
        data  = ViewModelProviders.of(requireActivity())[DataModel::class.java]
        match = ViewModelProviders.of(requireActivity())[MatchModel::class.java]

        arguments?.getLong("match_id")?.let {
            match.setMatchId(it, data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Triage des vues
        for (player in arrayOf("j1", "j2")) {
            val playerViews = mutableMapOf<String,View>()

            for (v in (view as ViewGroup).findViewsByTag(player)) {
                playerViews[v.getTag(R.id.data) as String] = v
            }

            playersViews[player] = playerViews
        }

        // Récupération des données
        match.match?.observe(this, Observer {
            // Remplissage
            view.titre.text = it.titre
            view.description.text = it.description
        })

        match.scores?.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                // Remplissage
                setScore(it[0], "j1")
                setScore(it[1], "j2")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_match, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_photos -> {
                findNavController().navigate(
                        R.id.action_match_photos,
                        bundleOf(
                            "match_id" to match.match_id
                        )
                )
                true
            }
            R.id.action_position -> {
                findNavController().navigate(
                        R.id.action_show_position,
                        bundleOf(
                            "match_id" to match.match_id
                        )
                )
                true
            }
            R.id.action_edit -> { findNavController().navigate(R.id.action_edit_match); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Méthodes
    @Suppress("UNCHECKED_CAST")
    fun <T: View> getPlayerView(player: String, data: String): T? {
        return playersViews[player]?.let { it[data] } as? T
    }

    fun setScore(score: Score, player: String) {
        // Recup joueur
        score.joueur_id?.let {
            data.getJoueur(it).observe(this, Observer { setJoueur(it, player) })
        }

        // Remplissage
        getPlayerView<TextView>(player, "score")?.text = score.score.toString()

        // Events
        getPlayerView<ImageButton>(player, "add")
                ?.setOnClickListener {
                    score.score++
                    data.update(score)

                    getPlayerView<ImageButton>(player, "sub")?.isEnabled = true
                }

        getPlayerView<ImageButton>(player, "sub")
                ?.apply {
                    isEnabled = (score.score != 0)

                    setOnClickListener {
                        if (score.score > 0) {
                            score.score--
                            data.update(score)
                        }

                        if (score.score == 0) {
                            it.isEnabled = false
                        }
                    }
                }

        getPlayerView<TextView>(player, "name")
                ?.setOnClickListener {
                    findNavController().navigate(
                            R.id.action_select_joueur,
                            bundleOf("score_id" to score.id)
                    )
                }
    }

    @SuppressLint("SetTextI18n")
    fun setJoueur(joueur: Joueur, player: String) {
        // Remplissage
        getPlayerView<TextView>(player, "name")?.text = "${joueur.prenom} ${joueur.nom}"
    }
}