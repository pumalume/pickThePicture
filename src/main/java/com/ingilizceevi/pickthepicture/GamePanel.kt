package com.ingilizceevi.pickthepicture

import android.animation.AnimatorSet
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.animation.doOnEnd
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ingilizceevi.conceptbuilder.ConceptLoader
import com.ingilizceevi.frenchconnection.SoundPlayer

private const val ARG_PARAM1 = "param1"

class GamePanel : Fragment() {

    lateinit var goSignal:FrameLayout
    var numOfImages = 3
    lateinit var imageControl:ImageController
    lateinit var myTimer: Chronometer
    lateinit var myClicks: TextView
    lateinit var cancelButton : ImageView
    lateinit var main: View
    private val imageViewsPanel: MutableList<ImageView> = ArrayList(0)
    private val gameBrain: LaneViewModel by activityViewModels()
    private var gameFinished = false
    lateinit var playButton: ImageView
    lateinit var soundPlayer: SoundPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val chapter = it.getString(ARG_PARAM1)
        }
        //if(!gameBrain.isGameInitialized()){
            soundPlayer = SoundPlayer(requireContext())
//            val conceptLoader = chapter?.let { ConceptLoader(it) }
            //gameBrain.initiateModel(chapter!!)
       // }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main =  inflater.inflate(R.layout.fragment_game_panel, container, false)
        myTimer = main.findViewById(R.id.myTimer)
        goSignal = main.findViewById(R.id.signalCircle)
        goSignal.setBackgroundResource(R.drawable.red_circle)
        myClicks = main.findViewById(R.id.myClicks)
        cancelButton= main.findViewById(R.id.cancelButton)
        //cancelButton.setOnClickListener(onCancelClick)
        playButton = main.findViewById(R.id.playButton)
        playButton.setOnClickListener{playTarget()}
        soundPlayer= SoundPlayer(requireContext())
        main.setOnClickListener(initiatorClick)
        //observerIsEstablishedForStartGame()
        //observerIsEstablishedForCancelGame()
        return main
    }
    val initiatorClick = View.OnClickListener {
        main.setOnClickListener(null)
        startGame()
    }

    fun handleOnCancelButton(): ImageView {
        return cancelButton
    }
    val onCancelClick = View.OnClickListener {
        val myDialog = DialogFragment()
        myDialog.show(childFragmentManager, "Cancel")
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val myDialog = DialogFragment()
//        myDialog.show(childFragmentManager, "PressStart")
//    }

    fun observerIsEstablishedForCancelGame() {
        val gameIsCancelledObserver = Observer<Boolean> {
            requireActivity().finish()
        }
        gameBrain.cancelGameLiveData.observe(viewLifecycleOwner, gameIsCancelledObserver)
    }

    fun observerIsEstablishedForStartGame() {
        val gameIsStartedObserver = Observer<Boolean> { startGame() }
        gameBrain.startGameLiveData.observe(viewLifecycleOwner, gameIsStartedObserver)
    }

    private fun startGame(){
        imageControl = childFragmentManager.findFragmentById(R.id.laneControllerView) as ImageController
        imageViewsPanelIsLoaded()
        imageControl.refreshPanelOfImages()
        gameBrain.myTargetConceptIsSetFromIdealMap()
        Handler().postDelayed({
            playTarget()
            myTimer.start()
        }, 500)
    }

    fun imageViewsPanelIsLoaded(){
        for(i in 0 until numOfImages){
            val imageFragment = imageControl.imageFragmentPanel[i]
            imageViewsPanel.add(imageFragment.handleOnImageView())
        }
    }

    fun playTarget(){
        if(gameBrain.myTargetConcept!="-1") {
            val u = gameBrain.getTargetUri()
            val list = mutableListOf(u)
            soundPlayer.setupSoundSequence(list)
            val player = soundPlayer.dontStartSoundYet()
            player!!.setOnCompletionListener {
                setOnClickListeners()
            }
            player.start()
        }
    }

    fun setOnClickListeners(){
        for(i in 0 until numOfImages!!){
            imageViewsPanel[i].setOnClickListener(myOnClickListener)
        }
        goSignal.setBackgroundResource(R.drawable.green_circle)
    }
    fun nullifyAllImageViewListeners(){
        for(i in 0 until numOfImages){
            imageViewsPanel[i].setOnClickListener(null)
        }
        goSignal.setBackgroundResource(R.drawable.red_circle)
    }
    fun dealWithTheClicks(){
        gameBrain.increaseClickedCounter()
        val totalClicksSoFar = gameBrain.getTotalClicks().toString()
        myClicks.text = getString(R.string.totalClicks, totalClicksSoFar)

    }
    val myOnClickListener = View.OnClickListener{
        nullifyAllImageViewListeners()
        dealWithTheClicks()
        validateSelection(it.id)
    }

    fun validateSelection(viewId:Int){
        if(gameBrain.isTargetConceptTrue(viewId)){targetConceptIsTrue(viewId)}
        else targetIsFalse(viewId)
    }
    fun targetConceptIsTrue(viewId:Int) {
        if(!gameBrain.schemaForImageClickedTrue(viewId)){
            gameFinished=true
        }
        val bigAnim = imageControl.imageFragmentPanel[viewId].enlargerAnimatorIsSetForView()
        bigAnim.doOnEnd { allViewsAreFadedOut(gameFinished) }
        bigAnim.start()
    }
    fun targetIsFalse(viewId:Int) {
        gameBrain.schemaForImageClickedFalse()
        val shakeAnim = imageControl.imageFragmentPanel[viewId].shakerAnimatorIsSetForView()
        shakeAnim.doOnEnd { allViewsAreFadedOut(false) }
        shakeAnim.start()
    }

    fun setupFadeOutAnimator(finished:Boolean):AnimatorSet{
        val fadeOutControl = AnimatorController(imageControl)
        val fadeOutSet = fadeOutControl.setupFadeOutAnimator()
        fadeOutSet.doOnEnd {
            if(!finished)startAnotherRoundOfFun()
            else concludingFragmentIsLoaded()
        }
        return fadeOutSet
    }
    fun allViewsAreFadedOut(finished:Boolean){
        setupFadeOutAnimator(finished).start()
    }

    fun concludingFragmentIsLoaded(){
        playButton.visibility = View.INVISIBLE
        goSignal.visibility = View.INVISIBLE
        myTimer.stop()
        val myTime = myTimer.text.toString()
        val myClicks = gameBrain.getTotalClicks().toString()

//        gameBrain.studentInfo.studentClicks = myClicks.toInt()
//        gameBrain.studentInfo.studentTime = gameBrain.calculateStudentTime(myTime)
//        childFragmentManager.beginTransaction()
//            .replace(R.id.laneControllerView, ConcludingFragment.newInstance(myTime, myClicks), "Hi")
//            .commit()
    }
    fun startAnotherRoundOfFun(){
        imageControl.refreshPanelOfImages()
        playTarget()
    }


    ////////////////////////////////////////////////////
    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            GamePanel().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}