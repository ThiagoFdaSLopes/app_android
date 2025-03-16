import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appandroid.R
import com.grupo.appandroid.model.FavoriteItem
import com.grupo.appandroid.ui.theme.WhiteBackground

@Composable
fun FavoriteCard(
    favoriteItem: FavoriteItem,
    onCardClick: () -> Unit,
    onHeartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // Margem externa
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(WhiteBackground)
                .clickable { onCardClick() }
                .padding(16.dp)
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
                when (favoriteItem) {
                    is FavoriteItem.CandidateFavorite -> {
                        // Exemplo: exibe informações do candidato
                        Text(
                            text = "Nome do candidato",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Profissão",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        // Adicione outros detalhes do candidato conforme necessário
                    }
                    is FavoriteItem.JobFavorite -> {
                        // Exemplo: exibe informações da vaga
                        Text(
                            text = "Título da vaga",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Nome da empresa",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        // Adicione outros detalhes da vaga conforme necessário
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pin),
                        contentDescription = "Ícone de localização",
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
