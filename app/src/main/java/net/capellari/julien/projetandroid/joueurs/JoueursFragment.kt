package net.capellari.julien.projetandroid.joueurs

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_joueur.view.*
import net.capellari.julien.fragments.ListFragment
import net.capellari.julien.projetandroid.DataViewModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Joueur
import net.capellari.julien.utils.inflate

class JoueursFragment : ListFragment() {
    // Attributs
    private val adapter = JoueursAdapter()
    private lateinit var data: DataViewModel

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // view model !
        data = ViewModelProviders.of(requireActivity())[DataViewModel::class.java]
        data.allJoueurs().observe(this, Observer {
            adapter.joueurs = it
            adapter.notifyDataSetChanged()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onRecyclerViewCreated(view: RecyclerView) {
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(requireContext())

        val helper = ItemTouchHelper(JoueurTouchCallback())
        helper.attachToRecyclerView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_joueurs, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_ajout_joueur -> { AjoutJoueurDialog().show(childFragmentManager, "dialog"); true }
            else -> false
        }
    }

    // Classes
    inner class JoueursAdapter : RecyclerView.Adapter<JoueurHolder>() {
        // Attributs
        var joueurs = arrayOf<Joueur>()

        // Méthodes
        override fun getItemCount(): Int = joueurs.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoueurHolder {
            return JoueurHolder(parent.inflate(R.layout.item_joueur))
        }

        override fun onBindViewHolder(holder: JoueurHolder, position: Int) {
            holder.bind(joueurs[position])
        }
    }

    inner class JoueurHolder(val view: View) : RecyclerView.ViewHolder(view) {
        // Attributs
        var joueur: Joueur? = null

        // Méthodes
        fun bind(joueur: Joueur) {
            this.joueur = joueur

            // vues
            view.nom.text = getString(R.string.joueur_format_nom, joueur.prenom, joueur.nom)
        }

        fun onSwipeOut() {
            joueur?.let {
                data.delete(it)

                val snackbar = Snackbar.make(view, R.string.joueur_supprime, Snackbar.LENGTH_SHORT)
                snackbar.setAction(getString(R.string.annuler)) { _ -> data.insert(it) }
                snackbar.show()
            }
        }
    }

    inner class JoueurTouchCallback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)
            = makeMovementFlags(0, ItemTouchHelper.END)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

        override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
            if (holder is JoueurHolder) holder.onSwipeOut()
        }
    }
}