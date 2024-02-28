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
        val yButton : Button = main.findViewById(R.id.yesButton)
        val nButton : Button = main.findViewById(R.id.noButton)
        nButton.setOnClickListener {
            dismiss()
        }
        yButton.setOnClickListener {
            gameBrain.cancelGameLiveData.value = true
            dismiss()

        }
        return main
    }


}
