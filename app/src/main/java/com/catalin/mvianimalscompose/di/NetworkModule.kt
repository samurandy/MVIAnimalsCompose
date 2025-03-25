package com.catalin.mvianimalscompose.di

import com.catalin.mvianimalscompose.BuildConfig
import com.catalin.mvianimalscompose.data.datasource.api.AnimalApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAnimalApi(retrofit: Retrofit): AnimalApi {
        return retrofit.create(AnimalApi::class.java)
    }
}