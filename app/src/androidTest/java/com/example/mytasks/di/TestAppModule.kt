package com.example.mytasks.di

import android.content.Context
import androidx.room.Room
import com.example.mytasks.data.TaskDao
import com.example.mytasks.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): TaskDatabase {
        return Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDao {
        return taskDatabase.getUserDao()
    }
}