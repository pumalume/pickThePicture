package com.ingilizceevi.pickthepicture


import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ingilizceevi.conceptbuilder.ConceptLoader

class LaneViewModel : ViewModel(){
    var numOfLanes: Int = 3
    private var gameBrainIsInitialized = false
    lateinit var masterCollectionDrawables: MutableMap<String, Drawable>
    lateinit var masterCollectionSounds: MutableMap<String, Uri>
    var myTargetConcept : String = "-1"
    private val availableConcepts: MutableList<String> = ArrayList(0)
    private val pileOfDiscardedConcepts:MutableList<String> = ArrayList(0)
    private val idealConceptualMap: MutableMap<Int, String> = mutableMapOf()

    private var clickedCounter = 0

    fun initiateModel(chapter:String){
        val concepts_array = ConceptLoader(chapter)
        concepts_array.loadDrawables()
        concepts_array.loadAudio()
        masterCollectionDrawables = concepts_array.theImages
        masterCollectionSounds = concepts_array.theSounds
        availableConceptsAreGenerated()
        conceptualMapsAreInitialized()
        schemaIsInitializeToBeginGame()
        gameBrainIsInitialized=true
    }

    fun isGameInitialized():Boolean{
        return gameBrainIsInitialized
    }

    fun getTargetUri(): Uri? {
        return masterCollectionSounds[myTargetConcept]
    }
    private fun availableConceptsAreGenerated() {
        masterCollectionDrawables.forEach{ el ->
            availableConcepts.add(el.key)
        }
        availableConcepts.shuffle()
    }
    private fun conceptualMapsAreInitialized(){
        for(i in 0 until numOfLanes){
            idealConceptualMap[i] = "-1"
        }
    }

    private fun conceptIsPulledFromAvailableConcepts():String{
        return if(availableConcepts.isNotEmpty()) availableConcepts.removeAt(0)
        else "-1"
    }
    private fun conceptIsRegisteredToIdealMapFromAvailableList(laneTag:Int):Boolean{
        idealConceptualMap[laneTag]=conceptIsPulledFromAvailableConcepts()
        return idealConceptualMap[laneTag] != "-1"
    }

    private fun conceptIsThrownToDiscardPile(laneTag:Int) {
        if (idealConceptualMap[laneTag] != null) {
            pileOfDiscardedConcepts.add(idealConceptualMap[laneTag]!!)
            idealConceptualMap[laneTag] = "-1"
        }
    }

    fun schemaIsInitializeToBeginGame():Boolean {
        for (i in 0 until numOfLanes) {
            if(!conceptIsRegisteredToIdealMapFromAvailableList(i))return false
        }
        return true
    }

    fun idealSchemaIsRecycled(){
        val tempList :MutableList<String> = ArrayList(0)
        for(i in 0 until numOfLanes) tempList.add(idealConceptualMap[i]!!)
        tempList.shuffle()
        for(i in 0 until numOfLanes) idealConceptualMap[i] = tempList[i]
    }

    private fun conceptIsReturnedToAvailableList(viewId:Int){
        val concept = idealConceptualMap[viewId]
        if (concept != "-1") availableConcepts.add(concept!!)
        availableConcepts.shuffle()
    }

    fun allImagesAreFinished():Boolean{
        for(i in 0 until numOfLanes){
            if(idealConceptualMap[i]!="-1")return false
        }
        return true
    }
    fun schemaForImageClickedTrue(viewId:Int):Boolean{
        for(index in 0 until numOfLanes) {
            if(index == viewId){
                conceptIsThrownToDiscardPile(index)
                conceptIsRegisteredToIdealMapFromAvailableList(index)
            }
            else{
                conceptIsReturnedToAvailableList(index)
                conceptIsRegisteredToIdealMapFromAvailableList(index)
            }
        }
        if(allImagesAreFinished())return false
        myTargetConceptIsSetFromIdealMap()
        return true
    }

    fun schemaForImageClickedFalse(){
        for(index in 0 until numOfLanes){
            val tempConcept = idealConceptualMap[index]
            if(tempConcept!=myTargetConcept) {
                conceptIsReturnedToAvailableList(index)
                conceptIsRegisteredToIdealMapFromAvailableList(index)
            }
        }
        idealSchemaIsRecycled()
    }

    fun getIdealLaneValue(laneTag:Int):String?{
        return idealConceptualMap[laneTag]
    }
    fun isTargetConceptTrue(laneTag: Int):Boolean{
        return idealConceptualMap[laneTag]==myTargetConcept
    }

    fun getRandomConceptFromDiscardedPile():String{
        val size = pileOfDiscardedConcepts.size
        val x = (0 until size).random()
        return pileOfDiscardedConcepts[x]
    }
    fun myTargetConceptIsSetFromIdealMap(){
        do{
            myTargetConcept = idealConceptualMap[(0 until numOfLanes).random()]!!
        } while(myTargetConcept=="-1")
    }

    private fun pictureIsPulledFromDiscardedPile():String{
        if (pileOfDiscardedConcepts.isEmpty())return "-1"
        val size = pileOfDiscardedConcepts.size
        return pileOfDiscardedConcepts[(0..size).random()]
    }

    fun getTotalClicks():Int{return clickedCounter}
    fun increaseClickedCounter(){
        clickedCounter++
    }

    fun calculateStudentTime(totalTime:String):Int{
        val parts = totalTime.split(":")
        val minutes= parts[0].toInt()
        var seconds=parts[1].toInt()
        seconds = seconds + (minutes * 60)
        return seconds
    }

    //the following three methods initiate the live data listeners
    val startGameLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val cancelGameLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}