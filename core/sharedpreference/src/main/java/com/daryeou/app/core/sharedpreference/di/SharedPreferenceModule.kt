package com.daryeou.app.core.sharedpreference.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceModule {
    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PRIMARY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }
}

const val PRIMARY_SHARED_PREFERENCE = "primary_shared_preference"