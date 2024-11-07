package com.example.betondecken.ui.support

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.betondecken.MainActivity
import com.example.betondecken.R
import com.example.betondecken.TrackingActivity

class SupportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño para este fragmento
        val root = inflater.inflate(R.layout.fragment_support, container, false)

        // Referencias a los elementos del diseño
        val btnChat = root.findViewById<Button>(R.id.btn_chat)
        val btnCall = root.findViewById<Button>(R.id.btn_call)
        val etConsulta = root.findViewById<EditText>(R.id.et_consulta)
        val btnSend = root.findViewById<Button>(R.id.btn_send)

        // Acción para el botón "Chatea con un Técnico"
        btnChat.setOnClickListener {
            Toast.makeText(requireContext(), "Abriendo chat con soporte técnico...", Toast.LENGTH_SHORT).show()
        }

        // Acción para el botón "Llámanos"
        btnCall.setOnClickListener {
            Toast.makeText(requireContext(), "Llamando a soporte técnico...", Toast.LENGTH_SHORT).show()
        }

        // Acción para el botón "Enviar Consulta"
        btnSend.setOnClickListener {
            val consultaText = etConsulta.text.toString()
            if (consultaText.isNotEmpty()) {
                Toast.makeText(requireContext(), "Consulta enviada: $consultaText", Toast.LENGTH_SHORT).show()
                etConsulta.text.clear()
            } else {
                Toast.makeText(requireContext(), "Por favor, ingresa una consulta.", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }


}
