package net.capellari.julien.projetandroid.joueurs

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_joueur.view.*
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Joueur
import net.capellari.julien.utils.snackbar

class JoueurFragment : Fragment() {
    // Attributs
    private lateinit var data: DataModel
    private var joueur: Joueur? = null

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // View models
        data = ViewModelProviders.of(requireActivity())[DataModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_joueur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        load()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_joueur, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_reload -> { load(true); true }
            R.id.action_save   -> { save(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Méthodes
    fun load(msg: Boolean = false) {
        // Get joueur
        arguments?.getLong("joueur_id")?.let { id ->
            data.getJoueur(id).observe(this, Observer {
                joueur = it

                // Remplissage
                view?.prenom?.setText(it.prenom)
                view?.nom?.setText(it.nom)
            })
        }

        // Message
        if (msg) {
            view?.snackbar(R.string.reloaded, Snackbar.LENGTH_SHORT)?.show()
        }
    }

    fun save() {
        joueur?.apply {
            // Récupération des modifs
            view?.let {
                prenom = it.prenom.text.toString()
                nom    = it.nom.text.toString()
            }

            // Sauvegarde
            data.update(this)
        }

        // Message
        view?.snackbar(R.string.saved, Snackbar.LENGTH_SHORT)?.show()
    }
}