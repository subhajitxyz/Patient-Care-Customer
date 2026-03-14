package com.real.patientcare.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.apply
import kotlin.math.sin

@Composable
fun WavyBackgroundView(backgroundColorResId: Int, height: Dp) {
    val waveColor = colorResource(backgroundColorResId)

    Canvas(
        modifier = Modifier.fillMaxWidth().height(height)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val wavesStartY = canvasHeight * 0.12f // Start near top
        val amplitude = 30.dp.toPx()

        val numberOfPoints = 500
        val stepX = canvasWidth / numberOfPoints
        val numberOfWaves = 1.2f
        val waveFrequencyFactor =
            (1.8f * Math.PI / canvasWidth) * numberOfWaves

        val wavyPath = Path().apply {
            // Start from top-left
            moveTo(0f, 0f)
            lineTo(0f, wavesStartY)

            var x = 0f

            while (x <= canvasWidth) {
                // FLIPPED sine wave (positive instead of negative)
                val y = wavesStartY +
                        amplitude * sin(x * waveFrequencyFactor).toFloat()
                lineTo(x, y)
                x += stepX
            }

            // Close shape to top-right
            lineTo(canvasWidth, 0f)
            close()
        }

        drawPath(
            path = wavyPath,
            color = waveColor
        )
    }
}



