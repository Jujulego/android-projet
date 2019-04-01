package net.capellari.julien.projetandroid.photos

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_photo.view.*
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Photo
import net.capellari.julien.utils.snackbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class PhotoFragment : Fragment() {
    // Attributs
    private lateinit var data: DataModel
    private var photo: Photo? = null

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // View models !
        data = ViewModelProviders.of(requireActivity())[DataModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getLong("photo_id")?.let { id ->
            data.getPhoto(id).observe(this, Observer {
                photo = it

                // Show picture
                view.progress.visibility = View.VISIBLE

                doAsync {
                    // Load image
                    val img = BitmapFactory.decodeFile(it.photo)

                    uiThread {
                        view.photo.setImageBitmap(img)
                        view.progress.visibility = View.GONE
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_photo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_delete -> { delete(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Méthodes
    fun delete() {
        // Gardien
        photo?.let {
            data.delete(it)

            // Snackbar
            val callback = object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    // Delete file
                    File(it.photo).delete()

                    // Get back !
                    findNavController().navigateUp()
                }
            }

            view?.snackbar(R.string.deleted, Snackbar.LENGTH_SHORT)?.apply {
                addCallback(callback)
                setAction(R.string.cancel) { _ ->
                    removeCallback(callback)

                    // Restore
                    data.insert(it)
                }

                show()
            }
        }
    }
}