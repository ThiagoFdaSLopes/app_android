package com.grupo.appandroid.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appandroid.ui.theme.GrayBackground60
import com.grupo.appandroid.ui.theme.TextWhite

@Composable
fun Dicas(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GrayBackground60)
            .verticalScroll(ScrollState(0))
    ) {
        Text(
            text = "Dicas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            fontSize = 15.sp,
            color = TextWhite,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
