package com.daryeou.app.core.data.di

import com.daryeou.app.core.data.repository.KakaoSearchRepoImpl
import com.daryeou.app.core.domain.repository.KakaoSearchRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindKakaoSearchRepository(
        kakaoSearchRepo: KakaoSearchRepoImpl
    ): KakaoSearchRepo
}