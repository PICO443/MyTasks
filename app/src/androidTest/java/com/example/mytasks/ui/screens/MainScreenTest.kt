package com.example.mytasks.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.mytasks.MainActivity
import com.example.mytasks.R
import com.example.mytasks.di.AppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MainScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun mainScreenUi_addEmptyTask_showsError() {
        val errorMessage = composeRule.activity.getString(R.string.empty_task_name_error)
        val textInput = composeRule.activity.getString(R.string.add_new_task)
        val addButton = composeRule.activity.getString(R.string.add)

        composeRule.onNodeWithText(textInput).performTextInput("")
        composeRule.onNodeWithText(addButton).performClick()
        composeRule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun mainScreenUi_addNewTask_taskAdded() {
        val textInput = composeRule.activity.getString(R.string.add_new_task)
        val addButton = composeRule.activity.getString(R.string.add)
        val taskName = "Dumb task"

        composeRule.onNodeWithText(textInput).performTextInput(taskName)
        composeRule.onNodeWithText(addButton).performClick()
        composeRule.onNodeWithText(taskName).assertExists()
    }

    @Test
    fun mainScreenUi_checkTask_taskChecked() {
        val textInput = composeRule.activity.getString(R.string.add_new_task)
        val addButton = composeRule.activity.getString(R.string.add)
        val taskName = "Dumb task"

        composeRule.onNodeWithText(textInput).performTextInput(taskName)
        composeRule.onNodeWithText(addButton).performClick()
        composeRule.onNodeWithTag("TASK_CHECK_BOX").performClick()
        composeRule.onNode(hasTestTag("TASK_CHECK_BOX") and isOn()).assertExists()
    }

    @Test
    fun mainScreenUi_deleteTask_taskDeleted() {
        val textInput = composeRule.activity.getString(R.string.add_new_task)
        val addButton = composeRule.activity.getString(R.string.add)
        val taskName = "Dumb task"

        composeRule.onNodeWithText(textInput).performTextInput(taskName)
        composeRule.onNodeWithText(addButton).performClick()
        composeRule.onNode(hasTestTag("DELETE_BUTTON")).performClick()
        composeRule.onNodeWithText(taskName).assertDoesNotExist()
    }
}