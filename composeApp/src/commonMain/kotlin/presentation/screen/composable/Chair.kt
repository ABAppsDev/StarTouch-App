package presentation.screen.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp

@Composable
fun Chair(
    tableSize: Dp,
    tableColor: Color,
    modifier: Modifier,
    covers: String,
    openTime: String,
    tableCode: String,
    totalAmount: String,
    checksCount: String,
    printed: Boolean,
    hasOrders: Boolean
) {
    val textMeasurer = rememberTextMeasurer()
    val color = if (printed) Color.Cyan else if (hasOrders) Color.Red else Color.Green
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(tableSize)) {
            val sizeWithPx = tableSize.toPx()

            val tableHeight = sizeWithPx * 0.6f

            val paddingBetweenTableAndChairs = 4f

            val canvasHeight = size.height

            drawTopChairs(
                sizeWithPx = sizeWithPx,
                x = sizeWithPx * 0.15f,
                y = ((canvasHeight - tableHeight) / 2) - (sizeWithPx * 0.1f) - paddingBetweenTableAndChairs,
                color = color
            )

            drawTopChairs(
                sizeWithPx = sizeWithPx,
                x = sizeWithPx * 0.6f,
                y = ((canvasHeight - tableHeight) / 2) - (sizeWithPx * 0.1f) - paddingBetweenTableAndChairs,
                color = color
            )

            drawRoundRect(
                topLeft = Offset(x = 0f, y = (canvasHeight - tableHeight) / 2),
                color = tableColor,
                size = Size(height = tableHeight, width = sizeWithPx),
                cornerRadius = CornerRadius(15f, 15f)
            )

            drawBottomChairs(
                sizeWithPx = sizeWithPx,
                x = sizeWithPx * 0.15f,
                y = ((canvasHeight + tableHeight) / 2) + paddingBetweenTableAndChairs,
                color = color
            )

            drawBottomChairs(
                sizeWithPx = sizeWithPx,
                x = sizeWithPx * 0.6f,
                y = ((canvasHeight + tableHeight) / 2) + paddingBetweenTableAndChairs,
                color = color
            )

            drawTextInEachSideOfTable(
                canvasHeight = canvasHeight,
                tableHeight = tableHeight,
                tableWidth = sizeWithPx,
                textMeasurer = textMeasurer,
                centerText = tableCode,
                topLeftText = totalAmount,
                topRightText = covers,
                bottomLeftText = checksCount,
                bottomRightText = if (openTime == "null") "" else openTime
            )
        }
    }
}

private fun DrawScope.drawTopChairs(sizeWithPx: Float, x: Float, y: Float, color: Color) {
    drawRect(
        topLeft = Offset(x = x, y = y),
        color = color,
        size = Size(height = sizeWithPx * 0.1f, width = sizeWithPx * 0.25f),
    )
    drawRoundRect(
        topLeft = Offset(x = x, y = y - 10),
        color = color,
        size = Size(height = sizeWithPx * 0.1f, width = sizeWithPx * 0.25f),
        cornerRadius = CornerRadius(15f, 15f)
    )
}

private fun DrawScope.drawBottomChairs(sizeWithPx: Float, x: Float, y: Float, color: Color) {
    drawRect(
        topLeft = Offset(x = x, y = y),
        color = color,
        size = Size(height = sizeWithPx * 0.1f, width = sizeWithPx * 0.25f),
    )
    drawRoundRect(
        topLeft = Offset(x = x, y = y + 10),
        color = color,
        size = Size(height = sizeWithPx * 0.1f, width = sizeWithPx * 0.25f),
        cornerRadius = CornerRadius(15f, 15f)
    )
}

private fun DrawScope.drawTextInEachSideOfTable(
    canvasHeight: Float,
    tableHeight: Float,
    tableWidth: Float,
    textMeasurer: TextMeasurer,
    centerText: String,
    topLeftText: String,
    topRightText: String,
    bottomLeftText: String,
    bottomRightText: String,
) {
    val centerTextLayoutResult = textMeasurer.measure(text = AnnotatedString(centerText))
    val centerTextSize = centerTextLayoutResult.size
    drawText(
        textLayoutResult = centerTextLayoutResult,
        topLeft = Offset(
            (tableWidth - centerTextSize.width) / 2f,
            (canvasHeight - centerTextSize.height) / 2f
        ),
    )


    drawText(
        textMeasurer = textMeasurer,
        text = topLeftText,
        topLeft = Offset(
            12f,
            ((canvasHeight - tableHeight) / 2) + 8
        ),
    )

    val topRightTextLayoutResult = textMeasurer.measure(text = AnnotatedString(topRightText))
    val topRightTextSize = topRightTextLayoutResult.size
    drawText(
        textLayoutResult = topRightTextLayoutResult,
        topLeft = Offset(
            tableWidth - topRightTextSize.width - 12,
            ((canvasHeight - tableHeight) / 2) + 8
        ),
    )

    val bottomLeftTextLayoutResult = textMeasurer.measure(text = AnnotatedString(bottomLeftText))
    val bottomLeftTextSize = bottomLeftTextLayoutResult.size
    drawText(
        textLayoutResult = bottomLeftTextLayoutResult,
        topLeft = Offset(
            12f,
            (((canvasHeight - tableHeight) / 2) + (tableHeight) - bottomLeftTextSize.height) - 8
        ),
    )

    val bottomRightTextLayoutResult = textMeasurer.measure(text = AnnotatedString(bottomRightText))
    val bottomRightTextSize = bottomRightTextLayoutResult.size
    drawText(
        textLayoutResult = bottomRightTextLayoutResult,
        topLeft = Offset(
            tableWidth - bottomRightTextSize.width - 12,
            (((canvasHeight - tableHeight) / 2) + (tableHeight) - bottomRightTextSize.height) - 8
        ),
    )
}