package com.grupo.appandroid.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun TopHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AmberPrimary)
            .padding(16.dp)
    ) {
        // Row for title and filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Title
            Text(
                text = stringResource(id = R.string.app_name),
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            // Filters (Location and Modality)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Location filter with white background
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.location_select),
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

                Spacer(modifier = Modifier.width(8.dp))

                // Modality filter with white background
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.modality),
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        }

        // Search bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.search),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
