package com.example.betondecken.ui.tracking

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.betondecken.R
import com.example.betondecken.TrackingActivity
import com.example.betondecken.databinding.FragmentTrackingBinding

class TrackingFragment : Fragment() {

    private var _binding: FragmentTrackingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflar el diseño para este fragmento
        val root1 = inflater.inflate(R.layout.fragment_tracking, container, false)

        // Referencias a los elementos del diseño
        val btnTracking = root1.findViewById<Button>(R.id.btnTracking)

        val homeViewModel =
            ViewModelProvider(this).get(TrackingViewModel::class.java)

        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTracking
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val btn: Button = binding.btnTracking

        btnTracking.setOnClickListener {
            setup()
        }
        //setup()

        return root1

    }

    private fun setup(){


            var intent = Intent(activity,TrackingActivity::class.java)
            startActivity(intent)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}