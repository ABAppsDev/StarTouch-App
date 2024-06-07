package presentation.screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kms
import presentation.screen.composable.modifier.shimmerEffect


@Composable
fun RestaurantTableWithText(
    covers: String,
    openTime: String,
    checksCount: String,
    totalAmount: String,
    tableCode: String,
    hasOrders: Boolean,
    printed: Boolean,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val tableWidth = 220.dp
        val tableHeight = 100.dp
        // Draw table
        SetLayoutDirection(layoutDirection = LayoutDirection.Rtl) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(width = tableWidth, height = tableHeight)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 0.5.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    text = tableCode,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.align(Alignment.Center)
                )
                Text(
                    text = totalAmount,
                    style = TextStyle(fontSize = 10.sp),
                    modifier = Modifier.padding(12.dp).align(Alignment.TopStart)
                )
                Text(
                    text = covers,
                    style = TextStyle(fontSize = 10.sp),
                    modifier = Modifier.padding(12.dp).align(Alignment.TopEnd)
                )
                Text(
                    text = checksCount,
                    style = TextStyle(fontSize = 10.sp),
                    modifier = Modifier.padding(12.dp).align(Alignment.BottomStart)
                )
                Text(
                    text = if (openTime == "null") "" else openTime,
                    style = TextStyle(fontSize = 10.sp),
                    modifier = Modifier.padding(12.dp).align(Alignment.BottomEnd)
                )

                val dotColor =
                    if (printed) Color.Cyan else if (hasOrders) Color.Red else Color.Green
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.kms)
                        .background(color = dotColor, shape = CircleShape)
                        .align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
fun RestaurantTableWithTextLoading(
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val tableWidth = 250.dp
        val tableHeight = 200.dp

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = tableWidth, height = tableHeight)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 0.5.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(16.dp)
                )
                .shimmerEffect()
        ) {
            Text(
                text = "",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = "",
                style = TextStyle(fontSize = 10.sp),
                modifier = Modifier.padding(12.dp).align(Alignment.TopStart)
            )
            Text(
                text = "",
                style = TextStyle(fontSize = 10.sp),
                modifier = Modifier.padding(12.dp).align(Alignment.TopEnd)
            )
            Text(
                text = "",
                style = TextStyle(fontSize = 10.sp),
                modifier = Modifier.padding(12.dp).align(Alignment.BottomStart)
            )
            Text(
                text = "",
                style = TextStyle(fontSize = 10.sp),
                modifier = Modifier.padding(12.dp).align(Alignment.BottomEnd)
            )

            val dotColor = Color.White
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.kms)
                    .background(color = dotColor, shape = CircleShape)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}