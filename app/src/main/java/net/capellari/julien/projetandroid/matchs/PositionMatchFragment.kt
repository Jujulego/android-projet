package net.capellari.julien.projetandroid.matchs

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.LocationModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Match
import net.capellari.julien.utils.snackbar
import net.capellari.julien.utils.success

class PositionMatchFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    // Companion
    companion object {
        // Constantes
        const val DEFAULT_ZOOM = 15f

        const val RQ_INIT_MAP = 1
    }

    // Attributs
    private lateinit var data: DataModel
    private lateinit var location: LocationModel

    private var map: GoogleMap? = null
    private var marker: Marker? = null
    private var match: Match?   = null
    private var me: LatLng?     = null

    // Propriétés
    private val mapsFragment get() = childFragmentManager.findFragmentById(R.id.carte) as SupportMapFragment

    // Events
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // View models
        data     = ViewModelProviders.of(requireActivity())[DataModel::class.java]
        location = ViewModelProviders.of(requireActivity())[LocationModel::class.java]

        arguments?.getLong("match_id")?.let { id ->
            data.getMatch(id).observe(this, Observer {
                match = it
                initMarker()
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_position_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Carte
        mapsFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_reload -> { initMarker(true); true }
            R.id.action_save   -> { save();           true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        initMap()
    }

    override fun onMapClick(pos: LatLng) {
        map?.apply {
            // Suppression du marker
            marker?.remove()

            // Nouveau marker
            marker = addMarker(MarkerOptions().apply {
                position(pos)
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RQ_INIT_MAP -> {
                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initMap()
                }
            }
        }
    }

    // Méthodes
    @SuppressLint("MissingPermission")
    fun initMap() {
        map?.let {
            it.setOnMapClickListener(this)

            // Affichage de ma position
            location.hasPermissionOrRequest(this, RQ_INIT_MAP) {
                it.isMyLocationEnabled = true
            }

            // Centrage de la carte sur moi
            location.getLastLocation(
                success { pos ->
                    me = LatLng(pos.latitude, pos.longitude)

                    if (marker == null) {
                        it.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(me, DEFAULT_ZOOM)
                        )
                    }
                }
            )

            // Creation du marker
            initMarker()
        }
    }

    fun initMarker(msg: Boolean = false) {
        match?.let {
            map?.apply {
                // Remove marker
                marker?.remove()
                marker = null

                // Création du marker
                if (it.latitude != .0 || it.longitude != .0) {
                    marker = addMarker(MarkerOptions().apply {
                        position(it.latlng)
                    })

                    animateCamera(
                        CameraUpdateFactory.newLatLngZoom(it.latlng, DEFAULT_ZOOM)
                    )
                } else if (me != null) {
                    animateCamera(
                        CameraUpdateFactory.newLatLngZoom(me, DEFAULT_ZOOM)
                    )
                }

                // Snackbar
                if (msg) {
                    view?.snackbar(R.string.reloaded, Snackbar.LENGTH_SHORT)?.show()
                }
            }
        }
    }

    fun save() {
        match?.let {
            // Position du marker
            val pos = marker?.position
            pos ?: return

            // Update
            it.latitude  = pos.latitude
            it.longitude = pos.longitude

            data.update(it)

            view?.snackbar(R.string.saved, Snackbar.LENGTH_SHORT)?.show()
        }
    }
}