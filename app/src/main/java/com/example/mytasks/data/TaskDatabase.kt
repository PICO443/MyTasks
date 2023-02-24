package com.example.mytasks.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mytasks.model.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun getUserDao(): TaskDao
}