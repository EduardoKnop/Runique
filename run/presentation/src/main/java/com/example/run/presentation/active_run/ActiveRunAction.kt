package com.example.run.presentation.active_run

sealed interface ActiveRunAction {
    data object OnToggleClick : ActiveRunAction
    data object OnFinishRunClick : ActiveRunAction
    data object OnResumeRunClick : ActiveRunAction
    data object OnBackClick : ActiveRunAction
}