package com.ingilizceevi.pickthepicture


import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels


private const val ARG_PARAM1 = "param1"

class ImageController : Fragment() {
    private var numOfImages: Int? = 3
    val imageFragmentPanel:MutableList<ImageFragment> = ArrayList(0)
    lateinit var main :View
    private val gameBrain: LaneViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            numOfImages = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        main =  inflater.inflate(R.layout.fragment_image_controller, container, false)
        addImageViewToPanel(numOfImages!!)
        return main
    }

    fun addImageViewToPanel(num:Int){
        val manager = childFragmentManager.beginTransaction()
        for(i in 0 until num){
            //this line is questionable... should not be called here
            imageFragmentPanel.add(ImageFragment.newInstance(i))
            manager.add(R.id.linearLayoutPanel, imageFragmentPanel[i], i.toString())
        }
        manager.commit()

    }
    fun panelOfFadeOutViews():MutableList<Animator>{
        val myFadeArray: MutableList<Animator> = ArrayList(0)
        for(i in 0 until numOfImages!!){
            myFadeArray.add(imageFragmentPanel[i].fadeAnimatorIsSetForView())
        }
        return myFadeArray
    }

    fun refreshPanelOfImages(){
        for(i in 0 until numOfImages!!){
            imageFragmentPanel[i].loadImageFromMasterCollection()
        }
        imagePanelValuesAreReset()
    }
    fun nullifyAllListeners(){
        for(i in 0 until numOfImages!!){
            imageFragmentPanel[i].handleOnImageView()
                .setOnClickListener(null)
        }
    }
    fun selectionIsTrue(viewId:Int):Animator{
        val shakeAnim = imageFragmentPanel[viewId].shakerAnimatorIsSetForView()
        return shakeAnim
    }

    fun imagePanelValuesAreReset(){
        for(i in 0 until numOfImages!!){
            imageFragmentPanel[i].imageValuesAreReset()
        }
    }
    fun selectionIsFalse(viewId:Int):Animator{
        val enlargerAnim = imageFragmentPanel[viewId].enlargerAnimatorIsSetForView()
        return enlargerAnim
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            ImageController().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }


}