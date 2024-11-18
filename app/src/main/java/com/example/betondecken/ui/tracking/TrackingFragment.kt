package com.example.betondecken.ui.tracking

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
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

        val homeViewModel =
            ViewModelProvider(this).get(TrackingViewModel::class.java)

        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        val bindingRoot: View = binding.root


        val textView: TextView = binding.textTracking
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        // Referencias a los elementos del diseño

        val btnTracking = root1.findViewById<Button>(R.id.btnTracking)
        btnTracking.setOnClickListener {
            setupTracking()
        }





        val btn: Button = binding.btnTracking

//        btnTracking.setOnClickListener {
//            setup()
//        }
        //setup()
        setupButtonListeners(root1)
        return root1

    }

    private fun setupTracking(){


            var intent = Intent(activity,TrackingActivity::class.java)
            startActivity(intent)


    }
    private fun setupButtonListeners(view: View) {
        // Botón Pedidos
        view.findViewById<ImageButton>(R.id.btn_pedidos).setOnClickListener {
            // Navegación hacia el fragmento de Pedidos
            findNavController().navigate(R.id.action_nav_tracking_to_nav_pedidos)
        }
        // Botón Contacta a Soporte
        view.findViewById<ImageButton>(R.id.btn_contacto).setOnClickListener {
            // Navegación hacia el fragmento de Soporte
            findNavController().navigate(R.id.action_nav_tracking_to_nav_support)
        }

        // Botón Servicio al Cliente
        view.findViewById<ImageButton>(R.id.btn_servicio).setOnClickListener {
            // Navegación hacia el fragmento de Servicio al Cliente
            findNavController().navigate(R.id.action_nav_tracking_to_nav_servicio)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}