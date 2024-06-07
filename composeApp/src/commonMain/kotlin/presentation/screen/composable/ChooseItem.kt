package presentation.screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.theme.Theme
import presentation.screen.composable.modifier.bounceClick
import presentation.screen.composable.modifier.shimmerEffect

@Composable
fun ChooseItem(
    title: String,
    price: String? = null,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .clip(shape)
            .bounceClick { onClick() }
            .size(128.dp),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                color = Theme.colors.contentPrimary,
                style = Theme.typography.titleLarge,
            )
            Spacer(Modifier.height(8.dp))
            price?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    color = Theme.colors.contentPrimary,
                    style = Theme.typography.titleLarge,
                )
            }
        }
    }
}

@Composable
fun ChooseItemLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .size(94.dp)
            .background(Color.White)
            .shimmerEffect(),
    )
}

@Composable
fun ChoosePresetLoading(modifier: Modifier = Modifier) {
    Box(modifier.height(260.dp).width(192.dp).bounceClick {}) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp))
                .shimmerEffect(),

            )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .shimmerEffect(),
            )
            Text(
                text = "",
                modifier = Modifier
                    .fillMaxWidth(0.8f).shimmerEffect(),
                color = Theme.colors.contentPrimary,
                textAlign = TextAlign.Center,
                style = Theme.typography.headline,
            )
            Text(
                text = "",
                modifier = Modifier
                    .fillMaxWidth(0.8f).shimmerEffect(),
                color = Theme.colors.contentPrimary,
                textAlign = TextAlign.Center,
                style = Theme.typography.headline,
            )
        }
    }
}