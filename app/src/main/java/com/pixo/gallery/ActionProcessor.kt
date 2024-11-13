package com.pixo.gallery

import kotlinx.coroutines.flow.Flow


interface ActionProcessor<Action, Mutation, Event> {
    suspend operator fun invoke(action: Action): Flow<Pair<Mutation?, Event?>>
}