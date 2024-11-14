package com.example.betondecken.ui.analytic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.betondecken.R
import com.example.betondecken.databinding.FragmentAnalyticBinding

class AnalyticFragment : Fragment() {

    private var _binding: FragmentAnalyticBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            //
            return inflater.inflate(R.layout.fragment_analytic, container, false)
        }
}