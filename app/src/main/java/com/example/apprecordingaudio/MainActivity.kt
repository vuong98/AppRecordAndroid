package com.example.apprecordingaudio

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.apprecordingaudio.databinding.ActivityMainBinding
import com.example.apprecordingaudio.repository.RecordingAudioRepository
import com.example.apprecordingaudio.utils.Constants
import com.example.apprecordingaudio.utils.Constants.logger
import com.example.apprecordingaudio.viewmodel.RecordingViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var viewBinding: ActivityMainBinding?= null

    private val recordingViewModel: RecordingViewModel by viewModels()
    private var filePathRecord: File ?= null
    private var listAudioRecording = mutableListOf<File>();
    @RequiresApi(Build.VERSION_CODES.S)
    private val registerPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
        val allPermission = permissions.all { permission -> permission.value }
        Log.d(logger, "$permissions ")
        if (allPermission){
            Toast.makeText(this,"Da cap quyen truy cap",Toast.LENGTH_SHORT).show()
            recordingViewModel.setStatusRecord(Constants.RecordStatus.StartRecording)
            var  longStartTime = System.currentTimeMillis()/1000
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RECORDINGS),"$longStartTime.m4a").also {
                recordingViewModel.startRecording(it)
                filePathRecord = it
            }
        }else {
            Toast.makeText(this,"Tu choi cap quyen truy cap",Toast.LENGTH_SHORT).show()
        }
    }
    private val permissionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        arrayOf<String>(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,

            Manifest.permission.RECORD_AUDIO
        )
    }else{
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(viewBinding?.root)


        recordingViewModel.recordStatusLiveData.observe(this){
            if (it is Constants.RecordStatus.StartRecording) {
                viewBinding?.ivStatus?.setBackgroundResource(R.drawable.baseline_pause_circle_24)
            }
            else if (it is Constants.RecordStatus.StopRecording){
                viewBinding?.ivStatus?.setBackgroundResource(R.drawable.circle_record_red)
            }
        }

        viewBinding?.apply {

            btnStartRandom.setOnClickListener {

                if (listAudioRecording.size == 0 ) return@setOnClickListener
                val index = Random.nextInt(0,listAudioRecording.size)
                Toast.makeText(this@MainActivity,"file = ${listAudioRecording[index]} - info = ${listAudioRecording[index].totalSpace}", Toast.LENGTH_SHORT).show()

                recordingViewModel.playRandom(listAudioRecording[index])

            }

            btnStop.setOnClickListener {
                Toast.makeText(this@MainActivity,"stop", Toast.LENGTH_SHORT).show()

                recordingViewModel.stopRecord()
            }

            this.btnListFile.setOnClickListener {
                val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RECORDINGS).toString()
                val filePath = File(path)
                val listFires = filePath.listFiles()
                val listFile = listFires.filter { file -> file.name.contains(".m4a")  }
                listAudioRecording.addAll(listFile)

            }

            this.startRecording.setOnClickListener {
                if (recordingViewModel.recordStatusLiveData.value is Constants.RecordStatus.None ||
                    recordingViewModel.recordStatusLiveData.value is Constants.RecordStatus.StopRecording
                ) {
                    registerPermission.launch(permissionList)
                }else {
                    recordingViewModel.setStatusRecord(Constants.RecordStatus.StartRecording)
                    recordingViewModel.stopRecording()
                }
            }
        }
    }
}