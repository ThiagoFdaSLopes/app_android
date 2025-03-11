package com.grupo.appandroid.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.grupo.appandroid.R

@Composable
fun NavigationBar(
    onSettingsClick: () -> Unit,
    onPeopleClick: () -> Unit,
    onBriefcaseClick: () -> Unit,
    onBellClick: () -> Unit,
    onStarClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.settings),
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier
                .weight(1f)
                .size(24.dp)
                .clickable { onSettingsClick() }
        )
        Icon(
            painter = painterResource(id = R.drawable.people),
            contentDescription = "People",
            tint = Color.White,
            modifier = Modifier
                .weight(1f)
                .size(24.dp)
                .clickable { onPeopleClick() }
        )
        Icon(
            painter = painterResource(id = R.drawable.briefcase),
            contentDescription = "Briefcase",
            tint = Color.White,
            modifier = Modifier
                .weight(1f)
                .size(52.dp)
                .clickable { onBriefcaseClick() }
        )
        Icon(
            painter = painterResource(id = R.drawable.bell),
            contentDescription = "Bell",
            tint = Color.White,
            modifier = Modifier
                .weight(1f)
                .size(24.dp)
                .clickable { onBellClick() }
        )
        Icon(
            painter = painterResource(id = R.drawable.star),
            contentDescription = "Star",
            tint = Color.White,
            modifier = Modifier
                .weight(1f)
                .size(24.dp)
                .clickable { onStarClick() }
        )
    }
}
