package com.ingilizceevi.pickthepicture

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

private const val ARG_PARAM1 = "param1"


class ImageFragment : Fragment() {
    private var imageTag: Int? = null
    lateinit var mainView: View
    lateinit var imageView: ImageView
    lateinit var myImageFrameLayout:FrameLayout
    private val gameBrain: LaneViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageTag = it.getInt(ARG_PARAM1)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mainView = inflater.inflate(R.layout.fragment_image, container, false)
        imageView = mainView.findViewById(R.id.imageView)
        imageView.setImageResource(R.drawable.logo)
        if(imageTag!=null){imageView.id = imageTag!!}
        myImageFrameLayout=mainView.findViewById(R.id.imageFrame)
        //if(imageTag!=null)loadImageFromMasterCollection()
        return mainView
    }
    fun handleOnImageView():ImageView{
        return imageView
    }

    fun loadImageFromMasterCollection(){
        var reference = gameBrain.getIdealLaneValue(imageTag!!)
        if (reference == "-1") reference = gameBrain.getRandomConceptFromDiscardedPile()
        val drawable = gameBrain.masterCollectionDrawables[reference!!]
        imageView.setImageDrawable(drawable)
    }
        fun imageValuesAreReset(){
            imageView.alpha = 1f
            imageView.rotation = 0F
            imageView.scaleX = 1.0f
            imageView.scaleY = 1.0f

        }

        fun getCurrentCoordinatesOnScreen():IntArray{
            val frame = mainView.findViewById<FrameLayout>(R.id.imageFrame)
            val location = IntArray(2)
            frame.getLocationInWindow(location)
            return location
        }

        fun alphaAnimatorReturnedToFull(){
            if(imageView.alpha!=1f) {
                AnimatorInflater.loadAnimator(requireContext(), R.animator.alpha_animator)
                    .apply {
                        setTarget(imageView)
                        start()
                    }
            }
        }

        fun enlargerAnimatorIsSetForView(): Animator {
            val myAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.enlarger_animator)
            myAnimator.setTarget(imageView)
            return myAnimator
        }
        fun shakerAnimatorIsSetForView():Animator{
            val myAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.shaker_animator)
            myAnimator.setTarget(imageView)
            return myAnimator
        }
        fun fadeAnimatorIsSetForView():Animator{
            val myFader = AnimatorInflater.loadAnimator(requireContext(), R.animator.alpha_animator)
            myFader.setTarget(imageView)
            return myFader
        }
    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }


}