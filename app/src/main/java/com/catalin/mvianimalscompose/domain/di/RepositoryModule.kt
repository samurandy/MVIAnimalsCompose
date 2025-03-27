package com.catalin.mvianimalscompose.domain.di

import com.catalin.mvianimalscompose.data.datasource.api.AnimalApi
import com.catalin.mvianimalscompose.data.repository.AnimalRepositoryImpl

import com.catalin.mvianimalscompose.domain.AnimalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideAnimalRepository(api: AnimalApi): AnimalRepository {
        return AnimalRepositoryImpl(api) // Tu implementación concreta
    }
}