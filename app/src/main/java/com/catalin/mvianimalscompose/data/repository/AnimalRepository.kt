package com.catalin.mvianimalscompose.data.repository

import com.catalin.mvianimalscompose.data.datasource.api.AnimalApi
import javax.inject.Inject

class AnimalRepository @Inject constructor(private val api: AnimalApi) {
    suspend fun getAnimals() = api.getAnimals()
}