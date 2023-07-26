package com.example.apprecordingaudio.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.apprecordingaudio.utils.Constants
import java.io.File

interface RecordingAuto {

     fun getStatusRecording(): MutableLiveData<Constants.RecordStatus>
    suspend fun startRecording(outputFile: File)
    suspend fun stopRecording()
    suspend fun playRecord(inputFile: File)
    suspend fun stopRecord()

}