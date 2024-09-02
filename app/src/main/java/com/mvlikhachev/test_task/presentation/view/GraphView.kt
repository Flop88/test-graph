package com.mvlikhachev.test_task.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.mvlikhachev.test_task.domain.model.Point
import kotlin.properties.Delegates

class GraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var points: List<Point> = listOf()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint()
    private val circlePaint = Paint()
    private val axisPaint = Paint()
    private val textPaint = Paint()
    private val linePath = Path()
    private val tablePaint = Paint()


    private val pointsX = mutableListOf<Float>()
    private val pointsY = mutableListOf<Float>()

    private val internalPadding = 12 * resources.displayMetrics.density
    private val axisPadding = 50f // Паддинг для осей и текста

    // Переменные для масштабирования
    private var scaleFactor = 1.0f
    private var scaleGestureDetector: ScaleGestureDetector

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        linePaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 7f
            color = Color.BLACK
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }

        circlePaint.apply {
            style = Paint.Style.FILL
            color = Color.RED
            isAntiAlias = true
        }

        axisPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = Color.BLACK
            isAntiAlias = true
        }

        textPaint.apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            textSize = 30f
            isAntiAlias = true
        }

        tablePaint.apply {
            style = Paint.Style.FILL
            color = Color.WHITE
            textSize = 20f
            isAntiAlias = true
        }

        // Инициализация детектора жестов для отслеживания масштабирования
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        // Устанавливаем масштабирование на canvas
        canvas.save()
        canvas.scale(scaleFactor, scaleFactor, width / 2f, height / 2f)

        canvas.drawRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            paint.apply { color = Color.parseColor("#FFFFFF") }
        )

        if (points.isNotEmpty()) {
            val contentWidth = (width - paddingStart - paddingEnd - internalPadding * 2 - axisPadding) * scaleFactor
            val contentHeight = (height - paddingTop - paddingBottom - internalPadding * 2 - axisPadding) * scaleFactor

            val maxDataY = points.maxByOrNull { it.y }?.y?.toFloat() ?: 1f
            val minDataY = points.minByOrNull { it.y }?.y?.toFloat() ?: 0f
            val maxDataX = points.maxByOrNull { it.x }?.x?.toFloat() ?: 1f
            val minDataX = points.minByOrNull { it.x }?.x?.toFloat() ?: 0f

            val dataSpanY = if (maxDataY != minDataY) maxDataY - minDataY else 1.0f
            val minDataExtendedY = minDataY - dataSpanY / 4F
            val maxDataExtendedY = maxDataY + dataSpanY / 4F
            val dataSpanExtendedY = maxDataExtendedY - minDataExtendedY

            pointsX.clear()
            pointsY.clear()

            if (points.size == 1) {
                pointsX.add(paddingStart + internalPadding + contentWidth / 2)
                pointsY.add(paddingTop + internalPadding + contentHeight / 2)
            } else {
                val widthStep = contentWidth / (points.size - 1)

                points.indices.mapTo(pointsX) {
                    paddingStart + internalPadding + axisPadding + widthStep * it
                }

                points.mapTo(pointsY) {
                    paddingTop + internalPadding + axisPadding + ((1 - ((it.y.toFloat() - minDataExtendedY) / dataSpanExtendedY)) * contentHeight)
                }

                linePath.apply {
                    reset()
                    moveTo(pointsX.first(), pointsY.first())

                    for (i in 1 until pointsX.size) {
                        val prevX = pointsX[i - 1]
                        val prevY = pointsY[i - 1]
                        val currX = pointsX[i]
                        val currY = pointsY[i]

                        val controlPointX1 = prevX + (currX - prevX) / 2
                        val controlPointY1 = prevY
                        val controlPointX2 = currX - (currX - prevX) / 2
                        val controlPointY2 = currY

                        cubicTo(controlPointX1, controlPointY1, controlPointX2, controlPointY2, currX, currY)
                    }
                }

                canvas.drawPath(linePath, linePaint)
            }

            points.indices.forEach {
                canvas.drawCircle(pointsX[it], pointsY[it], 15f, circlePaint)
            }

            // Рисуем оси X и Y
            drawAxes(canvas, contentWidth, contentHeight, maxDataExtendedY, minDataX, maxDataX, dataSpanExtendedY)
        }

        canvas.restore()
    }

    private fun drawAxes(
        canvas: Canvas,
        contentWidth: Float,
        contentHeight: Float,
        maxDataExtendedY: Float,
        minDataX: Float,
        maxDataX: Float,
        dataSpanExtendedY: Float
    ) {
        // Ось Y
        canvas.drawLine(
            paddingStart + internalPadding + axisPadding,
            paddingTop + internalPadding,
            paddingStart + internalPadding + axisPadding,
            height - paddingBottom - internalPadding,
            axisPaint
        )

        // Ось X
        canvas.drawLine(
            paddingStart + internalPadding + axisPadding,
            height - paddingBottom - internalPadding - axisPadding,
            width - paddingEnd - internalPadding,
            height - paddingBottom - internalPadding - axisPadding,
            axisPaint
        )

        // Деления и метки на оси Y
        val yStep = contentHeight / 5 // 5 делений на оси Y
        for (i in 0..5) {
            val yPosition = paddingTop + internalPadding + axisPadding + i * yStep
            val yValue = maxDataExtendedY - i * (dataSpanExtendedY / 5)
            canvas.drawText(String.format("%.1f", yValue), 10f, yPosition, textPaint)
            canvas.drawLine(
                paddingStart + internalPadding + axisPadding - 10,
                yPosition,
                paddingStart + internalPadding + axisPadding,
                yPosition,
                axisPaint
            )
        }

        // Деления и метки на оси X
        val xStep = contentWidth / 5 // 5 делений на оси X
        for (i in 0..5) {
            val xPosition = paddingStart + internalPadding + axisPadding + i * xStep
            val xValue = minDataX + i * (maxDataX - minDataX) / 5
            canvas.drawText(String.format("%.1f", xValue), xPosition - 20f, height - 10f, textPaint)
            canvas.drawLine(
                xPosition,
                height - paddingBottom - internalPadding - axisPadding,
                xPosition,
                height - paddingBottom - internalPadding - axisPadding + 10,
                axisPaint
            )
        }
    }

    // Обработка событий касаний
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    // Класс для обработки масштабирования
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(1f, 3.0f)
            invalidate()
            return true
        }
    }

    fun setPoints(newPoints: List<Point>) {
        points = newPoints
        invalidate()
    }
}
