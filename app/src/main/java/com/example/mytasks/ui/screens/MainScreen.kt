package com.example.mytasks.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.mytasks.R
import com.example.mytasks.model.Task



@Composable
fun MainScreen(modifier: Modifier = Modifier,viewModel: MainScreenViewModel) {

    val uiState by viewModel.uiState.collectAsState()

    val onTaskUpdated = fun(task: Task) {
        viewModel.updateTask(task)
    }
    val onDeleteClicked = fun(task: Task) {
        viewModel.deleteTask(task)
    }
    Column(modifier = modifier.padding(16.dp)) {
        AddNewTaskSection(onAddTaskClick = { taskName ->
            viewModel.addTask(taskName)
        })
        Spacer(modifier = Modifier.height(16.dp))
        TasksList(
            tasksList = uiState.tasks,
            onTaskUpdated = onTaskUpdated,
            onDeleteClicked = onDeleteClicked
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTaskSection(modifier: Modifier = Modifier, onAddTaskClick: (String) -> Unit) {
    var newTaskName by remember {
        mutableStateOf("")
    }
    var isError by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f),
            value = newTaskName,
            isError = isError,
            supportingText = {
                if (isError)
                    Text(text = stringResource(R.string.empty_task_name_error))
            },
            onValueChange = { newTaskName = it },
            placeholder = { Text(text = stringResource(R.string.add_new_task)) },
            shape = RoundedCornerShape(24.dp)
        )
        Button(onClick = {
            if (newTaskName.isNotEmpty()) {
                onAddTaskClick(newTaskName)
                isError = false
            }
            else
                isError = true
            newTaskName = ""
        }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(id = R.string.add))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stringResource(R.string.add))
        }
    }
}

@Composable
fun TasksList(
    modifier: Modifier = Modifier,
    tasksList: List<Task>,
    onTaskUpdated: (Task) -> Unit,
    onDeleteClicked: (Task) -> Unit
) {
    Surface(modifier = modifier, tonalElevation = 0.dp) {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            item {
                Text(text = stringResource(R.string.tasks), style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
            }
            items(tasksList.filter { it.isFinished.not() }) { task ->
                ListItem(
                    task = task,
                    onTaskUpdated = onTaskUpdated,
                    onDeleteClicked = onDeleteClicked,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Text(text = stringResource(R.string.finished_tasks), style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
            }
            items(tasksList.filter { it.isFinished }) { task ->
                ListItem(
                    task = task,
                    onTaskUpdated = onTaskUpdated,
                    onDeleteClicked = onDeleteClicked,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

        }
    }
}

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    task: Task,
    onTaskUpdated: (Task) -> Unit,
    onDeleteClicked: (Task) -> Unit
) {
    Card(shape = MaterialTheme.shapes.extraSmall, modifier = modifier.testTag("TASK_CARD")) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isFinished,
                onCheckedChange = { onTaskUpdated(task.copy(isFinished = it)) },
                Modifier.testTag("TASK_CHECK_BOX")
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = task.name,
                style = LocalTextStyle.current.copy(textDecoration = if (task.isFinished) TextDecoration.LineThrough else TextDecoration.None)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onDeleteClicked(task) }, modifier = Modifier.testTag("DELETE_BUTTON")) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.add),
                    tint = Color.Red.copy(alpha = 0.6f)
                )
            }
        }
    }
}