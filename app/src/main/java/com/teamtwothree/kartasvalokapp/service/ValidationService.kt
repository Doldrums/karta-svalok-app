package com.teamtwothree.kartasvalokapp.service

import android.location.Location
import android.net.Uri
import androidx.lifecycle.LiveData

interface ValidationService {
    /**
     *  Detects if Current user location is near sanctioned dump
     *  which might look ugly but not against the law
     *  @param location current user [Location]
     *  @return [ValidationState] of operation
     */
    fun isUnsanctioned(location: Location): LiveData<ValidationState>
    /**
     *  Detects if supplied images contain a dump (image recognition implementation)
     *  @param imageUris [List] with image [Uri]s to analyze
     *  @return [ValidationState] of operation
     */
    fun imageContainsDump(imageUris: List<Uri>): LiveData<ValidationState>

    /**
     *  Detects whether there is an already reported unsanctioned dump on user's location
     *  @param location current user [Location]
     *  @return [ValidationState] of operation
     */
    fun isNotAlreadyReported(location: Location): LiveData<ValidationState>
}