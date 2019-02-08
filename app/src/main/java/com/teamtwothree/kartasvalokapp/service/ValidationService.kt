package com.teamtwothree.kartasvalokapp.service

import android.net.Uri
import androidx.lifecycle.LiveData
import com.teamtwothree.kartasvalokapp.service.common.OperationState

/**
 * Service for remote db interactions. Contain methods to determine if the location
 * is valid to post a report.
 */
interface ValidationService {
    /**
     *  Detects if user location is near sanctioned dump
     *  @return [OperationState] of operation
     */
    fun isUnsanctioned(): LiveData<OperationState>
    /**
     *  Detects if supplied images contain a dump (image recognition implementation)
     *  @param imageUris [List] with image [Uri]s to analyze
     *  @return [OperationState] of operation
     */
    fun imageContainsDump(imageUris: List<Uri>): LiveData<OperationState>
    /**
     *  Detects whether there is an already reported unsanctioned dump on user's location
     *  @return [OperationState] of operation
     */
    fun isNotAlreadyReported(): LiveData<OperationState>
    /**
     *  TESTING implementation: DO NOT USE, will be removed during development
     */
    fun isUnsanctioned(lat: Double, lng: Double): LiveData<OperationState>
    /**
     *  TESTING implementation: DO NOT USE, will be removed during development
     */
    fun isNotAlreadyReported(lat: Double, lng: Double): LiveData<OperationState>

    fun getImageLabels(imageUri: Uri): LiveData<List<String>>
}