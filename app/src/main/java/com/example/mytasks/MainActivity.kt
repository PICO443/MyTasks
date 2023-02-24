package com.example.mytasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mytasks.model.Task
import com.example.mytasks.ui.screens.MainScreenViewModel
import com.example.mytasks.ui.theme.MyTasksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MyTasksTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(topBar = {
                        TopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
                    }) {
                        MainScreen(modifier = Modifier.padding(it))
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
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
    Card(shape = MaterialTheme.shapes.extraSmall) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isFinished,
                onCheckedChange = { onTaskUpdated(task.copy(isFinished = it)) })
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = task.name,
                style = LocalTextStyle.current.copy(textDecoration = if (task.isFinished) TextDecoration.LineThrough else TextDecoration.None)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onDeleteClicked(task) }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.deleteTask),
                    tint = Color.Red.copy(alpha = 0.6f)
                )
            }
        }
    }
}