package com.example.aspiretodo.di

import android.content.Context
import com.example.aspiretodo.data.repository.NetworkRepositoryImpl
import com.example.aspiretodo.domain.repository.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkRepository(
        @ApplicationContext context: Context
    ): NetworkRepository = NetworkRepositoryImpl(context)
}