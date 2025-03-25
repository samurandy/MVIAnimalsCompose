package com.catalin.mvianimalscompose.domain.usecase

import android.util.Log
import com.catalin.mvianimalscompose.data.repository.AnimalRepository
import com.catalin.mvianimalscompose.domain.model.Animal
import javax.inject.Inject

class GetAnimalsUseCase @Inject constructor(private val repository: AnimalRepository) {
    suspend operator fun invoke(): Result<List<Animal>> {
        return runCatching {
            val animalList = repository.getAnimals()
            animalList.forEach{ Log.d("Lista de animales: ", it.toString()) }

            animalList

        }
    }
}
