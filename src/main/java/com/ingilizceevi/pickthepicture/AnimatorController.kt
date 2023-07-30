package com.ingilizceevi.pickthepicture


import android.animation.AnimatorSet
import androidx.core.animation.doOnEnd

class AnimatorController(ic: ImageController) {
    private val imageController :ImageController = ic

    fun setupFadeOutAnimator():AnimatorSet{
        val fadeOutSet = AnimatorSet()
        val fadeList = imageController.panelOfFadeOutViews()
        fadeOutSet.playTogether(fadeList)
        return fadeOutSet
    }


}