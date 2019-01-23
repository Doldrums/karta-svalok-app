package com.teamtwothree.kartasvalokapp.service

import android.location.Location
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions
import com.teamtwothree.kartasvalokapp.AppDelegate
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.GeoQueryEventListener
import java.lang.Exception

val KEYWORDS = listOf("junk", "dump", "scrapyard")
const val SANCTIONED = "sanctioned"
const val UNSANCTIONED = "unsanctioned"
//Validation radius in kilometers
const val DETECTION_RADIUS = 0.2

class ValidationServiceImpl : ValidationService {

    private val db = FirebaseFirestore.getInstance()
    private val sanctionedFirestore = GeoFirestore(db.collection(SANCTIONED))
    private val unsanctionedFirestore = GeoFirestore(db.collection(UNSANCTIONED))

    private val options = FirebaseVisionLabelDetectorOptions.Builder()
        .setConfidenceThreshold(0.8f)
        .build()
    private val detector = FirebaseVision.getInstance().getVisionLabelDetector(options)

    override fun isNotAlreadyReported(location: Location): LiveData<ValidationState> =
        detectNearPointsInFirestore(location, unsanctionedFirestore)

    override fun isUnsanctioned(location: Location): LiveData<ValidationState> =
        detectNearPointsInFirestore(location, sanctionedFirestore)

    private fun detectNearPointsInFirestore(location: Location, store: GeoFirestore) : LiveData<ValidationState> {
        val result = MutableLiveData<ValidationState>()
        val geoQuery = store.queryAtLocation(GeoPoint(location.latitude, location.longitude), DETECTION_RADIUS)
        result.postValue(ValidationState.VALIDATING)
        geoQuery.addGeoQueryEventListener(GeoPointListener {
            if (it.isNotEmpty()) result.postValue(ValidationState.FAILED)
            else result.postValue(ValidationState.SUCCESS)
            geoQuery.removeAllListeners()
        })
        return result
    }

    override fun imageContainsDump(imageUris: List<Uri>): LiveData<ValidationState> {
        val result = MutableLiveData<ValidationState>()
        result.postValue(ValidationState.VALIDATING)
        var positives = 0
        var negatives = 0
        imageUris.forEach {
            detector.detectInImage(FirebaseVisionImage.fromFilePath(AppDelegate.applicationContext(), it))
                .addOnSuccessListener {
                    if (hasDump(it.map { it.toString().toLowerCase() })) positives++
                    else negatives++
                    if (positives + negatives == imageUris.size) {
                        if (positives >= imageUris.size/2) result.postValue(ValidationState.SUCCESS)
                        else result.postValue(ValidationState.FAILED)
                    }
                }
                .addOnFailureListener {
                    //TODO: implement error handling
                }
        }
        return result
    }

    private fun hasDump(labels: List<String>) = labels.intersect(KEYWORDS).isNotEmpty()
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
        point?.also { geoPoints.remove(key) }
    }
}