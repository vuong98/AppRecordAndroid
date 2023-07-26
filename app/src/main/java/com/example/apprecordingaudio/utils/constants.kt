package com.example.apprecordingaudio.utils

object Constants {

    const val logger = "VuongDebugDev"

    sealed class RecordStatus{
        object None : RecordStatus()
        object StartRecording: RecordStatus()
        object StopRecording: RecordStatus()
    }
}