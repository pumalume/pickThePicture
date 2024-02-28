package com.ingilizceevi.pickthepicture

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultsPanel.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultsPanel : Fragment() {
    // TODO: Rename and change types of parameters
    private var totalTime: String? = null
    private var clicks: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalTime = it.getString(ARG_PARAM1)
            clicks = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val main = inflater.inflate(R.layout.fragment_results_panel, container, false)
        val timeView = main.findViewById<TextView>(R.id.timeView)
        val clickView = main.findViewById<TextView>(R.id.clickView)
        timeView.text =  getString(R.string.totalTime, totalTime)
        clickView.text = getString(R.string.totalClicks, clicks.toString())
        return main
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            ResultsPanel().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}