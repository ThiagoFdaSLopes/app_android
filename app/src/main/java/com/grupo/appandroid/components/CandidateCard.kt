package com.grupo.appandroid.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appandroid.R
import java.util.Locale

@Composable
fun CandidateCard(
    name: String,
    age: Int,
    location: String,
    area: String,
    experienceTime: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Image and Button
        Column(
            modifier = Modifier
                .padding(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circular profile image
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

            // "VER MAIS" button below the image
            Button(
                onClick = { /* Handle "Ver Mais" click */ },
                modifier = Modifier
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B4513)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.see_more).uppercase(Locale.getDefault()),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Right side: Candidate details
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
                        append("${stringResource(id = R.string.location)}: ")
                    }
                    append(location)
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
        }
    }
}
