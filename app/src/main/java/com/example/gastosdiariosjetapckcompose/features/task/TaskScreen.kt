package com.example.gastosdiariosjetapckcompose.features.task

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.domain.model.TaskModel

@Composable
fun TaskScreen(taskViewModel: TaskViewModel, navController: NavController) {
    val showDialog: Boolean by taskViewModel.showDialog.observeAsState(initial = false)
    //se crea un ciclo de vida de la screen
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    //creamos un estado de uiState
    val uiState by produceState<TaskUiState>(
        initialValue = TaskUiState.Loading,
        key1 = lifecycle,
        key2 = taskViewModel
    ) {
        //MIENTRAS ESTE EMPEZADO, QUE NOS CONSUMA DATOS SIEMPRE
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            //value es el uiState de qrriba que declaramos como val
            //al valor actual se lo pondra al uiState, entonces al ultimo valor siempre se lo enviara alli
            taskViewModel.uiState.collect { value = it }
        }
    }
    when (uiState) {
        is TaskUiState.Error -> {}
        TaskUiState.Loading -> {
            CircularProgressIndicator()
        }

        is TaskUiState.Success -> {
            //entonces cuando haya datos sera el unico momento en donde se pinta la pantalla
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Fab(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .zIndex(2f),
                    taskViewModel
                )

                AddTasksDialog(show = showDialog,
                    //funcion para cerrar el dialogo
                    onDissmis = { taskViewModel.onDialogClose() }
                    //funcion donde se envia el valor para crear la tarea
                    , onTaskAdd = { taskViewModel.onTaskCreate(it) }
                )
//TaskList(tasksViewModel)
                //le estamos diciendo que al uiState lo convierta en Access y nos de las tareas
                TaskList((uiState as TaskUiState.Success).tasks, taskViewModel)
            }
        }
    }

}

@Composable
//se le pasa el modelo de data class y el taskviewModel
fun ItemTask(taskModel: TaskModel, taskViewModel: TaskViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            //funcion para controlar los gestos de clicks
            .pointerInput(Unit) {
                //usamos el gesto de boton presionado prolongadamente
                detectTapGestures(onLongPress = {
                    //llama a la funcion àra eliminar el item seleccionado
                    taskViewModel.onItemRemove(taskModel)
                })
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            //taskModel.selected para comprobar si esta seleccionado o no
            checked = taskModel.selected,
            onCheckedChange = { taskViewModel.onCheckBoxSelected(taskModel) },
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Column(Modifier.weight(1f)) {
            //Titulo
            Text(
                text = taskModel.task,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            //Subtitulo
            Text(
                text = "Supporting text",
                color = Color.LightGray
            )
        }
        Text(text = "100+",
            color = Color.LightGray,
            modifier = Modifier.padding(end = 16.dp))

    }
}

//creando recyclerView
@Composable
fun TaskList(task: List<TaskModel>, taskViewModel: TaskViewModel) {
    // se crea una variable para manipular la lista de TaskModel
    LazyColumn {
        items(task, key = { it.id }) { task ->
            ItemTask(taskModel = task, taskViewModel = taskViewModel)

        }
    }
}

@Composable
fun Fab(modifier: Modifier, taskViewModel: TaskViewModel) {
    FloatingActionButton(
        onClick = {
            //mostrar dialogo
            taskViewModel.onShowDialogClick()
        },
        modifier = modifier.zIndex(2f),
        containerColor = Color(0xFF7E57C2)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//onDissmis:()-> es una funcion creada para enviar para que se cierre
//onTaskAdd:(String) -> Unit se crea eso en String para poder enviar el valor
fun AddTasksDialog(show: Boolean, onDissmis: () -> Unit, onTaskAdd: (String) -> Unit) {
    //valor inicial en el editext es vacio
    var myTask by remember { mutableStateOf("") }

    if (show) {
        //onDismissRequest es para que se cierre
        Dialog(onDismissRequest = { onDissmis() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Añade tu tarea",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(16.dp))
                //editex
                TextField(
                    //se rrecupera el valor que se agrega
                    value = myTask,
                    //el valor de lo que se escriba sera guardado en la variable myTask
                    onValueChange = { myTask = it },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = {
                        //mandar tarea
                        onTaskAdd(myTask)
                        //se muestra el editext nuevamente vacio para prepararse para otra tarea
                        myTask = ""
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Añadir tarea")
                }
            }
        }
    }
}

@Composable
fun MyConfirmationDialog(show: Boolean, onDismiss: () -> Unit) {
    if (show) {
        //shape es para definir las esquinas redondeadas

        Dialog(onDismissRequest = { onDismiss() }) {
            Card(shape = MaterialTheme.shapes.small) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    MyTitleDialog(text = "Phone ringtone", modifier = Modifier.padding(24.dp))

                    Divider(Modifier.fillMaxWidth(), color = Color.LightGray)
                    //se alinea a la derecha
                    Row(
                        Modifier
                            .align(Alignment.End)
                            .padding(8.dp)
                    ) {
                        TextButton(onClick = { }) {
                            Text(text = "CANCEL")
                        }
                        TextButton(onClick = { }) {
                            Text(text = "OK")
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun MyTitleDialog(text: String, modifier: Modifier) {
    Text(text = text, modifier = modifier)
}
