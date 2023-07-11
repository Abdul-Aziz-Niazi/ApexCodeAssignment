package com.apex.codeassesment.di

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.apex.codeassesment.data.UserRepository
import com.apex.codeassesment.data.UserRepositoryImplementation
import com.apex.codeassesment.data.local.PreferencesManager
import com.apex.codeassesment.data.remote.ApiClient
import com.apex.codeassesment.ui.MainViewModel
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class MainModule {

  companion object {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
      return context.getSharedPreferences("random-user-preferences", Context.MODE_PRIVATE)
    }

    @Provides
    fun providePreferencesManager(sharedPreferences: SharedPreferences): PreferencesManager = PreferencesManager(sharedPreferences)

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun provideApiClient(moshi: Moshi): ApiClient = ApiClient(moshi)

  }

  @Binds
  abstract fun bindViewModelFactory(factory: MainViewModel.Factory): ViewModelProvider.Factory

  @Binds
  abstract fun bindUserRepositoryImplementation(userRepositoryImplementation: UserRepositoryImplementation): UserRepository


}