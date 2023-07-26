package com.example.apprecordingaudio.viewmodel

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apprecordingaudio.repository.RecordingAuto
import com.example.apprecordingaudio.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class RecordingViewModel @Inject constructor(private val recording : RecordingAuto) : ViewModel() {


    private var _recordStatusLiveData = recording.getStatusRecording()

    val recordStatusLiveData: LiveData<Constants.RecordStatus> = _recordStatusLiveData

    fun setStatusRecord(status: Constants.RecordStatus){
        _recordStatusLiveData.value = status
    }

    fun startRecording(outputFile: File){
        viewModelScope.launch {
            Log.d(Constants.logger, " thread start recording = ${Thread.currentThread().name}")
            recording.startRecording(outputFile)
        }
    }
    fun stopRecording(){
        viewModelScope.launch {
            Log.d(Constants.logger, " thread stop recording = ${Thread.currentThread().name}")

            recording.stopRecording()
        }
    }

    fun playRandom(file: File){
        viewModelScope.launch {
            Log.d(Constants.logger, " thread play record = ${Thread.currentThread().name}")

            recording.playRecord(file)
        }
    }
    fun stopRecord() {

        viewModelScope.launch {
            Log.d(Constants.logger, " thread stop record = ${Thread.currentThread().name}")

            recording.stopRecord()
        }
    }

    override fun onCleared() {
        super.onCleared()

    }
}