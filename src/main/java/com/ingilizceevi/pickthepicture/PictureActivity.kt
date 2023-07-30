package com.ingilizceevi.pickthepicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels

class PictureActivity : AppCompatActivity() {
    private val gameBrain: LaneViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        val bundle = intent.extras
        var chapter: String? = ""
        if (bundle != null) {
            chapter = bundle.getString("chapter")
            gameBrain.initiateModel(chapter!!)
            supportFragmentManager.beginTransaction()
                .replace(R.id.gamePanelView, GamePanel.newInstance(chapter!!), "gamePanel")
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        val view = supportFragmentManager.findFragmentById(R.id.gamePanelView) as GamePanel
        view.handleOnCancelButton().setOnClickListener {
            val data = Intent()
            setResult(RESULT_OK, data)
            finish()
        }
    }
}