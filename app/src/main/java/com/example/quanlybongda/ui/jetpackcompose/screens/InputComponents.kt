@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.quanlybongda.ui.theme.Pink80
import com.example.quanlybongda.ui.theme.Purple80
import com.example.quanlybongda.ui.theme.darkCardBackground
import com.example.quanlybongda.ui.theme.darkPurple
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

@Composable
fun AppTopBar(
    title: String,
    scrollBehavior : TopAppBarScrollBehavior,
    navigationIcon: @Composable () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = darkPurple,
            titleContentColor = Color.White,
            scrolledContainerColor = darkPurple,
            navigationIconContentColor = Purple80,
            actionIconContentColor = Purple80,
        ),
        navigationIcon = navigationIcon,
        scrollBehavior = scrollBehavior
    )
}

data class InputError(
    var isError: Boolean = false,
    var errorMessage: String = "",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    label : String,
    value : String,
    onValueChange : (String) -> Unit,
    isError : Boolean = false,
    errorMessage : String = "",
    visualTransformation : VisualTransformation = VisualTransformation.None,
    trailingIcon : @Composable () -> Unit = {},
    leadingIcon : (@Composable () -> Unit)? = null,
    keyboardOption: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    val supportingText : @Composable (() -> Unit) = {
        Text(errorMessage);
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        isError = isError,
        supportingText = if (isError) supportingText else null,
        modifier = modifier
            .fillMaxWidth()
            .background(darkCardBackground, shape = RoundedCornerShape(6.dp)),
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = {
            trailingIcon()
            if (isError)
                Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = Pink80)
        },
        keyboardOptions = keyboardOption,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.LightGray,
            errorTextColor = Pink80,
            errorLabelColor = Pink80,
            errorSupportingTextColor = Pink80,
            errorBorderColor = Pink80,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.LightGray,
        )
    )
}

@Composable
fun InputIntField(
    label : String, // this must be unique
    value : Int?,
    onValueChange : (Int?) -> Unit,
    isError: Boolean = false,
    errorMessage : String = "",
    modifier: Modifier = Modifier
) {
    var valueString by remember(label) { mutableStateOf(value?.toString() ?: "") }
    var formatError by remember(label) { mutableStateOf(false) }
    LaunchedEffect(value) {
        if (valueString.toIntOrNull() != null && value != valueString.toInt())
            valueString = value.toString();
    }
    InputTextField(
        label = label,
        value = valueString,
        onValueChange = {
            valueString = it;
            formatError = false;
            if (valueString.toIntOrNull() == null)
                formatError = true;
            onValueChange(valueString.toIntOrNull());
        },
        isError = isError || formatError,
        errorMessage = if (isError) errorMessage else "Vui lòng nhập vào dữ liệu số nguyên",
        keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

@Composable
fun InputFloatField(
    label : String, // this must be unique
    value : Float?,
    onValueChange : (Float?) -> Unit,
    isError: Boolean = false,
    errorMessage : String = "",
    modifier: Modifier = Modifier
) {
    var valueString by remember(label) { mutableStateOf(value.toString()) }
    var formatError by remember(label) { mutableStateOf(false) }
    LaunchedEffect(value) {
        if (valueString.toFloatOrNull() != null && value != valueString.toFloat())
            valueString = value.toString();
    }
    InputTextField(
        label = label,
        value = valueString,
        onValueChange = {
            valueString = it;
            formatError = false;
            if (valueString.toFloatOrNull() == null)
                formatError = true;
            onValueChange(it.toFloatOrNull());
        },
        isError = isError || formatError,
        errorMessage = if (isError) errorMessage else "Vui lòng nhập vào dữ liệu số thập phân",
        keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

data class OptionValue(
    val value : Int?,
    val label : String,
) {
    companion object {
        val DEFAULT = OptionValue(null, "");
    }
}

@Composable
fun AddFloatingButton(label: String, onClick : () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        modifier = Modifier,
        containerColor = Purple80,
        contentColor = Color.Black
    ) {
        Icon(Icons.Filled.Add, label)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDropDownMenu(
    label : String,
    options : List<OptionValue>,
    selectedOption: OptionValue,
    onOptionSelected: (OptionValue) -> Unit,
    showEmptyError: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.background(darkCardBackground, shape = RoundedCornerShape(6.dp)),
    ) {
        TextField(
            value = if (selectedOption.value == null) "Vui lòng chọn một $label" else selectedOption.label,
            label = { Text(label) },
            onValueChange = {},
            readOnly = true,
            isError = selectedOption.value == null && showEmptyError,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true).fillMaxWidth(), // Required for dropdown to position correctly
            colors = TextFieldDefaults.colors(
                focusedContainerColor = darkCardBackground,
                unfocusedContainerColor = darkCardBackground,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.LightGray,
                cursorColor = Color.White,
                disabledTrailingIconColor = Purple80,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = Purple80,
                unfocusedTrailingIconColor = Purple80,

                errorTextColor = Pink80,
                errorLabelColor = Pink80,
                errorIndicatorColor = Pink80,
                errorContainerColor = darkCardBackground
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
                    text = {  Text(option.label, style = MaterialTheme.typography.bodyLarge) },
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

    LaunchedEffect(visible) {
        if (visible) {
            prevDatePicker.selectedDateMillis = datePickerState.selectedDateMillis;
        }
    }

    LaunchedEffect(value) {
        datePickerState.selectedDateMillis = value;
    }

    TextField(
        value = selectedDate,
        label = { Text(label) },
        onValueChange = {},
        readOnly = true,
        enabled = false,
        trailingIcon = { Icon(
                imageVector = Icons.Default.EditCalendar,
                tint = Purple80,
                contentDescription = "Calendar",
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .background(darkCardBackground, shape = RoundedCornerShape(6.dp))
            .clickable { visible = true },
        colors = TextFieldDefaults.colors(

            disabledPlaceholderColor = darkCardBackground,
            disabledContainerColor = darkCardBackground,
            disabledTextColor = Color.White,
            disabledLabelColor = Color.White,
            cursorColor = Color.White,
            disabledTrailingIconColor = Purple80,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
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

