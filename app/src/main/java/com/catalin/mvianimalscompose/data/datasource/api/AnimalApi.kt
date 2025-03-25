package com.catalin.mvianimalscompose.data.datasource.api

import com.catalin.mvianimalscompose.domain.model.Animal
import retrofit2.http.GET

interface AnimalApi {

    @GET("animals.json")
    suspend fun getAnimals(): List<Animal>

}