package presentation.screen.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp

@Composable
fun Chair(tableSize: Dp, color: Color) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = Modifier.size(tableSize).background(Color.White)) {
        val sizeWithPx = tableSize.toPx()

        val tableHeight = sizeWithPx * 0.4f
        val tableWidth = sizeWithPx

        val tableX = 0f
        val tableY = sizeWithPx * 0.25f

        val paddingBetweenTableAndChairs = 4f


        val canvasWidth = size.width
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
            color = color,
            size = Size(height = tableHeight, width = tableWidth),
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
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight,
            tableHeight = tableHeight,
            textMeasurer = textMeasurer,
            centerText = "Center",
            topLeftText = "topLeft",
            topRightText = "topRight",
            bottomLeftText = "bottomLeft",
            bottomRightText = "bottomRight"
        )
    }
}

fun DrawScope.drawTopChairs(sizeWithPx: Float, x: Float, y: Float, color: Color) {
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

fun DrawScope.drawBottomChairs(sizeWithPx: Float, x: Float, y: Float, color: Color) {
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

fun DrawScope.drawTextInEachSideOfTable(
    canvasWidth: Float,
    canvasHeight: Float,
    tableHeight: Float,
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
            (canvasWidth - centerTextSize.width) / 2f,
            (canvasHeight - centerTextSize.height) / 2f
        ),
    )

    drawText(
        textMeasurer = textMeasurer,
        text = topLeftText,
        topLeft = Offset(
            4f,
            (canvasHeight - tableHeight) / 2
        ),
    )

    drawText(
        textMeasurer = textMeasurer,
        text = topLeftText,
        topLeft = Offset(
            4f,
            (canvasHeight - tableHeight) / 2
        ),
    )

    val topRightTextLayoutResult = textMeasurer.measure(text = AnnotatedString(topRightText))
    val topRightTextSize = topRightTextLayoutResult.size
    drawText(
        textLayoutResult = topRightTextLayoutResult,
        topLeft = Offset(
            canvasWidth - topRightTextSize.width,
            (canvasHeight - tableHeight) / 2
        ),
    )

    val bottomLeftTextLayoutResult = textMeasurer.measure(text = AnnotatedString(bottomLeftText))
    val bottomLeftTextSize = bottomLeftTextLayoutResult.size
    drawText(
        textLayoutResult = bottomLeftTextLayoutResult,
        topLeft = Offset(
            4f,
            ((canvasHeight - tableHeight) / 2) + (tableHeight) - bottomLeftTextSize.height
        ),
    )

    val bottomRightTextLayoutResult = textMeasurer.measure(text = AnnotatedString(bottomRightText))
    val bottomRightTextSize = bottomRightTextLayoutResult.size
    drawText(
        textLayoutResult = bottomRightTextLayoutResult,
        topLeft = Offset(
            canvasWidth - bottomRightTextSize.width,
            ((canvasHeight - tableHeight) / 2) + (tableHeight) - bottomRightTextSize.height
        ),
    )
}