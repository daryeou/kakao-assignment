package com.daryeou.app.core.data.di

import com.daryeou.app.core.data.api.KakaoSearchService
import com.daryeou.app.core.network.di.NetworkType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {
    @Provides
    @Singleton
    fun provideKakaoSearchService(
        @Named(NetworkType.KAKAO) retrofit: Retrofit
    ) = retrofit.create<KakaoSearchService>()
}