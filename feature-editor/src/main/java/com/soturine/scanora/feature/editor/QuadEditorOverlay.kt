package com.soturine.scanora.feature.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
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
    imageBounds: Rect,
    onQuadChange: (DocumentQuad) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strokeColor = MaterialTheme.colorScheme.tertiary
    val handleFill = MaterialTheme.colorScheme.surface
    val handleStroke = MaterialTheme.colorScheme.primary

    val topLeft = imageBounds.toOffset(quad.topLeft)
    val topRight = imageBounds.toOffset(quad.topRight)
    val bottomRight = imageBounds.toOffset(quad.bottomRight)
    val bottomLeft = imageBounds.toOffset(quad.bottomLeft)

    Canvas(modifier = modifier.fillMaxSize()) {
        drawLine(strokeColor, topLeft, topRight, strokeWidth = 6f)
        drawLine(strokeColor, topRight, bottomRight, strokeWidth = 6f)
        drawLine(strokeColor, bottomRight, bottomLeft, strokeWidth = 6f)
        drawLine(strokeColor, bottomLeft, topLeft, strokeWidth = 6f)
        drawCircle(
            color = strokeColor.copy(alpha = 0.2f),
            radius = 18f,
            center = topLeft,
            style = Fill,
        )
        drawCircle(
            color = strokeColor.copy(alpha = 0.2f),
            radius = 18f,
            center = topRight,
            style = Fill,
        )
        drawCircle(
            color = strokeColor.copy(alpha = 0.2f),
            radius = 18f,
            center = bottomRight,
            style = Fill,
        )
        drawCircle(
            color = strokeColor.copy(alpha = 0.2f),
            radius = 18f,
            center = bottomLeft,
            style = Fill,
        )
        drawCircle(strokeColor, radius = 14f, center = topLeft, style = Stroke(4f))
        drawCircle(strokeColor, radius = 14f, center = topRight, style = Stroke(4f))
        drawCircle(strokeColor, radius = 14f, center = bottomRight, style = Stroke(4f))
        drawCircle(strokeColor, radius = 14f, center = bottomLeft, style = Stroke(4f))
    }

    Handle(
        point = quad.topLeft,
        imageBounds = imageBounds,
        fillColor = handleFill,
        strokeColor = handleStroke,
        onMoved = { point -> onQuadChange(quad.copy(topLeft = point)) },
    )
    Handle(
        point = quad.topRight,
        imageBounds = imageBounds,
        fillColor = handleFill,
        strokeColor = handleStroke,
        onMoved = { point -> onQuadChange(quad.copy(topRight = point)) },
    )
    Handle(
        point = quad.bottomRight,
        imageBounds = imageBounds,
        fillColor = handleFill,
        strokeColor = handleStroke,
        onMoved = { point -> onQuadChange(quad.copy(bottomRight = point)) },
    )
    Handle(
        point = quad.bottomLeft,
        imageBounds = imageBounds,
        fillColor = handleFill,
        strokeColor = handleStroke,
        onMoved = { point -> onQuadChange(quad.copy(bottomLeft = point)) },
    )
}

@Composable
private fun Handle(
    point: PointValue,
    imageBounds: Rect,
    fillColor: Color,
    strokeColor: Color,
    onMoved: (PointValue) -> Unit,
) {
    val touchTarget = 48.dp
    val visualSize = 20.dp
    val latestPoint by rememberUpdatedState(point)

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = (imageBounds.left + point.x * imageBounds.width).roundToInt() - touchTarget.roundToPx() / 2,
                    y = (imageBounds.top + point.y * imageBounds.height).roundToInt() - touchTarget.roundToPx() / 2,
                )
            }
            .size(touchTarget)
            .pointerInput(imageBounds) {
                var currentPoint = latestPoint
                detectDragGestures(
                    onDragStart = {
                        currentPoint = latestPoint
                    },
                ) { change, dragAmount ->
                    change.consume()
                    val moved = PointValue(
                        x = (currentPoint.x + dragAmount.x / imageBounds.width).coerceIn(0f, 1f),
                        y = (currentPoint.y + dragAmount.y / imageBounds.height).coerceIn(0f, 1f),
                    )
                    currentPoint = moved
                    onMoved(moved)
                }
            },
    ) {
        Box(
            modifier = Modifier
                .size(visualSize)
                .offset {
                    IntOffset(
                        x = (touchTarget.roundToPx() - visualSize.roundToPx()) / 2,
                        y = (touchTarget.roundToPx() - visualSize.roundToPx()) / 2,
                    )
                }
                .background(fillColor, CircleShape)
                .border(
                    width = 3.dp,
                    color = strokeColor,
                    shape = CircleShape,
                ),
        )
    }
}

private fun Rect.toOffset(point: PointValue): Offset =
    Offset(
        x = left + point.x * width,
        y = top + point.y * height,
    )
