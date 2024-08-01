package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import com.example.gastosdiariosjetapckcompose.domain.model.CategoriesModel
import com.example.gastosdiariosjetapckcompose.domain.model.categoriaDefault
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesGastos
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesIngresos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun menuDesplegable(
    isChecked: Boolean,
    modifier: Modifier,
): CategoriesModel {
    val categories: List<CategoriesModel> by remember(isChecked) {
        mutableStateOf(
            if (isChecked) {
                categoriaDefault + categoriesIngresos.sortedBy { it.name }
            } else {
                categoriaDefault + categoriesGastos.sortedBy { it.name }
            }
        )
    }

    var expanded by remember { mutableStateOf(false) }

    var selectedItem by remember { mutableStateOf(categories.first()) }

    val  colorIcon =  if(selectedItem == categories.first()){
        Color.Transparent
    }else{
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        Alignment.Center
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            TextField(
                value = selectedItem.name,
                onValueChange = {},
                leadingIcon = {
                    Image(
                        painter = painterResource(id = selectedItem.icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colorIcon)
                    )

                },
                readOnly = true,
                trailingIcon = {
                    IconButton(
                        onClick = { expanded != expanded },
                        modifier = Modifier.clearAndSetSemantics { }) {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            "Trailing icon for exposed dropdown menu",
                            Modifier.rotate(
                                if (expanded)
                                    180f
                                else
                                    360f
                            ), tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    //  ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ItemMenuDesplegable(
                    categories = categories,
                    onSelectedItem = { selectedItem = it }) { it ->
                    expanded = it
                }

            }
        }
    }
    return selectedItem
}

@Composable
fun ItemMenuDesplegable(
    categories: List<CategoriesModel>,
    onSelectedItem: (CategoriesModel) -> Unit,
    onExpanded: (Boolean) -> Unit
) {
    Column {
        categories.forEachIndexed { index,itemCategory ->
            val  colorIcon =  if(index == 0){
                Color.Transparent
            }else{
                MaterialTheme.colorScheme.onSurfaceVariant
            }
            DropdownMenuItem(
                text = { Text(text = itemCategory.name) },
                onClick = {
                    onSelectedItem(itemCategory)
                    onExpanded(false)
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = itemCategory.icon),
                        contentDescription = null,
                        modifier = Modifier.size(
                            24.dp
                        ),
                        colorFilter = ColorFilter.tint(colorIcon)
                    )
                }
            )
        }
    }
}