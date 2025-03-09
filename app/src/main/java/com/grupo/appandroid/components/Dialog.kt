package com.grupo.appandroid.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalDialog(onDismissRequest: () -> Unit,onConfirmation: () -> Unit, showDialog: Boolean, dialogTitle: String, dialogText: String, textButton: String) {
    when {
        showDialog -> {
            AlertDialog(
                onDismissRequest = { onDismissRequest() },
                confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(textButton)
                }
            },
                title = {
                    Text(text = dialogTitle)
                },
                text = {
                    Text(text = dialogText)
                })
        }
    }
}