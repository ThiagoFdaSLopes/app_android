package com.grupo.appandroid.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.room.Update
import com.grupo.appandroid.R
import com.grupo.appandroid.components.CustomTextField
import com.grupo.appandroid.model.Company
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.DarkBackground

@Composable
fun CompanyProfileScreen(company: Company){

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

            Text(text = company.companyName)

            Spacer(modifier = Modifier.height(5.dp))

            Text(text = "Company")

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Company Name",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            CustomTextField(label = stringResource(id = R.string.company_name), value = company.companyName, onValueChange = { /**/ })
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Corporate Email",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            CustomTextField(label = stringResource(id = R.string.email), keyboardType = KeyboardType.Email, value = company.email, onValueChange = { /**/ })
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "CNPJ",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            CustomTextField(label = stringResource(id = R.string.document), value = company.document, onValueChange = { /**/ })
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Location",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            CustomTextField(label = stringResource(id = R.string.location), value = company.location, onValueChange = { /**/ })
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Sector of activity",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            CustomTextField(label = "label", value = "", onValueChange = {/**/})
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Company size",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            CustomTextField(label = "label", value = "", onValueChange = {/**/})
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "ESG Commitment?",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            Row(

            ) {
                Checkbox(checked = false, onCheckedChange = null)
                Text(
                    text = "Sim"
                )
                Spacer(modifier = Modifier.width(15.dp))

                Checkbox(checked = false, onCheckedChange = null)
                Text(
                    text = "Não"
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

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