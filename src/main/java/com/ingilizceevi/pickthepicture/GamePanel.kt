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
import com.ingilizceevi.frenchconnection.SoundPlayer

class GamePanel : Fragment() {

    lateinit var goSignal: FrameLayout
    private val numOfImages = 3
    lateinit var imageControl: ImageController
    lateinit var timer_view: Chronometer
    lateinit var clicks_view: TextView
    lateinit var main: View
    private val imageViewsPanel: MutableList<ImageView> = ArrayList(0)
    private val gameBrain: LaneViewModel by activityViewModels()
    private var gameFinished = false
    lateinit var playButton: ImageView
    lateinit var soundPlayer: SoundPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundPlayer = SoundPlayer(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main = inflater.inflate(R.layout.fragment_game_panel, container, false)
        timer_view = main.findViewById(R.id.myTimer)
        goSignal = main.findViewById(R.id.signalCircle)
        goSignal.setBackgroundResource(R.drawable.red_circle)
        clicks_view = main.findViewById(R.id.clicks_textview)
        playButton = main.findViewById(R.id.playButton)
        playButton.setOnClickListener { playTarget() }
        soundPlayer = SoundPlayer(requireContext())
        main.setOnClickListener(initiatorClick)
        //observerIsEstablishedForStartGame()
        return main
    }

    val initiatorClick = View.OnClickListener {
        main.setOnClickListener(null)
        startGame()
    }

    fun observerIsEstablishedForStartGame() {
        val gameIsStartedObserver = Observer<Boolean> { startGame() }
        gameBrain.startGameLiveData.observe(viewLifecycleOwner, gameIsStartedObserver)
    }

    private fun startGame() {
        imageControl =
            childFragmentManager.findFragmentById(R.id.laneControllerView) as ImageController
        imageViewsPanelIsLoaded()
        imageControl.refreshPanelOfImages()
        gameBrain.myTargetConceptIsSetFromIdealMap()
        Handler().postDelayed({
            playTarget()
            timer_view.start()
        }, 200)
    }

    fun imageViewsPanelIsLoaded() {
        for (i in 0 until numOfImages) {
            val imageFragment = imageControl.imageFragmentPanel[i]
            imageViewsPanel.add(imageFragment.handleOnImageView())
        }
    }

    fun playTarget() {
        if (gameBrain.myTargetConcept != "-1") {
            val u = gameBrain.getTargetUri()
            val list = mutableListOf(u)
            soundPlayer.setupSoundSequence(list)
            val player = soundPlayer.setUpSoundPlayerAndReturnHandle()
            player!!.setOnCompletionListener {
                setOnImageClickListeners()
            }
            player.start()
        }
    }

    fun setOnImageClickListeners() {
        for (i in 0 until numOfImages!!) {
            imageViewsPanel[i].setOnClickListener(onImageClickListener)
        }
        goSignal.setBackgroundResource(R.drawable.green_circle)
    }

    fun nullifyAllImageViewListeners() {
        for (i in 0 until numOfImages) {
            imageViewsPanel[i].setOnClickListener(null)
        }
        goSignal.setBackgroundResource(R.drawable.red_circle)
    }

    fun dealWithTheClicks() {
        gameBrain.increaseClickedCounter()
        val totalClicksSoFar = gameBrain.getTotalClicks().toString()
        clicks_view.text = totalClicksSoFar

    }

    val onImageClickListener = View.OnClickListener {
        nullifyAllImageViewListeners()
        dealWithTheClicks()
        validateSelection(it.id)
    }

    fun validateSelection(viewId: Int) {
        if (gameBrain.isTargetConceptTrue(viewId)) {
            targetConceptIsTrue(viewId)
        } else targetIsFalse(viewId)
    }

    fun targetConceptIsTrue(viewId: Int) {
        if (!gameBrain.schemaForImageClickedTrue(viewId)) {
            gameFinished = true
        }
        val bigAnim = imageControl.imageFragmentPanel[viewId].enlargerAnimatorIsSetForView()
        bigAnim.doOnEnd { allViewsAreFadedOut(gameFinished) }
        bigAnim.start()
    }

    fun targetIsFalse(viewId: Int) {
        gameBrain.schemaForImageClickedFalse()
        val shakeAnim = imageControl.imageFragmentPanel[viewId].shakerAnimatorIsSetForView()
        shakeAnim.doOnEnd { allViewsAreFadedOut(false) }
        shakeAnim.start()
    }

    fun setupFadeOutAnimator(finished: Boolean): AnimatorSet {
        val fadeOutControl = AnimatorController(imageControl)
        val fadeOutSet = fadeOutControl.setupFadeOutAnimator()
        fadeOutSet.doOnEnd {
            if (!finished) startAnotherRoundOfFun()
            else {
                stopGamePanel()
                gameBrain.concludedGameLiveData.value = true
            }
        }
        return fadeOutSet
    }

    fun allViewsAreFadedOut(finished: Boolean) {
        setupFadeOutAnimator(finished).start()
    }

    fun stopGamePanel():Boolean {
        timer_view.stop()
        val total_time = timer_view.text.toString()
        val total_seconds = gameBrain.calculateTotalSeconds(total_time)
        if(total_seconds==0)return false
        gameBrain.chapterInfo.chapterTime = total_seconds.toString()
        return total_seconds != 0
    }

    fun startAnotherRoundOfFun() {
        imageControl.refreshPanelOfImages()
        playTarget()
    }
}