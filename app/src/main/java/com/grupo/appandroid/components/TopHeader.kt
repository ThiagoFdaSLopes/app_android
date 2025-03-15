package com.grupo.appandroid.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appandroid.R
import com.grupo.appandroid.model.Category
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.TextWhite

@Composable
fun TopHeader(
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onResultsPerPageChange: (Int) -> Unit,
    onAreaChange: (String) -> Unit,
    selectedCategory: String,
    selectedLocation: String,
    selectedResultsPerPage: Int,
    selectedArea: String,
    categories: List<Category>,
    locations: List<String>,
    isCompanyLogin: Boolean,
    areas: List<String>
) {
    var searchText by remember { mutableStateOf("") }
    var isCategoryExpanded by remember { mutableStateOf(false) }
    var isLocationExpanded by remember { mutableStateOf(false) }
    var isResultsPerPageExpanded by remember { mutableStateOf(false) }
    var isAreaExpanded by remember { mutableStateOf(false) }

    val resultsPerPageOptions = listOf(5, 10, 20)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AmberPrimary)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            color = TextWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable { isResultsPerPageExpanded = true }
                ) {
                    Text(
                        text = "$selectedResultsPerPage por página",
                        color = Color.Black,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                DropdownMenu(
                    expanded = isResultsPerPageExpanded,
                    onDismissRequest = { isResultsPerPageExpanded = false }
                ) {
                    resultsPerPageOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text("$option por página") },
                            onClick = {
                                isResultsPerPageExpanded = false
                                onResultsPerPageChange(option)
                            }
                        )
                    }
                }
            }
            if (!isCompanyLogin) {

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable { isCategoryExpanded = true }
                    ) {
                        Text(
                            text = if (selectedCategory.isEmpty()) "Categoria"
                            else categories.find { it.tag == selectedCategory }?.label?.let {
                                if (it.length > 15) it.take(12) + "..." else it
                            } ?: "Categoria",
                            color = Color.Black,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    DropdownMenu(
                        expanded = isCategoryExpanded,
                        onDismissRequest = { isCategoryExpanded = false },
                        modifier = Modifier.widthIn(max = 200.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todas") },
                            onClick = {
                                isCategoryExpanded = false
                                onCategoryChange("")
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = category.label,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                onClick = {
                                    isCategoryExpanded = false
                                    onCategoryChange(category.tag)
                                }
                            )
                        }
                    }
                }

            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable { isLocationExpanded = true }
                ) {
                    Text(
                        text = if (selectedLocation.isEmpty()) "Estado"
                        else selectedLocation,
                        color = Color.Black,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                DropdownMenu(
                    expanded = isLocationExpanded,
                    onDismissRequest = { isLocationExpanded = false },
                    modifier = Modifier.widthIn(max = 200.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Todos") },
                        onClick = {
                            isLocationExpanded = false
                            onLocationChange("")
                        }
                    )
                    locations.forEach { state ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = state,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            onClick = {
                                isLocationExpanded = false
                                onLocationChange(state)
                            }
                        )
                    }
                }
            }

            if (isCompanyLogin) {

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable { isAreaExpanded = true }
                    ) {
                        Text(
                            text = if (selectedArea.isEmpty()) "Área" else selectedArea,
                            color = Color.Black,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    DropdownMenu(
                        expanded = isAreaExpanded,
                        onDismissRequest = { isAreaExpanded = false },
                        modifier = Modifier.widthIn(max = 200.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todas") },
                            onClick = {
                                isAreaExpanded = false
                                onAreaChange("")
                            }
                        )
                        areas.forEach { area ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = area,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                onClick = {
                                    isAreaExpanded = false
                                    onAreaChange(area)
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
            modifier = Modifier.fillMaxWidth(),
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