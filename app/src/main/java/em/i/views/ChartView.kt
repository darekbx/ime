package em.i.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import em.i.R
import em.i.repository.Entry
import java.util.*

fun Context?.getColorOrDefault(resId: Int) = this?.getColor(resId) ?: Color.BLACK

class ChartView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        val RADIUS = 8F
        val PADDING = 12F
        val HOUR_SCOPE = 10f
        val BOTTOM_OFFSET = 30f
    }

    private val linesPaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 1.5f
        color = Color.BLACK
        textSize = 18F
    }

    private val guidesPaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 0.5f
        color = Color.LTGRAY
    }

    private val timePaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 0.5f
    }

    private val circlePaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 2.5f
        style = Paint.Style.STROKE
    }

    private val kitchenPaint = Paint(circlePaint)
    private val metPaint = Paint(circlePaint).apply { style = Paint.Style.FILL_AND_STROKE }
    private val enterOfficePaint = Paint(circlePaint)
    private var data: List<Entry>? = null

    val minute = 60F
    val minValue = Entry.MIN_HOUR * minute
    val maxValue = HOUR_SCOPE * minute
    var curentTime = 0F
    var initialTop = 0F

    init {
        kitchenPaint.color = context.getColorOrDefault(R.color.kitchen)
        metPaint.color = context.getColorOrDefault(R.color.met)
        timePaint.color = context.getColorOrDefault(R.color.met)
        enterOfficePaint.color = context.getColorOrDefault(R.color.enter_office)
        prepareCurrentTime()
    }

    private fun prepareCurrentTime() {
        with(Calendar.getInstance()) {
            curentTime = get(Calendar.HOUR_OF_DAY) * minute + get(Calendar.MINUTE)
        }
    }

    fun updateData(data: List<Entry>) {
        this.data = data.sortedBy { it.computedValue }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (data == null) return
        canvas?.let { canvas ->
            drawHours(canvas)
            canvas.translate(0F, -BOTTOM_OFFSET)
            drawAxes(canvas)
            drawData(canvas)
        }
    }

    fun drawAxes(canvas: Canvas) {
        canvas.drawLine(0F, 0F, 0F, height.toFloat(), linesPaint)
        canvas.drawLine(0F, height.toFloat(), width.toFloat(), height.toFloat(), linesPaint)
    }

    fun drawHours(canvas: Canvas) {
        val top = height.toFloat()
        val ratio = width.toFloat().div(maxValue)
        ((Entry.MIN_HOUR + 1)..(Entry.MAX_HOUR - 1))
                .map { it * minute }
                .forEach {
                    var left = width - ((maxValue - (it - minValue)) * ratio)
                    canvas.drawLine(left, top - RADIUS - BOTTOM_OFFSET, left, top + RADIUS - BOTTOM_OFFSET, linesPaint)
                    canvas.drawText("%02d".format((it / minute).toInt()), left - 8F, top, linesPaint)
                }

        drawCurrentTime(ratio, canvas, top)
    }

    private fun drawCurrentTime(ratio: Float, canvas: Canvas, top: Float) {
        val left = width - ((maxValue - (curentTime - minValue)) * ratio)
        canvas.drawLine(left, 0F, left, top + RADIUS - BOTTOM_OFFSET, timePaint)
    }

    fun drawData(canvas: Canvas) {
        val ratio = width.toFloat().div(maxValue)
        var lastValue = -1
        initialTop = height - PADDING.times(3)
        var top = initialTop

        data?.forEach { entry ->
            var value = entry.computedValue
            var left = width - ((maxValue - (value - minValue)) * ratio)
            top = liftWithSimilarValue(lastValue, value, top)
            lastValue = value
            drawEntry(entry, canvas, left, top)
        }
    }

    private fun liftWithSimilarValue(lastValue: Int, value: Int, top: Float): Float {
        if (lastValue != -1) {
            val bottomRange = lastValue - PADDING
            val topRange = lastValue + PADDING
            return when (value in bottomRange..topRange) {
                true -> top - RADIUS.times(2)
                else -> initialTop
            }
        }
        return top
    }

    private fun drawEntry(entry: Entry, canvas: Canvas, left: Float, top: Float) {
        lateinit var paint: Paint
        when (entry.type) {
            Entry.TYPE_KITCHEN -> paint = kitchenPaint
            Entry.TYPE_ENTER_OFFICE -> paint = enterOfficePaint
            else -> paint = metPaint
        }
        canvas.drawCircle(left, top, RADIUS, paint)
        canvas.drawLine(left, top + RADIUS, left, height.toFloat(), guidesPaint)
    }
}