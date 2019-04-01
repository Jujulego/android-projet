package net.capellari.julien.projetandroid.photos

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_photo.view.*
import net.capellari.julien.fragments.ListFragment
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Photo
import net.capellari.julien.utils.RecyclerAdapter
import net.capellari.julien.utils.RecyclerHolder
import net.capellari.julien.utils.autoNotify
import net.capellari.julien.utils.inflate
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PhotosFragment : ListFragment() {
    // Companion
    companion object {
        // Constantes
        const val TAG = "PhotosFragment"
        const val THUMBNAIL_SIZE = 1024

        const val REQUEST_TAKE_PICTURE = 1
    }

    // Attributs
    private lateinit var data: DataModel
    private var adapter = PhotoAdaptater()

    private var file: String? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore
        file = savedInstanceState?.getString("file")

        // Peut prendre des photo ?
        if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            setHasOptionsMenu(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_photos, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_take_photo -> { takePicture(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRecyclerViewCreated(view: RecyclerView) {
        view.let {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(
                    requireContext(), resources.getInteger(R.integer.columns)
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_TAKE_PICTURE -> {
                if (resultCode == RESULT_OK) {
                    addPhoto()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("file", file)
    }

    // Méthodes
    @Throws(IOException::class)
    fun createImgFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val dir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_$timestamp", ".jpg", dir)
                .also { file = it.absolutePath }
    }

    fun takePicture() {
        context?.let { ctx ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            intent.resolveActivity(ctx.packageManager)?.let {
                // Create file
                val file = createImgFile()
                val uri = FileProvider.getUriForFile(ctx, "com.example.android.fileprovider", file)

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(intent, REQUEST_TAKE_PICTURE)
            }
        }
    }

    fun addPhoto() {
        Log.d(TAG, "add photo $file to ${arguments?.getLong("match_id")}")
        arguments?.getLong("match_id")?.let { id ->
            data.insert(Photo(0, id, file!!))
        }
    }

    // Classes
    inner class PhotoAdaptater : RecyclerAdapter<Photo,PhotoHolder>() {
        // Attributs
        override var items: Array<Photo> by autoNotify()

        // Méthodes
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            return PhotoHolder(parent.inflate(R.layout.item_photo, false))
        }
    }

    inner class PhotoHolder(view: View): RecyclerHolder<Photo>(view) {
        // Initialisation
        init {
            view.setOnClickListener {
                value?.let {
                    findNavController().navigate(
                        R.id.action_show_photo,
                        bundleOf("photo_id" to it.id)
                    )
                }
            }
        }

        // Méthodes
        override fun onBind(value: Photo?) {
            value?.photo.let {
                view.progress.visibility = View.VISIBLE

                doAsync {
                    val img = BitmapFactory.decodeFile(it)
                    val thumbnail = ThumbnailUtils.extractThumbnail(img, THUMBNAIL_SIZE, THUMBNAIL_SIZE)

                    uiThread {
                        view.photo.setImageBitmap(thumbnail)
                        view.progress.visibility = View.GONE
                    }
                }
            }
        }
    }
}