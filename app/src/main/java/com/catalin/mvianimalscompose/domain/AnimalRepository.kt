package com.catalin.mvianimalscompose.domain

import com.catalin.mvianimalscompose.domain.model.Animal

interface AnimalRepository {
    suspend fun getAnimals(): Result<List<Animal>>
}