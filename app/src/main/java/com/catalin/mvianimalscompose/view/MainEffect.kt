package com.catalin.mvianimalscompose.view

sealed class MainEffect {
    data class ShowToast(val message: String?) : MainEffect()
}