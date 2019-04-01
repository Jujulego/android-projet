package net.capellari.julien.projetandroid.matchs

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_match.view.*
import net.capellari.julien.fragments.ListFragment
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Match
import net.capellari.julien.utils.*
import org.jetbrains.anko.bundleOf

class MatchsFragment : ListFragment() {
    // Companion
    companion object {
        const val TAG = "MatchsFragment"
    }

    // Attributs
    private val adapter = MatchsAdapter()
    private lateinit var data: DataModel

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // view model !
        data = ViewModelProviders.of(requireActivity())[DataModel::class.java]
        data.allMatchs().observe(this, adapter.observer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onRecyclerViewCreated(view: RecyclerView) {
        view.adapter = adapter
        view.itemAnimator = DefaultItemAnimator()
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
    inner class MatchsAdapter : RecyclerAdapter<Match,MatchHolder>() {
        // Attributs
        override var items by autoNotify<Match>()

        // Méthodes
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchHolder {
            return MatchHolder(parent.inflate(R.layout.item_match))
        }
    }

    inner class MatchHolder(view: View) : RecyclerHolder<Match>(view), View.OnClickListener {
        // Initialisation
        init {
            view.setOnClickListener(this)
        }

        // Méthodes
        override fun onBind(value: Match?) {
            // Remplissage
            view.titre.text       = value?.titre ?: ""
            view.description.text = value?.description ?: ""

            // Autres données
            if (value != null) {
                data.allScoresByMatch(value).observe(this@MatchsFragment, Observer {
                    if (it != null && it.isNotEmpty()) {
                        view.score_j1.text = it[0].score.toString()
                        view.score_j2.text = it[1].score.toString()
                    }
                })
            } else {
                view.score_j1.text = ""
                view.score_j2.text = ""
            }
        }

        override fun onClick(v: View?) {
            value?.let {
                findNavController().navigate(
                        R.id.action_show_match,
                        bundleOf("match_id" to it.id)
                )
            }
        }

        fun onSwipeOut() {
            value?.let {
                data.delete(it)

                view.snackbar(R.string.deleted, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.cancel) { _ -> data.insert(it) }
                        .show()
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