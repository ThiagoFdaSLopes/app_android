package com.grupo.appandroid.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.BorderColor
import com.grupo.appandroid.ui.theme.TextGray
import com.grupo.appandroid.ui.theme.TextWhite

@Composable
fun CustomTextField(
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    value: String,
    onValueChange: (String) -> Unit
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = null,
        placeholder = { Text(text = label, color = TextGray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = AmberPrimary,
            unfocusedIndicatorColor = BorderColor,
            cursorColor = AmberPrimary,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun CustomTextArea(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = label, color = TextGray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = AmberPrimary,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite
        ),
        minLines = 4
    )
}