package com.example.mytasks.ui.screens

import com.example.mytasks.MainDispatcherRule
import com.example.mytasks.data.FakeTaskDao
import com.example.mytasks.model.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainScreenViewModelTest {

    private lateinit var fakeTaskDao: FakeTaskDao
    private lateinit var mainScreenViewModel: MainScreenViewModel

    @get:Rule
    val mainDispatcherTestRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        fakeTaskDao = FakeTaskDao()
        val fakeTasks = mutableListOf(
            Task(id = 1, name = "Read first chapter of atomic habits", isFinished = false),
            Task(id = 2, name = "Go Swim", isFinished = true),
            Task(id = 3, name = "1", isFinished = false),
            Task(id = 4, name = "One", isFinished = true),
            Task(id = 5, name = "Two", isFinished = false),
        )
        fakeTasks.forEach() {
            runTest() {
                fakeTaskDao.add(it)
            }
        }
        mainScreenViewModel = MainScreenViewModel(fakeTaskDao)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun mainScreenViewModel_addTask_newTaskAdded() = runTest {
        val dumpTaskName = "Dump task"
        mainScreenViewModel.addTask(dumpTaskName)
        val insertedTask = fakeTaskDao.getAll().first().firstOrNull { it.name == dumpTaskName }
        assert(insertedTask != null)
    }

    @Test
    fun mainScreenViewModel_updateTask_taskIsFinishedChanged() = runTest {
        val dumpTask = mainScreenViewModel.uiState.value.tasks.first()
        mainScreenViewModel.updateTask(dumpTask.copy(isFinished = dumpTask.isFinished.not()))
        val updatedTask = mainScreenViewModel.uiState.value.tasks.first { it.id == dumpTask.id }
        assert(dumpTask.isFinished != updatedTask.isFinished)
    }

    @Test
    fun mainScreenViewModel_deleteTask_taskDeleted() = runTest {
        val dumpTask = fakeTaskDao.getAll().first().first()
        mainScreenViewModel.deleteTask(dumpTask)
        val deletedTask = fakeTaskDao.getAll().first().firstOrNull{ it.id == dumpTask.id}
        assert(deletedTask == null)
    }
}