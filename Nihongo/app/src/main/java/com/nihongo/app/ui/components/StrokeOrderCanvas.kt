package com.nihongo.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Interactive Japanese Calligraphy & Stroke-Order Writing Pad.
 * Features a traditional Genkouyoushi 4-quadrant practice box, watermark trace guide,
 * verified stroke count indicator, and brush stroke recording.
 */
@Composable
fun StrokeOrderCanvas(
    character: String,
    verifiedStrokeCount: Int,
    strokeDirectionsBn: String,
    strokeDirectionsEn: String,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val completedStrokes = remember { mutableStateListOf<List<Offset>>() }
    var activeStrokePoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var showTraceGuide by remember { mutableStateOf(true) }

    // Clear strokes whenever the user switches to a different character
    LaunchedEffect(character) {
        completedStrokes.clear()
        activeStrokePoints = emptyList()
    }

    val inkGreenColor = NihongoTheme.colors.inkGreen
    val phoenixRedColor = NihongoTheme.colors.phoenixFlowerRed
    val gridColor = NihongoTheme.colors.borderOutline.copy(alpha = 0.55f)
    val guideTextColor = NihongoTheme.colors.primaryText.copy(
        alpha = if (showTraceGuide) 0.16f else 0f
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Verified Stroke Count Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Brush,
                    contentDescription = null,
                    tint = inkGreenColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (verifiedStrokeCount > 0) {
                        language.selectText(
                            "নির্ধারিত স্ট্রোক সংখ্যা: $verifiedStrokeCount (আপনার আঁকা: ${completedStrokes.size})",
                            "Verified Strokes: $verifiedStrokeCount (Drawn: ${completedStrokes.size})"
                        )
                    } else {
                        language.selectText(
                            "সংযুক্ত বর্ণ অনুশীলন (অঙ্কিত স্ট্রোক: ${completedStrokes.size})",
                            "Combined Kana Practice (Drawn Strokes: ${completedStrokes.size})"
                        )
                    },
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
            }

            if (verifiedStrokeCount > 0 && completedStrokes.size == verifiedStrokeCount) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = NihongoTheme.colors.selectedContainer
                ) {
                    Text(
                        text = language.selectText("সঠিক স্ট্রোক সংখ্যা!", "Exact Stroke Count!"),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = inkGreenColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Genkouyoushi 4-Quadrant Writing Box
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = MaterialTheme.shapes.large,
            color = NihongoTheme.colors.elevatedSurface,
            border = BorderStroke(2.dp, NihongoTheme.colors.inkGreen.copy(alpha = 0.65f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Watermark Character Guide
                Text(
                    text = character,
                    fontSize = if (character.length == 1) 170.sp else 105.sp,
                    fontWeight = FontWeight.Medium,
                    color = guideTextColor
                )

                // Interactive Drawing Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(character) {
                            detectDragGestures(
                                onDragStart = { startOffset ->
                                    activeStrokePoints = listOf(startOffset)
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    activeStrokePoints = activeStrokePoints + change.position
                                },
                                onDragEnd = {
                                    if (activeStrokePoints.size > 1) {
                                        completedStrokes.add(activeStrokePoints)
                                    }
                                    activeStrokePoints = emptyList()
                                },
                                onDragCancel = {
                                    activeStrokePoints = emptyList()
                                }
                            )
                        }
                ) {
                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 12f), 0f)

                    // Vertical center guideline
                    drawLine(
                        color = gridColor,
                        start = Offset(size.width / 2f, 0f),
                        end = Offset(size.width / 2f, size.height),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = dashEffect
                    )

                    // Horizontal center guideline
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, size.height / 2f),
                        end = Offset(size.width, size.height / 2f),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = dashEffect
                    )

                    // Helper to render a smooth calligraphy path
                    fun drawBrushStroke(points: List<Offset>, strokeIndex: Int) {
                        if (points.isEmpty()) return
                        val path = Path().apply {
                            moveTo(points.first().x, points.first().y)
                            for (i in 1 until points.size) {
                                lineTo(points[i].x, points[i].y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = inkGreenColor,
                            style = Stroke(
                                width = 12.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                        // Phoenix Flower Red dot marking the start of each stroke
                        drawCircle(
                            color = phoenixRedColor,
                            radius = 5.dp.toPx(),
                            center = points.first()
                        )
                    }

                    completedStrokes.forEachIndexed { index, strokePoints ->
                        drawBrushStroke(strokePoints, index)
                    }

                    if (activeStrokePoints.isNotEmpty()) {
                        drawBrushStroke(activeStrokePoints, completedStrokes.size)
                    }
                }
            }
        }

        // Verified Stroke Order Instructions Card
        NihongoCard(contentPadding = 12.dp) {
            Text(
                text = language.selectText("স্ট্রোক অর্ডার নির্দেশিকা (筆順):", "Verified Stroke Order Guide:"),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = NihongoTheme.colors.inkGreen
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = language.selectText(strokeDirectionsBn, strokeDirectionsEn),
                style = MaterialTheme.typography.bodyMedium,
                color = NihongoTheme.colors.primaryText
            )
        }

        // Canvas Controls: Toggle Trace Guide, Undo Stroke, Clear Pad
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NihongoOutlinedButton(
                text = if (showTraceGuide) {
                    language.selectText("ছায়া লুকান", "Hide Guide")
                } else {
                    language.selectText("ছায়া দেখুন", "Show Guide")
                },
                onClick = { showTraceGuide = !showTraceGuide },
                leadingIcon = if (showTraceGuide) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                modifier = Modifier.weight(1f)
            )

            NihongoOutlinedButton(
                text = language.selectText("পূর্বাবস্থা", "Undo"),
                onClick = {
                    if (completedStrokes.isNotEmpty()) {
                        completedStrokes.removeAt(completedStrokes.lastIndex)
                    }
                },
                enabled = completedStrokes.isNotEmpty(),
                leadingIcon = Icons.AutoMirrored.Filled.Undo,
                modifier = Modifier.weight(1f)
            )

            NihongoAccentButton(
                text = language.selectText("মুছুন", "Clear"),
                onClick = {
                    completedStrokes.clear()
                    activeStrokePoints = emptyList()
                },
                enabled = completedStrokes.isNotEmpty(),
                leadingIcon = Icons.Default.DeleteSweep,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
