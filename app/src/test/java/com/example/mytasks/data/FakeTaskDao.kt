package com.example.mytasks.data

import com.example.mytasks.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTaskDao: TaskDao {
    private val tasks = mutableListOf<Task>()
    override fun getAll(): Flow<List<Task>> {
        return flow { emit(tasks) }
    }

    override suspend fun add(task: Task) {
        tasks.add(task)
    }

    override suspend fun update(task: Task) {
        if(tasks.removeIf { it.id == task.id }){
            tasks.add(task)
        }
    }

    override suspend fun delete(task: Task) {
        tasks.removeIf { it.id == task.id }
    }
}