package com.grupo.appandroid.views

import android.widget.Button
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.room.Update
import com.grupo.appandroid.R
import com.grupo.appandroid.components.CustomTextField
import com.grupo.appandroid.model.User
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.DarkBackground

@Composable
fun PersonalProfileScreen(user: User){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.people),
                contentDescription = "Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = user.name
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Candidate"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Name",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            CustomTextField(label = stringResource(id = R.string.fullName), value = user.name, onValueChange = { /*OnValueChange*/ })
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Email",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            CustomTextField(label = stringResource(id = R.string.email), keyboardType = KeyboardType.Email, value = user.email, onValueChange = { /*OnValueChange*/ })
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Phone",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            CustomTextField(label = stringResource(id = R.string.phone), keyboardType = KeyboardType.Phone, value = user.phone, onValueChange = { /*OnValueChange*/ })
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Location",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            CustomTextField(label = stringResource(id = R.string.location), value = user.location, onValueChange = { /*OnValueChange*/ })

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = { Update() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmberPrimary
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Save Changes",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
