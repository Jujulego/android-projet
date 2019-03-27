package net.capellari.julien.projetandroid.matchs

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_match.view.*
import net.capellari.julien.fragments.ListFragment
import net.capellari.julien.projetandroid.DataViewModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Match
import net.capellari.julien.utils.inflate

class MatchsFragment : ListFragment() {
    // Companion
    companion object {
        const val TAG = "MatchsFragment"
    }

    // Attributs
    private val adapter = MatchsAdapter()
    private lateinit var data: DataViewModel

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // view model !
        data = ViewModelProviders.of(requireActivity())[DataViewModel::class.java]
        data.allMatchs().observe(this, Observer {
            adapter.matchs = it
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

        val helper = ItemTouchHelper(MatchTouchCallback())
        helper.attachToRecyclerView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_matchs, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_ajout_match -> { AjoutMatchDialog().show(childFragmentManager, "dialog"); true }
            else -> false
        }
    }

    // Classes
    inner class MatchsAdapter : RecyclerView.Adapter<MatchHolder>() {
        // Attributs
        var matchs = arrayOf<Match>()

        // Méthodes
        override fun getItemCount(): Int = matchs.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchHolder {
            return MatchHolder(parent.inflate(R.layout.item_match))
        }

        override fun onBindViewHolder(holder: MatchHolder, position: Int) {
            holder.bind(matchs[position])
        }
    }

    inner class MatchHolder(val view: View) : RecyclerView.ViewHolder(view) {
        // Attributs
        var match: Match? = null

        // Méthodes
        fun bind(match: Match) {
            this.match = match

            // vues
            view.titre.text = match.titre
        }

        fun onSwipeOut() {
            match?.let {
                data.delete(it)

                val snackbar = Snackbar.make(view, R.string.match_supprime, Snackbar.LENGTH_SHORT)
                snackbar.setAction(getString(R.string.annuler)) { _ -> data.insert(it) }
                snackbar.show()
            }
        }
    }

    inner class MatchTouchCallback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)
                = makeMovementFlags(0, ItemTouchHelper.END)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

        override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
            if (holder is MatchHolder) holder.onSwipeOut()
        }
    }
}