package com.teamtwothree.kartasvalokapp.service

import android.location.Location
import android.net.Uri

interface ValidationService {
    /**
     *  Detects if Current user location is near sanctioned dump
     *  which might look ugly but not against the law
     *  @param location current user [Location]
     *  @return true if no sanctioned dump in 100 meters, false otherwise
     */
    fun isUnsanctioned(location: Location): Boolean
    /**
     *  Detects if supplied images contain a dump (image recognition implementation)
     *  @param imageUris [List] with image [Uri]s to analyze
     *  @return true if most images contains a dump, false otherwise
     */
    fun imageContainsDump(imageUris: List<Uri>): Boolean

    /**
     *  Detects whether there is an already reported unsanctioned dump on user's location
     *  @param location current user [Location]
     *  @return true if no reports has been posted nearby, false otherwise
     */
    fun isNotAlreadyReported(location: Location): Boolean
}