package com.pixo.gallery

interface Reducer<Mutation, UiState> {
    operator fun invoke(mutation: Mutation, currentState: UiState): UiState
}