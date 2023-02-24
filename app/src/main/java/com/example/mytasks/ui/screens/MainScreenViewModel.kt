package com.example.mytasks.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasks.data.TaskDao
import com.example.mytasks.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {
    val uiState: StateFlow<TaskUiState> = taskDao.getAll().map { TaskUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskUiState(listOf())
        )

    fun addTask(name: String) {
        viewModelScope.launch {
            taskDao.add(Task(name = name, isFinished = false))
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.update(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }
}

data class TaskUiState(val tasks: List<Task>)