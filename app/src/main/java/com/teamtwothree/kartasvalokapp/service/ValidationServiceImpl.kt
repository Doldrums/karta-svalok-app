package com.teamtwothree.kartasvalokapp.service

import android.location.Location
import android.net.Uri
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions
import com.teamtwothree.kartasvalokapp.AppDelegate
import org.kodein.di.Kodein

val KEYWORDS = listOf("junk", "dump", "scrapyard")

class ValidationServiceImpl(private val kodein: Kodein): ValidationService {

    private val options = FirebaseVisionLabelDetectorOptions.Builder()
        .setConfidenceThreshold(0.8f)
        .build()
    private val detector = FirebaseVision.getInstance().getVisionLabelDetector(options)

    override fun isNotAlreadyReported(location: Location): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isUnsanctioned(location: Location): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun imageContainsDump(imageUris: List<Uri>): Boolean {
        var result = false
        imageUris.forEach {
            detector.detectInImage(FirebaseVisionImage.fromFilePath(AppDelegate.applicationContext(), it))
                .addOnSuccessListener {  result = hasDump(it.map { it.toString().toLowerCase() }) }
                .addOnFailureListener {
                    //TODO: implement error handling
                }
        }
        //list.map { KEYWORDS.contains(it.toString().toLowerCase()) }.count { true } > list.size/2
        return result
    }

    private fun hasDump(labels: List<String>) = labels.intersect(KEYWORDS).isNotEmpty()

}