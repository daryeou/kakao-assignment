package com.daryeou.app.core.domain.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

sealed class ApiResult<out T> {
    object Progress: ApiResult<Nothing>()
    data class Success<out T>(val value: T): ApiResult<T>()
    data class Error(val exception: Throwable): ApiResult<Nothing>()
}

/**
 * A helper function to wrap the api call in a flow and emit the result
 */
fun <T> safeFlow(apiFunc: suspend () -> T): Flow<ApiResult<T>> = flow {
    emit(ApiResult.Progress)
    try {
        val result = apiFunc.invoke()
        emit(ApiResult.Success(result))
    } catch (exception: Exception) {
        emit(ApiResult.Error(exception = exception))
    }
}.flowOn(Dispatchers.IO)