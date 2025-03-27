package com.catalin.mvianimalscompose.domain.usecase

import com.catalin.mvianimalscompose.domain.model.Animal
import javax.inject.Inject

open class GetAnimalsUseCase @Inject constructor(private val repository: com.catalin.mvianimalscompose.domain.AnimalRepository) {
    suspend operator fun invoke(): Result<List<Animal>> {
        return repository.getAnimals()
    }
}