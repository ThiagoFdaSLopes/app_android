// TopHeader.kt
package com.grupo.appandroid.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appandroid.R
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.TextWhite

@Composable
fun TopHeader(
    onSearchChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onModalityChange: (String) -> Unit,
    selectedLocation: String,
    selectedModality: String
) {
    var searchText by remember { mutableStateOf("") }
    var isLocationExpanded by remember { mutableStateOf(false) }
    var isModalityExpanded by remember { mutableStateOf(false) }

    val locations = listOf("Todos", "São Paulo - SP", "Rio de Janeiro - RJ", "Belo Horizonte - MG", "Curitiba - PR", "Porto Alegre - RS")
    val modalities = listOf("Todos", "Remoto", "Presencial", "Híbrido")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AmberPrimary)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable { isLocationExpanded = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (selectedLocation.isEmpty()) stringResource(id = R.string.location_select)
                                else selectedLocation,
                                color = Color.Black,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.pin),
                                contentDescription = "Location",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = isLocationExpanded,
                        onDismissRequest = { isLocationExpanded = false }
                    ) {
                        locations.forEach { location ->
                            DropdownMenuItem(
                                text = { Text(location) },
                                onClick = {
                                    isLocationExpanded = false
                                    onLocationChange(if (location == "Todos") "" else location)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box {
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable { isModalityExpanded = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (selectedModality.isEmpty()) stringResource(id = R.string.modality)
                                else selectedModality,
                                color = Color.Black,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = isModalityExpanded,
                        onDismissRequest = { isModalityExpanded = false }
                    ) {
                        modalities.forEach { modality ->
                            DropdownMenuItem(
                                text = { Text(modality) },
                                onClick = {
                                    isModalityExpanded = false
                                    onModalityChange(if (modality == "Todos") "" else modality)
                                }
                            )
                        }
                    }
                }
            }
        }

        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onSearchChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            placeholder = { Text(stringResource(id = R.string.search)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedTextColor = Color.Black,
            ),
            shape = RoundedCornerShape(4.dp),
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }
}
