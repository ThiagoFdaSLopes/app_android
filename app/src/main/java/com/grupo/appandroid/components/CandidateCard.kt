package com.grupo.appandroid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appandroid.R

@Composable
fun CandidateCard(
    name: String,
    age: Int,
    location: String,
    area: String,
    experienceTime: String,
    isCompanyLogin: Boolean,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                        .padding(16.dp),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${stringResource(id = R.string.name_candidate_card)}: ")
                        }
                        append(name)
                    },
                    fontSize = 16.sp
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${stringResource(id = R.string.age_candidate_card)}: ")
                        }
                        append(age.toString())
                    },
                    fontSize = 14.sp
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${stringResource(id = R.string.area_candidate_card)}: ")
                        }
                        append(area)
                    },
                    fontSize = 14.sp
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${stringResource(id = R.string.experience_candidate_card)}: ")
                        }
                        append(experienceTime)
                    },
                    fontSize = 14.sp
                )

                if (isCompanyLogin) {
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.pin),
                                contentDescription = "Modalidade",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Gray
                            )
                            Text(
                                text = location,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.briefcase),
                                contentDescription = "Tempo",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Gray
                            )
                            Text(
                                text = experienceTime,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        Icon(
            painter = painterResource(
                id = if (isFavorite) R.drawable.heart_fill else R.drawable.icon_heart
            ),
            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .clickable { onFavoriteClick() },
            tint = if (isFavorite) Color.Red else Color.Gray
        )
    }
}