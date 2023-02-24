package com.example.mytasks.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mytasks.R
import com.example.mytasks.ui.screens.MainScreen
import com.example.mytasks.ui.screens.MainScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTasksApp(){
    val viewModel = hiltViewModel<MainScreenViewModel>()
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(topBar = {
            TopAppBar(title = { Text( text = stringResource(id = R.string.app_name))})
        }) {
            MainScreen(modifier = Modifier.padding(it), viewModel = viewModel)
        }
    }
}