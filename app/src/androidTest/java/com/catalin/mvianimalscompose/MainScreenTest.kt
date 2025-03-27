package com.catalin.mvianimalscompose

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.catalin.mvianimalscompose.domain.AnimalRepository
import com.catalin.mvianimalscompose.domain.model.Animal
import com.catalin.mvianimalscompose.domain.usecase.GetAnimalsUseCase
import com.catalin.mvianimalscompose.ui.theme.MVIAnimalsComposeTheme
import com.catalin.mvianimalscompose.view.MainState
import com.catalin.mvianimalscompose.view.MainViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var mockAnimalRepository: AnimalRepository
    private lateinit var getAnimalsUseCase: GetAnimalsUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        runTest {
            mockAnimalRepository = mock()
            whenever(mockAnimalRepository.getAnimals()).thenReturn(Result.success(emptyList()))
        }

        getAnimalsUseCase = GetAnimalsUseCase(mockAnimalRepository)
        viewModel = MainViewModel(getAnimalsUseCase)
        viewModel.setStateForTesting(mutableStateOf(MainState.Idle))
    }

    @Test
    fun displaysIdleScreenInitially() = runTest {
        // Given
        viewModel.setStateForTesting(mutableStateOf(MainState.Idle))

        // When
        composeTestRule.setContent {
            MVIAnimalsComposeTheme {
                MainScreen(vm = viewModel)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Fetch Animals").assertIsDisplayed()
    }

    @Test
    fun displaysLoadingScreenWhenStateIsLoading() {
        val testState = mutableStateOf<MainState>(MainState.Loading)

        composeTestRule.setContent {
            MVIAnimalsComposeTheme {
                MainScreen(vm = viewModel.apply {
                    setStateForTesting(testState)
                })
            }
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertExists()
    }

    @Test
    fun displaysAnimalsListWhenStateIsAnimals() = runTest {
        // Given
        val animals = listOf(
            Animal(name = "Lion", location = "Europe", image = "lion.jpg"),
            Animal(name = "Tiger", location = "Asia", image = "tiger.jpg")
        )
        viewModel.setStateForTesting(mutableStateOf(MainState.Animals(animals)) )

        // When
        composeTestRule.setContent {
            MVIAnimalsComposeTheme {
                MainScreen(vm = viewModel)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Lion").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tiger").assertIsDisplayed()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun displaysIdleScreenAndTriggersIntentOnButtonClick() = runTest {
        // Given

        // When
        composeTestRule.setContent {
            MVIAnimalsComposeTheme {
                MainScreen(vm = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Fetch Animals").assertExists().performClick()

        // Then
        verify(mockAnimalRepository).getAnimals()
    }

}