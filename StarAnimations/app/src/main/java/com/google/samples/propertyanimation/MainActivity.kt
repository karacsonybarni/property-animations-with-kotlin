/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var star: ImageView
    private lateinit var rotateButton: Button
    private lateinit var translateButton: Button
    private lateinit var scaleButton: Button
    private lateinit var fadeButton: Button
    private lateinit var colorizeButton: Button
    private lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById(R.id.rotateButton)
        translateButton = findViewById(R.id.translateButton)
        scaleButton = findViewById(R.id.scaleButton)
        fadeButton = findViewById(R.id.fadeButton)
        colorizeButton = findViewById(R.id.colorizeButton)
        showerButton = findViewById(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)
        animator.duration = 1000
        animator.disableViewDuringAnimation(rotateButton)
        animator.start()
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

    private fun translater() {
        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)
        animator.repeatInReverse()
        animator.disableViewDuringAnimation(translateButton)
        animator.start()
    }

    private fun ObjectAnimator.repeatInReverse() {
        repeatCount = 1
        repeatMode = ObjectAnimator.REVERSE
    }

    private fun scaler() {
        val scale = 4f
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, scale)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scale)
        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)
        animator.repeatInReverse()
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()
    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatInReverse()
        animator.disableViewDuringAnimation(fadeButton)
        animator.start()
    }

    private fun colorizer() {
        val animator =
            ObjectAnimator
                .ofArgb(star.parent, "backgroundColor", Color.BLACK, Color.RED)
        animator.duration = 500
        animator.repeatInReverse()
        animator.disableViewDuringAnimation(colorizeButton)
        animator.start()
    }

    private fun shower() {
        val container = star.parent as ViewGroup
        val newStar = addNewStarTo(container)
        val animatorSet = createAnimatorSet(newStar, container)
        animatorSet.start()
    }

    private fun addNewStarTo(container: ViewGroup): View {
        val newStar = createStar()
        val containerW = container.width
        val starW = star.width.toFloat() * newStar.scaleX
        newStar.translationX = Math.random().toFloat() * containerW - starW / 2
        container.addView(newStar)
        return newStar
    }

    private fun createStar(): ImageView {
        val newStar = ImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
            )
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        return newStar
    }

    private fun createAnimatorSet(view: View, container: ViewGroup): Animator {
        val mover = createMover(view, container)
        val rotator = createRotator(view)
        val animatorSet = createAnimatorSet(mover, rotator)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(view)
            }
        })
        return animatorSet
    }

    private fun createMover(view: View, container: ViewGroup): ObjectAnimator {
        val starH = star.height.toFloat() * view.scaleY
        val start = -starH
        val end = container.height + starH
        val mover = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, start, end)
        mover.interpolator = AccelerateInterpolator(1f)
        return mover
    }

    private fun createRotator(view: View): ObjectAnimator {
        val rotator = ObjectAnimator.ofFloat(view, View.ROTATION, (Math.random() * 1000).toFloat())
        rotator.interpolator = LinearInterpolator()
        return rotator
    }

    private fun createAnimatorSet(vararg animators: Animator): AnimatorSet {
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(*animators)
        animatorSet.duration = (Math.random() * 1500 + 500).toLong()
        return animatorSet
    }
}
