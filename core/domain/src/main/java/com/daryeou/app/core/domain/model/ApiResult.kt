package com.daryeou.app.core.domain.model

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T): ApiResult<T>()
    data class Error(val code: Int, val message: String?): ApiResult<Nothing>()
    data class Exception(val exception: Throwable): ApiResult<Nothing>()
}