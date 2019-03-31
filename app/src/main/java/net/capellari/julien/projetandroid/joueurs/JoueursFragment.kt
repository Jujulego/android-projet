package net.capellari.julien.projetandroid.joueurs

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_joueur.view.*
import net.capellari.julien.fragments.ListFragment
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Joueur
import net.capellari.julien.utils.*
import org.jetbrains.anko.bundleOf

open class JoueursFragment : ListFragment() {
    // Attributs
    private val adapter = JoueursAdapter()
    protected lateinit var data: DataModel

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // view model !
        data = ViewModelProviders.of(requireActivity())[DataModel::class.java]
        data.allJoueurs().observe(this, adapter.observer)
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
            R.id.action_ajout_match -> { AjoutJoueurDialog().show(childFragmentManager, "dialog"); true }
            else -> false
        }
    }

    open fun onJoueurClick(joueur: Joueur) {
        findNavController().navigate(
                R.id.action_show_joueur,
                bundleOf("joueur_id" to joueur.id)
        )
    }

    // Classes
    inner class JoueursAdapter : RecyclerAdapter<Joueur,JoueurHolder>() {
        // Attributs
        override var items by autoNotify<Joueur>()

        // Méthodes
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoueurHolder {
            return JoueurHolder(parent.inflate(R.layout.item_joueur))
        }
    }

    inner class JoueurHolder(view: View) : RecyclerHolder<Joueur>(view) {
        // Initialisation
        init {
            view.setOnClickListener {
                value?.let {
                    this@JoueursFragment.onJoueurClick(it)
                }
            }
        }

        // Méthodes
        override fun onBind(value: Joueur?) {
            // vues
            view.nom.text = value?.let { getString(R.string.joueur_format_nom, it.prenom, it.nom) } ?: ""
        }

        fun onSwipeOut() {
            value?.let {
                data.delete(it)

                view.snackbar(R.string.joueur_supprime, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.cancel) { _ -> data.insert(it) }
                        .show()
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