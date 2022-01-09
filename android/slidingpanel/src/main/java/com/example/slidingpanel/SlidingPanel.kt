package com.example.slidingpanel

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

class SlidingPanel(
    context: Context,
    attrs: AttributeSet) : TextView(context, attrs, 0) {

    companion object {
        const val COLOR_NORMAL = Color.GRAY
        const val COLOR_MOVE = Color.GREEN
    }

    private var _gravity: Int = Gravity.NO_GRAVITY
    private var _origin: PointF? = null
    private var _rect: Rect? = null

    init {
        setBackgroundColor(COLOR_NORMAL)
        setOnTouchListener(object : OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                view?.let { v->
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            v.setBackgroundColor(COLOR_MOVE)
                            _rect = Rect(left, top, right, bottom)
                            _origin = PointF(event.rawX, event.rawY)
                            removeGravity()
                        }
                        MotionEvent.ACTION_UP -> {
                            v.setBackgroundColor(COLOR_NORMAL)
                            _origin = null
                            resolveGravity()
                        }
                        MotionEvent.ACTION_MOVE -> {
                            v.setBackgroundColor(COLOR_MOVE)
                            moveTo(event.rawX, event.rawY)
                        }
                    }
                }
                return true
            }
        })
    }

    private fun removeGravity() {
        _gravity = Gravity.NO_GRAVITY
        val param = layoutParams as FrameLayout.LayoutParams
        param.gravity = _gravity
        _rect?.let {
            param.leftMargin = it.left
            param.topMargin = it.top
        }
        layoutParams = param
    }

    private fun resolveGravity() {
        val layout = parent as FrameLayout
        val params = layoutParams as FrameLayout.LayoutParams
        val center = PointF(x + width / 2.0f, y + height / 2.0f)
        params.gravity = if (center.y >= layout.height / 2.0) Gravity.BOTTOM else Gravity.TOP
        params.topMargin = 0
        layoutParams = params
    }

    private fun moveTo(x: Float, y: Float) {
        _origin?.let {
            val dx = x - it.x
            val dy = y - it.y
            val layout = parent as FrameLayout
            val param = layoutParams as FrameLayout.LayoutParams
            _rect?.let {
                param.leftMargin += dx.toInt()
                param.topMargin += dy.toInt()
                param.leftMargin = param.leftMargin.coerceIn(0, layout.width - width)
                param.topMargin = param.topMargin.coerceIn(0, layout.height - height)
                _origin = PointF(x, y)
            }
            layoutParams = param
        }
    }

}