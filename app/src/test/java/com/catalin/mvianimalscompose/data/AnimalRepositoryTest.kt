package com.catalin.mvianimalscompose.data

import com.catalin.mvianimalscompose.data.datasource.api.AnimalApi
import com.catalin.mvianimalscompose.data.repository.AnimalRepositoryImpl
import com.catalin.mvianimalscompose.domain.model.Animal
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AnimalRepositoryTest {

    // Dependencia simulada
    private lateinit var animalApi: AnimalApi

    // Clase bajo prueba
    private lateinit var repository: AnimalRepositoryImpl

    @Before
    fun setUp() {
        // Crear mock de AnimalApi
        animalApi = mock()
        // Inicializar el repositorio con el mock
        repository = AnimalRepositoryImpl(animalApi)
    }

    @Test
    fun `getAnimals should return list from api`() = runTest {
        // Arrange
        val expectedAnimals = listOf(
            Animal(name = "Lion", location = "Europe", image = "lion.jpg"),
            Animal(name = "Tiger", location = "Asia", image = "tiger.jpg")
        )
        Mockito.`when`(animalApi.getAnimals()).thenReturn(expectedAnimals)

        // Act
        val result = repository.getAnimals()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(expectedAnimals, result.getOrNull())
        verify(animalApi).getAnimals()
    }

    @Test
    fun `getAnimals should return failure when api throws exception`() = runTest {
        // Arrange
        val exception = RuntimeException("API error")
        whenever(animalApi.getAnimals()).thenThrow(exception)

        // Act
        val result = repository.getAnimals()

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(animalApi).getAnimals()
    }
}