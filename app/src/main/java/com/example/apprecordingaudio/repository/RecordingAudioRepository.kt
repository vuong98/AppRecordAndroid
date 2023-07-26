package com.example.apprecordingaudio.repository

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.apprecordingaudio.utils.Constants
import com.example.apprecordingaudio.utils.Constants.logger
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import javax.inject.Inject

class RecordingAudioRepository @Inject constructor(@ApplicationContext private val context: Context) : RecordingAuto {

    private var recorder: MediaRecorder ?= null

    private var mediaPlayer: MediaPlayer ?= null

    private fun createRecorder(): MediaRecorder{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }


    var longStartTime = 0L

    private val _isRecording: MutableLiveData<Constants.RecordStatus>  by lazy { MutableLiveData(Constants.RecordStatus.None) }


    override fun getStatusRecording(): MutableLiveData<Constants.RecordStatus> {
        return _isRecording
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun startRecording(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioChannels(1)
            try {
                setOutputFile(outputFile)
            }catch (e: Exception){
                Log.d(logger, "error = ${e.message}")
            }
            prepare()
            start()
            recorder = this
        }
        _isRecording.postValue(Constants.RecordStatus.StartRecording)
    }

    override suspend fun stopRecording() {

        recorder?.stop()
        recorder?.release()
        _isRecording.postValue(Constants.RecordStatus.StopRecording)


    }

    override suspend fun playRecord(inputFile: File) {

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(inputFile.absolutePath)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }

    override suspend fun stopRecord() {

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}