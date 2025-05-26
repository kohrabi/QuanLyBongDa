@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quanlybongda.ui.theme.Purple40
import com.example.quanlybongda.ui.theme.Purple80
import com.example.quanlybongda.ui.theme.darkCardBackground
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoField
import java.util.Date
import java.util.Locale
import java.util.TimeZone


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
            .background(darkCardBackground, shape = RoundedCornerShape(6.dp)),
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
) {
    companion object {
        val DEFAULT = OptionValue(0, "");
    }
}

@Composable
fun AddFloatingButton(label: String, onClick : () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        modifier = Modifier
    ) {
        Icon(Icons.Filled.Add, label)
    }
}

@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun InputDropDownMenu(
    name : String,
    options : List<OptionValue>,
    selectedOption: OptionValue,
    onOptionSelected: (OptionValue) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.background(darkCardBackground, shape = RoundedCornerShape(6.dp)),
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
                    onClick = { onOptionSelected(option); expanded = false },
                    text = {  Text(option.label, style = MaterialTheme.typography.body1) },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun convertLocalDateTimeToMillis(dateTime: LocalDateTime) : Long {
    return dateTime.toEpochSecond(ZoneOffset.UTC) * 1000 + dateTime.get(ChronoField.MILLI_OF_SECOND);
}

fun convertMillisToLocalDateTime(millis : Long) : LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), TimeZone.getDefault().toZoneId());
}

fun convertMillisToLocalDate(millis : Long) : LocalDate {
    return convertMillisToLocalDateTime(millis).toLocalDate();
}

fun convertLocalDateToMillis(date: LocalDate) : Long {
    return convertLocalDateTimeToMillis(LocalDateTime.of(date, LocalTime.now()));
}

@Composable
fun InputDatePicker(
    label : String,
    value: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val prevDatePicker = rememberDatePickerState()
    val datePickerState = rememberDatePickerState(value)
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

//    OutlinedTextField(
//        value = selectedDate,
//        onValueChange = {},
//        label = { Text(label) },
//        readOnly = true,
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            focusedBorderColor = Color.Transparent,
//            unfocusedBorderColor = Color.Transparent,
//            focusedTextColor = Color.White,
//            cursorColor = Color.White,
//            focusedLabelColor = Color.White,
//            unfocusedLabelColor = Color.LightGray
//        ),
//        modifier = modifier
//            .fillMaxWidth()
//            .background(Color(0xFF222232), shape = RoundedCornerShape(6.dp))
//            .clickable { visible = true },
//    )
    LaunchedEffect(visible) {
        if (visible) {
            prevDatePicker.selectedDateMillis = datePickerState.selectedDateMillis;
        }
    }
    TextField(
        value = selectedDate,
        label = { Text(label) },
        onValueChange = {},
        readOnly = true,
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = visible) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.White,
            cursorColor = Color.White,
            disabledTextColor = Color.LightGray,
            disabledLabelColor = Color.LightGray,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.LightGray
        ),
        enabled = false,
        modifier = modifier
            .fillMaxWidth()
            .background(darkCardBackground, shape = RoundedCornerShape(6.dp))
            .clickable { visible = true },

    )
    if (visible) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    visible = false;
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss();
                    visible = false;
                    datePickerState.selectedDateMillis = prevDatePicker.selectedDateMillis;
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
