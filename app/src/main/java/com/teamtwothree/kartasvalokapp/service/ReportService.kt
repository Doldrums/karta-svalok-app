package com.teamtwothree.kartasvalokapp.service

import android.net.Uri
import androidx.lifecycle.LiveData
import com.teamtwothree.kartasvalokapp.model.report.Report
import com.teamtwothree.kartasvalokapp.service.common.OperationState

interface ReportService {
    fun newReport(imageUris: List<Uri>): LiveData<OperationState>
    fun getReport(): Report
    fun getState(): LiveData<OperationState>
}