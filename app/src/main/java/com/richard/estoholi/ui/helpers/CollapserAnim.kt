package com.richard.estoholi.ui.helpers

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation


class CollapserAnim {
    companion object{
        fun expand(v: View) {
            val matchParentMeasureSpec: Int = View.MeasureSpec.makeMeasureSpec((v.getParent() as View).getWidth(), View.MeasureSpec.EXACTLY)
            val wrapContentMeasureSpec: Int = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
            val targetHeight: Int = v.getMeasuredHeight()

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            v.getLayoutParams().height = 1
            v.setVisibility(View.VISIBLE)
            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    v.getLayoutParams().height = if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            // Expansion speed of 1dp/ms
            a.setDuration(((targetHeight / v.getContext().getResources().getDisplayMetrics().density).toLong()) )
            v.startAnimation(a)
        }

        fun collapse(v: View) {
            val initialHeight: Int = v.getMeasuredHeight()
            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    if (interpolatedTime == 1f) {
                        v.setVisibility(View.GONE)
                    } else {
                        v.getLayoutParams().height = initialHeight - (initialHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            // Collapse speed of 1dp/ms
            a.setDuration(((initialHeight / v.getContext().getResources().getDisplayMetrics().density).toLong()))
            v.startAnimation(a)
        }
    }
}