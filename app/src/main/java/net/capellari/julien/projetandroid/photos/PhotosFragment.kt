package net.capellari.julien.projetandroid.photos

import android.content.Context
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.capellari.julien.fragments.ListFragment
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.db.Photo
import net.capellari.julien.utils.RecyclerAdapter
import net.capellari.julien.utils.RecyclerHolder
import net.capellari.julien.utils.autoNotify

class PhotosFragment : ListFragment() {
    // Attributs
    private lateinit var data: DataModel
    private var adapter = PhotoAdaptater()

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // View models !
        data = ViewModelProviders.of(requireActivity())[DataModel::class.java]

        arguments?.getLong("match_id")?.let { id ->
            data.allPhotosByMatch(id).observe(this, Observer {
                adapter.items = it
            })
        }
    }

    override fun onRecyclerViewCreated(view: RecyclerView) {
        view.let {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    // Classes
    inner class PhotoAdaptater : RecyclerAdapter<Photo,PhotoHolder>() {
        // Attributs
        override var items: Array<Photo> by autoNotify()

        // Méthodes
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val view = ImageView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

            parent.addView(view)

            return PhotoHolder(view)
        }
    }

    inner class PhotoHolder(view: View): RecyclerHolder<Photo>(view) {
        override fun onBind(value: Photo?) {
            value?.getFile(requireContext())?.let {
                val img = BitmapFactory.decodeFile(it.absolutePath)
                (view as ImageView).setImageBitmap(img)
            }
        }
    }
}