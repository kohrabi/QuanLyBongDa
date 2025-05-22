package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    value : String,
    label : String,
    onValueChange : (String) -> Unit,
    modifier: Modifier = Modifier
) {

    // Username
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF222232), shape = RoundedCornerShape(6.dp)),
//        colors = TextFieldDefaults.colors()
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.White,
//            textColor = Color.White,
            cursorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.LightGray
        )
    )
}

data class OptionValue(
    val value : Int,
    val label : String,
)

@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun InputDropDownMenu(name : String, options : List<OptionValue>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(OptionValue(0, "")) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.background(Color(0xFF222232), shape = RoundedCornerShape(6.dp)),
    ) {
        TextField(
            value = selectedOption.label,
            label = { Text(name) },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(), // Required for dropdown to position correctly
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
//            textColor = Color.White,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.LightGray
            )
        )
        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false },

        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    onClick = { selectedOption = option; expanded = false },
                    text = {  Text(option.label, style = MaterialTheme.typography.body1) },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }

    }

}