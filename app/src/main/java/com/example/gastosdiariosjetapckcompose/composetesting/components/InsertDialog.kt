package com.example.gastosdiariosjetapckcompose.composetesting.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarms
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun AristComponent() {
    Column(Modifier.fillMaxSize()) {
        //Modifier.testTag("component1") es como ponerle un id para encontrarlo mas rapido
        Text(text = "ARIS", Modifier.testTag("component1"))
        Text(text = "ARIS", Modifier.testTag("component2"))

        Image(Icons.Default.AccessAlarms, contentDescription = "superImage")
//poniendole el mismo tag a todos, me mostraria a todos
        Text(text = "ARIS", Modifier.testTag("component3"))
        Text(text = "ARIS", Modifier.testTag("component3"))
    }
}