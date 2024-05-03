package com.example.assignment4.util

import android.graphics.*
import com.squareup.picasso.Transformation

class RoundedCornersTransformation(
    private val radius: Float
) : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, radius, radius, paint)

        if (source != output) {
            source.recycle()
        }

        return output
    }

    override fun key(): String {
        return "rounded_corners"
    }
}