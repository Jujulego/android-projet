package net.capellari.julien.projetandroid.matchs

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import net.capellari.julien.projetandroid.DataModel
import net.capellari.julien.projetandroid.LocationModel
import net.capellari.julien.projetandroid.R
import net.capellari.julien.projetandroid.db.Match
import net.capellari.julien.utils.success

class PositionMatchFragment : Fragment(), OnMapReadyCallback {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_position_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Carte
        mapsFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        initMap()
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
            // Affichage de ma position
            location.hasPermissionOrRequest(this, RQ_INIT_MAP) {
                it.isMyLocationEnabled = true
            }

            // Centrage de la carte sur moi
            location.getLastLocation(
                success { pos ->
                    if (marker == null) {
                        it.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        LatLng(pos.latitude, pos.longitude),
                                        DEFAULT_ZOOM
                                )
                        )
                    }
                }
            )

            // Creation du marker
            initMarker()
        }
    }

    fun initMarker() {
        match?.let {
            map?.apply {
                // Création du marker
                marker = addMarker(MarkerOptions().apply {
                    position(it.latlng)
                })

                animateCamera(
                    CameraUpdateFactory.newLatLngZoom(it.latlng, DEFAULT_ZOOM)
                )
            }
        }
    }
}