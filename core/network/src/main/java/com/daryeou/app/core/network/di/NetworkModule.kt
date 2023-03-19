package com.daryeou.app.core.network.di

import com.daryeou.app.core.network.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    @Named(NetworkType.KAKAO)
    fun provideRetrofit(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.KAKAO_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonDate))
            .addConverterFactory(
                @OptIn(ExperimentalSerializationApi::class)
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .client(provideOkHttpClient(InterceptorForKakao()))
            .build()

    // e.g. 2011-09-14T01:39:00.000+09:00
    private val gsonDate: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        .create()

    private val networkJson: Json = Json {
        ignoreUnknownKeys = true
    }

    private fun provideOkHttpClient(interceptor: Interceptor? = null): OkHttpClient =
        OkHttpClient.Builder().run {
            interceptor?.also { interceptor ->
                addInterceptor(interceptor)
            }
            build()
        }

    private class InterceptorForKakao : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", BuildConfig.KAKAO_API_KEY)
                .build()
            proceed(newRequest)
        }
    }
}

object NetworkType {
    const val KAKAO = "kakao"
}