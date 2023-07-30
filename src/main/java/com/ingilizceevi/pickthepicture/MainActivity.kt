package com.ingilizceevi.pickthepicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ingilizceevi.dataconnection.ConnectActivity

class MainActivity : AppCompatActivity() {
    var studentID:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, ConnectActivity::class.java)
        startActivityForResult(intent, 0)
    }

//        val bundle = intent.extras
//        var myChapter: String? = "01"
//        if (bundle != null) {
//            myChapter = bundle.getString("myChapter")
//            studentID= bundle.getInt("studentID")
//        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Get the result from intent
                val chapter_result = data?.getStringExtra("chapter")
                studentID = data?.getStringExtra("id")
                val game_intent = Intent(this, PictureActivity::class.java )
                game_intent.putExtra("chapter", chapter_result)
                startActivityForResult(game_intent, 1)

            }

        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val intent = Intent(this, ConnectActivity::class.java )
                startActivityForResult(intent, 0)
            }

        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}