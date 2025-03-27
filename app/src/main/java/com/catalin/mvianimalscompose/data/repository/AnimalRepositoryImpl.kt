package com.catalin.mvianimalscompose.data.repository

import com.catalin.mvianimalscompose.data.datasource.api.AnimalApi
import com.catalin.mvianimalscompose.domain.AnimalRepository
import com.catalin.mvianimalscompose.domain.model.Animal
import javax.inject.Inject

class AnimalRepositoryImpl @Inject constructor(private val api: AnimalApi): AnimalRepository {
    override suspend fun getAnimals(): Result<List<Animal>> {
        return runCatching {
            api.getAnimals()
        }
    }
}