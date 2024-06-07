package presentation.screen.composable

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.cart
import abapps_startouch.composeapp.generated.resources.ic_back
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CardEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF53D47)),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(Res.drawable.cart), contentDescription = "")
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "Cart Empty", color = Color.White, style = Theme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Good food is always cooking!\nGo ahead, order some yummy items\nfrom the menu.",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = Theme.typography.title
        )
    }
}