package com.teamtwothree.kartasvalokapp.service.validation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.teamtwothree.kartasvalokapp.AppDelegate
import com.teamtwothree.kartasvalokapp.service.ValidationService
import com.teamtwothree.kartasvalokapp.service.common.OperationState
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.GeoQueryEventListener
import org.kodein.di.generic.instance
import java.lang.Exception

val KEYWORDS = listOf("Waste", "Litter", "Scrap", "Pollution", "Junk")
const val SANCTIONED = "sanctioned"
const val UNSANCTIONED = "unsanctioned"
const val DETECTION_RADIUS = 0.2

class FirebaseValidationService : ValidationService {


    private val locationProvider: FusedLocationProviderClient by AppDelegate.getKodein().instance()

    private val db = FirebaseFirestore.getInstance()
    private val sanctionedFirestore = GeoFirestore(db.collection(SANCTIONED))
    private val unsanctionedFirestore = GeoFirestore(db.collection(UNSANCTIONED))


    private val options = FirebaseVisionCloudDetectorOptions.Builder()
        .setMaxResults(10)
        .build()
    private val detector = FirebaseVision.getInstance().getVisionCloudLabelDetector(options)

    override fun isNotAlreadyReported(): LiveData<OperationState> =
        detectNearPointsInFirestore(unsanctionedFirestore)

    override fun isNotAlreadyReported(lat: Double, lng: Double): LiveData<OperationState> =
        detectNearPointsInFirestore(lat, lng, unsanctionedFirestore)

    override fun isUnsanctioned(): LiveData<OperationState> =
        detectNearPointsInFirestore(sanctionedFirestore)

    override fun isUnsanctioned(lat: Double, lng: Double): LiveData<OperationState> =
        detectNearPointsInFirestore(lat, lng, sanctionedFirestore)


    private fun detectNearPointsInFirestore(store: GeoFirestore): LiveData<OperationState> {
        val result = MutableLiveData<OperationState>().also { it.postValue(
            OperationState.VALIDATING) }
        if (ContextCompat.checkSelfPermission(AppDelegate.applicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationProvider.lastLocation.addOnSuccessListener {
                val geoQuery = store.queryAtLocation(GeoPoint(it.latitude, it.longitude),
                    DETECTION_RADIUS
                )
                result.postValue(OperationState.VALIDATING)
                geoQuery.addGeoQueryEventListener(GeoPointListener {
                    if (it.isNotEmpty()) result.postValue(OperationState.FAILED)
                    else result.postValue(OperationState.SUCCESS)
                    geoQuery.removeAllListeners()
                })
            }
        }
        return result
    }

    override fun imageContainsDump(imageUris: List<Uri>): LiveData<OperationState> {
        val result = MutableLiveData<OperationState>().also { it.postValue(
            OperationState.VALIDATING) }
        result.postValue(OperationState.VALIDATING)
        var positives = 0
        var negatives = 0
        imageUris.forEach {
            detector.detectInImage(FirebaseVisionImage.fromFilePath(AppDelegate.applicationContext(), it))
                .addOnSuccessListener {
                    if (hasDump(it.map { it.label.toString().toLowerCase() })) positives++
                    else negatives++
                    if (positives + negatives == imageUris.size) {
                        if (positives >= imageUris.size / 2) result.postValue(OperationState.SUCCESS)
                        else result.postValue(OperationState.FAILED)
                    }
                }
                .addOnFailureListener {
                    //TODO: implement error handling
                }
        }
        return result
    }

    private fun hasDump(labels: List<String>) = labels.intersect(KEYWORDS).isNotEmpty()


    private fun detectNearPointsInFirestore(lat: Double, lng: Double, store: GeoFirestore): LiveData<OperationState> {
        val result = MutableLiveData<OperationState>()
        val geoQuery = store.queryAtLocation(GeoPoint(lat, lng),
            DETECTION_RADIUS
        )
        result.postValue(OperationState.VALIDATING)
        geoQuery.addGeoQueryEventListener(GeoPointListener {
            if (it.isNotEmpty()) result.postValue(OperationState.FAILED)
            else result.postValue(OperationState.SUCCESS)
            geoQuery.removeAllListeners()
        })
        return result
    }

    override fun getImageLabels(imageUri: Uri): LiveData<List<String>> {
        val image = FirebaseVisionImage.fromBitmap(BitmapFactory.decodeFile(imageUri.path))
        val result = MutableLiveData<List<String>>().also { it.postValue(emptyList()) }
        detector.detectInImage(image)
            //detector.detectInImage(FirebaseVisionImage.fromFilePath(AppDelegate.applicationContext(), imageUri))
            .addOnSuccessListener {
                result.postValue(it.map { it.label.toString() })
            }
            .addOnFailureListener {
                //TODO: implement error handling
            }
        return result
    }

}

open class GeoPointListener(val onReady: (Map<String, GeoPoint>) -> Unit) : GeoQueryEventListener {

    private val geoPoints = mutableMapOf<String, GeoPoint>()

    override fun onGeoQueryReady() = onReady(geoPoints)
    override fun onKeyMoved(p0: String?, p1: GeoPoint?) {}
    override fun onKeyExited(key: String?) {
        key?.apply { geoPoints.remove(key) }
    }

    override fun onGeoQueryError(p0: Exception?) {
        //TODO: implement error handling
    }

    override fun onKeyEntered(key: String?, point: GeoPoint?) {
        point?.also { key?.also { geoPoints[key] = point } }
    }
}