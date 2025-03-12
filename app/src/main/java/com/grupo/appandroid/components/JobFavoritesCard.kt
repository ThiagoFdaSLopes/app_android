import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appandroid.R
import com.grupo.appandroid.ui.theme.WhiteBackground

@Composable
fun JobFavoritesCard(
    onCardClick: () -> Unit,
    onHeartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // Margem externa de 16.dp
        shape = RoundedCornerShape(12.dp), // Bordas levemente arredondadas
        elevation = CardDefaults.cardElevation(4.dp) // Elevação para destacar o card (opcional)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(WhiteBackground)
                .clickable { onCardClick() }
                .padding(16.dp) // Espaçamento interno
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_heart),
                contentDescription = "Ícone de favoritar",
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable { onHeartClick() }
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                // Conteúdo do card permanece o mesmo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.padding(end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "",
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Desenvolvedor Full Stack",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Fandangos LTDA",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = " • Se você é um profissional que gosta de trabalhar com pessoas, é pró-ativo e busca se desenvolver em uma área em constante crescimento, aqui é o seu lugar. #vemserFandangolo",
                            fontSize = 12.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.pin),
                                contentDescription = "Ícone de modalidade local ou remoto",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Local",
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.icon_time),
                                    contentDescription = "Ícone de tempo",
                                    modifier = Modifier.size(20.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = "Tempo",
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun JobFavoritesCardPreview() {
    JobFavoritesCard(
        onCardClick = {},
        onHeartClick = {}
    )
}