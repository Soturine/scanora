package com.soturine.scanora.feature.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.soturine.scanora.core.common.model.DocumentQuad
import com.soturine.scanora.core.common.model.PointValue
import kotlin.math.roundToInt

@Composable
fun QuadEditorOverlay(
    quad: DocumentQuad,
    onQuadChange: (DocumentQuad) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val widthPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)
        val heightPx = constraints.maxHeight.toFloat().coerceAtLeast(1f)
        val strokeColor = MaterialTheme.colorScheme.tertiary
        val handleColor = MaterialTheme.colorScheme.primary

        Canvas(modifier = Modifier.fillMaxSize()) {
            val topLeft = Offset(quad.topLeft.x * widthPx, quad.topLeft.y * heightPx)
            val topRight = Offset(quad.topRight.x * widthPx, quad.topRight.y * heightPx)
            val bottomRight = Offset(quad.bottomRight.x * widthPx, quad.bottomRight.y * heightPx)
            val bottomLeft = Offset(quad.bottomLeft.x * widthPx, quad.bottomLeft.y * heightPx)

            drawLine(strokeColor, topLeft, topRight, strokeWidth = 6f)
            drawLine(strokeColor, topRight, bottomRight, strokeWidth = 6f)
            drawLine(strokeColor, bottomRight, bottomLeft, strokeWidth = 6f)
            drawLine(strokeColor, bottomLeft, topLeft, strokeWidth = 6f)
            drawCircle(strokeColor, radius = 10f, center = topLeft, style = Stroke(4f))
            drawCircle(strokeColor, radius = 10f, center = topRight, style = Stroke(4f))
            drawCircle(strokeColor, radius = 10f, center = bottomRight, style = Stroke(4f))
            drawCircle(strokeColor, radius = 10f, center = bottomLeft, style = Stroke(4f))
        }

        Handle(quad.topLeft, widthPx, heightPx, handleColor) { point ->
            onQuadChange(quad.copy(topLeft = point))
        }
        Handle(quad.topRight, widthPx, heightPx, handleColor) { point ->
            onQuadChange(quad.copy(topRight = point))
        }
        Handle(quad.bottomRight, widthPx, heightPx, handleColor) { point ->
            onQuadChange(quad.copy(bottomRight = point))
        }
        Handle(quad.bottomLeft, widthPx, heightPx, handleColor) { point ->
            onQuadChange(quad.copy(bottomLeft = point))
        }
    }
}

@Composable
private fun Handle(
    point: PointValue,
    widthPx: Float,
    heightPx: Float,
    color: androidx.compose.ui.graphics.Color,
    onMoved: (PointValue) -> Unit,
) {
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = (point.x * widthPx).roundToInt() - 14.dp.roundToPx(),
                    y = (point.y * heightPx).roundToInt() - 14.dp.roundToPx(),
                )
            }
            .size(28.dp)
            .background(color = color, shape = CircleShape)
            .pointerInput(widthPx, heightPx) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val moved = PointValue(
                        x = ((point.x * widthPx + dragAmount.x) / widthPx).coerceIn(0.02f, 0.98f),
                        y = ((point.y * heightPx + dragAmount.y) / heightPx).coerceIn(0.02f, 0.98f),
                    )
                    onMoved(moved)
                }
            },
    )
}
