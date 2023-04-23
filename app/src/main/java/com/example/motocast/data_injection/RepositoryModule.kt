package com.example.motocast.data_injection

import com.example.motocast.data.repository.MotoCastRepositoryImp
import com.example.motocast.domain.repository.MotoCastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMotoCastRepository(
        repository: MotoCastRepositoryImp
    ): MotoCastRepository

}

