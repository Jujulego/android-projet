package net.capellari.julien.projetandroid.matchs

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_edit_match.view.*
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Match
import net.capellari.julien.utils.snackbar

class EditMatchFragment : Fragment() {
    // Attributs
    private lateinit var data: DataModel
    private lateinit var model: MatchModel

    private var match: Match? = null

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // view models !
        data  = ViewModelProviders.of(requireActivity())[DataModel::class.java]
        model = ViewModelProviders.of(requireActivity())[MatchModel::class.java]

        arguments?.getLong("match_id")?.let {
            model.setMatchId(it, data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.match?.observe(this, Observer {
            match = it
            load()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_reload -> { load(true); true }
            R.id.action_save   -> { save();     true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Méthodes
    fun load(msg: Boolean = false) {
        // Get joueur
        match?.let {
            // Remplissage
            view?.titre?.setText(it.titre)
            view?.description?.setText(it.description)
        }

        // Message
        if (msg) {
            view?.snackbar(R.string.reloaded, Snackbar.LENGTH_SHORT)?.show()
        }
    }

    fun save() {
        match?.apply {
            // Récupération des modifs
            view?.let {
                titre       = it.titre.text.toString()
                description = it.description.text.toString()
            }

            // Sauvegarde
            data.update(this)
        }

        // Message
        view?.snackbar(R.string.saved, Snackbar.LENGTH_SHORT)?.show()
    }
}