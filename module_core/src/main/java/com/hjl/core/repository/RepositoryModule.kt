package com.hjl.core.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {


    @Provides
    @Singleton
    fun provideMavenRepository() = MavenRepository()
}