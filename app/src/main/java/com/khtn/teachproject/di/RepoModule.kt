package com.khtn.teachproject.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.khtn.teachproject.repo.AuthRepo
import com.khtn.teachproject.repo.AuthRepoImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepoModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        /*auth: FirebaseAuth,*/
        appPreferences: SharedPreferences,
    ): AuthRepo {
        return AuthRepoImp(/*auth, */appPreferences)
    }
}