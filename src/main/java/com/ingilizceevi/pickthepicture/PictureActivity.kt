package com.ingilizceevi.pickthepicture

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ingilizceevi.dataconnection.FatherConnection

class PictureActivity : AppCompatActivity() {
    private val gameBrain: LaneViewModel by viewModels()
    var chapter: String? = ""
    var student:String?=""
    val dialog = DialogFragment()
    lateinit var cancelButton:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        if (bundle != null){
            chapter = bundle.getString("chapter")
            student = bundle.getString("studentId")
        }
        if(!gameBrain.gameBrainIsInitialized)gameBrain.initiateModel(chapter!!)
        setContentView(R.layout.activity_picture)
        initializeTheGame()
        observerIsEstablishedForGameConcluded()
        observerIsEstablishedForGameCancelled()
    }

    override fun onResume() {
        super.onResume()
        setOnClick()
    }
    fun initializeTheGame(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.gamePanelView, GamePanel(), "gamePanel")
            .commit()
    }


    fun stopGameTimer():Boolean {
        val panel = supportFragmentManager.findFragmentById(R.id.gamePanelView) as GamePanel
        return panel.stopGamePanel()
    }
    fun showResultsPanel(){
        val t = supportFragmentManager.findFragmentById(R.id.gamePanelView) as GamePanel
        t.soundPlayer.endThePlayer()
        val tt = gameBrain.chapterInfo.chapterTime
        val tc = gameBrain.getTotalClicks()
        supportFragmentManager.beginTransaction()
            .replace(R.id.gamePanelView, ResultsPanel.newInstance(tt, tc))
            .commit()
        sendDataToFather()
        setExitClick()
    }

    fun setOnClick(){
        cancelButton = findViewById(R.id.cancelButton)
        cancelButton.bringToFront()
        cancelButton.setOnClickListener{
            val ft = supportFragmentManager.beginTransaction()
            dialog.show(ft, "Tag")
        }
    }
    fun setExitClick(){
        cancelButton.setOnClickListener {
            closeDownPictureActivity()
        }
    }
    fun observerIsEstablishedForGameConcluded() {
        val gameIsConcludedObserver = Observer<Boolean> {
            showResultsPanel()
        }
        gameBrain.concludedGameLiveData.observe(this, gameIsConcludedObserver)
    }
    fun observerIsEstablishedForGameCancelled() {
        val gameIsCancelledObserver = Observer<Boolean> {
            if(it)dialog.dismiss()
            if(stopGameTimer()) showResultsPanel()
            closeDownPictureActivity()
        }
        gameBrain.cancelGameLiveData.observe(this, gameIsCancelledObserver)
    }

    fun closeDownPictureActivity(){
        val falseAnswers = ArrayList(gameBrain.chapterInfo.getFinalResultsArray())
        val intent = Intent(this, PictureActivity::class.java)
        intent.putExtra("false_answers", falseAnswers);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    fun sendDataToFather(){
        val false_results = gameBrain.chapterInfo.getFinalResultsArray()
        val myConnection = FatherConnection(student, chapter, false_results)
        myConnection.execute("nameConnection")
    }
}