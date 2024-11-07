//package com.example.betondecken.ui.analytic
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.betondecken.databinding.FragmentAnalyticBinding
//
//class AnalyticFragment : Fragment() {
//
//    private var _binding: FragmentAnalyticBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val analyticViewModel =
//            ViewModelProvider(this).get(AnalyticViewModel::class.java)
//
//        _binding = FragmentAnalyticBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textAnalytic1
//        analyticViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
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