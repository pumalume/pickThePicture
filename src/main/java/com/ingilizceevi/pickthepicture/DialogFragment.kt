package com.ingilizceevi.pickthepicture


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

class DialogFragment : DialogFragment() {
    private val gameBrain: LaneViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val main = inflater.inflate(R.layout.fragment_dialog, container, false)
        val button : Button = main.findViewById(R.id.startButton)
        button.setOnClickListener {
            dismiss()
            gameBrain.startGameLiveData.value = true
        }
        return main
    }


}
