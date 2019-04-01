package net.capellari.julien.projetandroid

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.LocationServices
import net.capellari.julien.utils.SuccessFail
import net.capellari.julien.utils.success
import java.lang.Exception

class LocationModel(app: Application) : AndroidViewModel(app) {
    // Attributs
    private var locationService = LocationServices.getFusedLocationProviderClient(app)

    // Méthodes
    fun hasPermission(cb: SuccessFail<Unit,Unit>) {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            cb._success(Unit)
        } else {
            cb._fail(Unit)
        }
    }

    fun hasPermissionOrRequest(activity: Activity, requestCode: Int, cb: () -> Unit) {
        hasPermission(
            success {
                cb()
            } fail {
                activity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
            }
        )
    }

    fun hasPermissionOrRequest(fragment: Fragment, requestCode: Int, cb: () -> Unit) {
        hasPermission(
            success {
                cb()
            } fail {
                fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
            }
        )
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(cb: SuccessFail<Location,Exception?>) {
        hasPermission(
            success {
                locationService.lastLocation
                    .addOnSuccessListener(cb._success)
                    .addOnFailureListener(cb._fail)
            } fail {
                cb._fail(null)
            }
        )
    }

    @SuppressLint("MissingPermission")
    fun getLastLocationOrRequest(activity: Activity, requestCode: Int, cb: SuccessFail<Location,Exception>) {
        hasPermissionOrRequest(activity, requestCode) {
            locationService.lastLocation
                    .addOnSuccessListener(cb._success)
                    .addOnFailureListener(cb._fail)
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocationOrRequest(fragment: Fragment, requestCode: Int, cb: SuccessFail<Location, Exception?>) {
        hasPermissionOrRequest(fragment, requestCode) {
            locationService.lastLocation
                    .addOnSuccessListener(cb._success)
                    .addOnFailureListener(cb._fail)
        }
    }
}