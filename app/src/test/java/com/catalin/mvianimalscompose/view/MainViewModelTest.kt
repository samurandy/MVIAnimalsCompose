package com.catalin.mvianimalscompose.view

import app.cash.turbine.test
import com.catalin.mvianimalscompose.domain.model.Animal
import com.catalin.mvianimalscompose.domain.usecase.GetAnimalsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.seconds

class MainViewModelTest {

    private lateinit var getAnimalsUseCase: GetAnimalsUseCase
    private lateinit var viewModel: MainViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        getAnimalsUseCase = mock()
        viewModel = MainViewModel(getAnimalsUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchAnimals should update state to Animals on success`() = runTest {
        // Arrange
        val animals = listOf(
            Animal(name = "Lion", location = "Europe", image = "lion.jpg"),
            Animal(name = "Tiger", location = "Asia", image = "tiger.jpg")
        )
        doReturn(Result.success(animals)).whenever(getAnimalsUseCase).invoke()

        // Act
        viewModel.sendIntent(MainIntent.FetchAnimals)

        // Assert
        // No verificamos Loading porque UnconfinedTestDispatcher lo salta
        assertEquals(MainState.Animals(animals), viewModel.state.value)
    }

    @Test
    fun `fetchAnimals should update state to Error and emit ShowToast on failure`() = runTest {
        // Arrange
        val exception = RuntimeException("API error")
        doReturn(Result.failure<List<Animal>>(exception)).whenever(getAnimalsUseCase).invoke()

        // Act


        // Assert
        // No verificamos Loading porque UnconfinedTestDispatcher lo salta


        viewModel.effect.test(timeout = 1.seconds) {
            viewModel.sendIntent(MainIntent.FetchAnimals)// Reducimos el timeout para depuración
            assertEquals(MainState.Error("API error"), viewModel.state.value)
            assertEquals(MainEffect.ShowToast("API error"), awaitItem())
        }
    }
}