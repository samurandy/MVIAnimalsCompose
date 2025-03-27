package com.catalin.mvianimalscompose.domain

import com.catalin.mvianimalscompose.data.repository.AnimalRepositoryImpl
import com.catalin.mvianimalscompose.domain.model.Animal
import com.catalin.mvianimalscompose.domain.usecase.GetAnimalsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetAnimalsUseCaseTest {

    private lateinit var repository: AnimalRepositoryImpl
    private lateinit var useCase: GetAnimalsUseCase

    @Before
    fun setUp() {
        repository = mock()
        useCase = GetAnimalsUseCase(repository)
    }

    @Test
    fun `invoke should return success with animal list from repository`() = runTest {
        // Arrange
        val expectedAnimals = listOf(
            Animal(name = "Lion", location = "Europe", image = "lion.jpg"),
            Animal(name = "Tiger", location = "Asia", image = "tiger.jpg")
        )
        doReturn(expectedAnimals).whenever(repository).getAnimals()

        // Act
        val result = useCase()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(expectedAnimals, result.getOrNull())
        verify(repository).getAnimals()
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Repository error")
        Mockito.`when`(repository.getAnimals()).thenReturn(Result.failure(exception))

        // Act
        val result = useCase()
        println("Result: $result")
        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(repository).getAnimals()
    }
}