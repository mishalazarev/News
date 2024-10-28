package white.ball.news.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import white.ball.news.R
import white.ball.news.domain.util.InternetUtil

@Composable
fun InternetNotWorking(
    getArticlesFromApi: () -> Unit,
) {
    val internetUtil = InternetUtil()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                if(internetUtil.isInternetAvailable(context)) {
                    getArticlesFromApi()
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.internet),
            contentDescription = null
        )

        Text(
            text = "Internet is not working..",
            style = TextStyle(
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier
                .width(200.dp)
                .padding(top = 20.dp)
        )
    }
}