package com.grupo.appandroid.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.BorderColor
import com.grupo.appandroid.ui.theme.TextGray
import com.grupo.appandroid.ui.theme.TextWhite

@Composable
fun RegistrationTabRow(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<TabItem>
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        contentColor = TextWhite,
        divider = { HorizontalDivider(color = BorderColor) },
        indicator = { /* No indicator, we'll use custom one */ }
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = tab.title)
                    }
                },
                selectedContentColor = AmberPrimary,
                unselectedContentColor = TextGray
            )
        }
    }

    // Custom indicator for selected tab
    Row(modifier = Modifier.fillMaxWidth()) {
        tabs.forEachIndexed { index, _ ->
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                thickness = 2.dp,
                color = if (selectedTabIndex == index) AmberPrimary else androidx.compose.ui.graphics.Color.Transparent
            )
        }
    }
}

data class TabItem(
    val title: String,
    val icon: ImageVector
)