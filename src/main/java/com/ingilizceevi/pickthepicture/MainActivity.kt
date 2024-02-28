package com.ingilizceevi.pickthepicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ingilizceevi.dataconnection.ConnectActivity
import com.ingilizceevi.dataconnection.FatherConnection

class MainActivity : AppCompatActivity() {
    var studentId:String? = ""
    var chapterId:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startSELECTQuery(0)
//        val game_intent = Intent(this, PictureActivity::class.java )
//        game_intent.putExtra("chapter", "chapter03")
//        game_intent.putExtra("studentId", "01")
//        startActivityForResult(game_intent, 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                chapterId = data?.getStringExtra("chapter")
                studentId = data?.getStringExtra("id")
                startGameActivity()
            }

        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //val false_answers_result = data?.getSerializableExtra("false_answers") as MutableList<String>
                //startINSERTQuery(false_answers_result)
                startSELECTQuery(0)
            }
        }
    }

    fun startGameActivity(){
        val game_intent = Intent(this, PictureActivity::class.java )
        game_intent.putExtra("chapter", chapterId)
        game_intent.putExtra("studentId", studentId)
        startActivityForResult(game_intent, 1)
    }
    fun startINSERTQuery(false_results:MutableList<String>){
        val myConnection = FatherConnection(studentId, chapterId, false_results)
        myConnection.execute("nameConnection")
    }
    fun startSELECTQuery(requestCode: Int){
        val intent = Intent(this, ConnectActivity::class.java)
        startActivityForResult(intent, requestCode)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}