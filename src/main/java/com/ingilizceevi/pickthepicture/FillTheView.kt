package com.ingilizceevi.pickthepicture

import android.app.Activity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isNotEmpty
import androidx.core.view.setMargins
import androidx.core.view.setPadding

class FillTheView(activity: Activity) {
    val activity = activity
    val namesListView:LinearLayout = activity.findViewById(R.id.myNamesList)

    fun addChapterList(myMap : List<StudentInfo>){
        if(namesListView.isNotEmpty())namesListView.removeAllViews()
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(15)
        for ( i in 0 until myMap.size){
            val tempText = TextView(activity)
            tempText.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tempText.layoutParams = lp
            tempText.setPadding(10)
            tempText.text = myMap[i].studentName.toString()
            tempText.textSize = 30f
            namesListView.addView(tempText)
        }
    }

}