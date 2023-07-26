package com.example.apprecordingaudio.di

import com.example.apprecordingaudio.repository.RecordingAudioRepository
import com.example.apprecordingaudio.repository.RecordingAuto
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRecordingAudio(recordingAudioRepository: RecordingAudioRepository): RecordingAuto

}