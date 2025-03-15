package com.grupo.appandroid.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.TextWhite

// Função auxiliar para criar cada ícone e texto
@Composable
fun MenuIcon(icon: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "",
            modifier = Modifier
                .size(80.dp)
//                .clip(CircleShape)
                .background(TextWhite, RoundedCornerShape(10.dp))
                .padding(8.dp)
                .clickable { onClick() }, // adiciona um clique
            contentScale = ContentScale.Crop
        )
        Text(
            text = label,
            color = TextWhite,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}