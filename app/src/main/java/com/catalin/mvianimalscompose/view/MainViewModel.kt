package com.catalin.mvianimalscompose.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catalin.mvianimalscompose.domain.usecase.GetAnimalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getAnimalsUseCase: GetAnimalsUseCase) : ViewModel() {

    private val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    var state = mutableStateOf<MainState>(MainState.Idle)
        private set
    private val _effect = MutableSharedFlow<MainEffect>()
    val effect = _effect.asSharedFlow()

    init {
        handleIntent()
    }

    @VisibleForTesting
    fun setStateForTesting(newState: MutableState<MainState>) {
        state = newState
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { collector ->
                when (collector) {
                    is MainIntent.FetchAnimals -> fetchAnimals()
                }
            }
        }
    }

    private fun fetchAnimals() {
        viewModelScope.launch {
            state.value = MainState.Loading

            getAnimalsUseCase().fold(
                onSuccess = { state.value = MainState.Animals(it) },
                onFailure = {
                    _effect.emit(MainEffect.ShowToast(it.message))
                    state.value = MainState.Error(it.message)
                }
            )
        }
    }

    fun sendIntent(intent: MainIntent) {
        viewModelScope.launch {
            userIntent.trySend(intent)
        }
    }
}





