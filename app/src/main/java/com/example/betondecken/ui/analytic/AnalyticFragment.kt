package com.example.betondecken.ui.analytic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.betondecken.DAO.DBHelper
import com.example.betondecken.DAO.UsuarioDAO
import com.example.betondecken.R
import com.example.betondecken.databinding.FragmentAnalyticBinding

class AnalyticFragment : Fragment() {

    private var _binding: FragmentAnalyticBinding? = null
    private val binding get() = _binding!!

    private lateinit var usuarioDAO: UsuarioDAO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticBinding.inflate(inflater, container, false)

        // Inicializa el DBHelper y el UsuarioDAO
        val dbHelper = DBHelper(requireContext())  // Asegúrate de que DBHelper está configurado correctamente
        usuarioDAO = UsuarioDAO(dbHelper)

        // Supongamos que el idUsuario está disponible, puedes obtenerlo de SharedPreferences o de algún otro lugar
        val idUsuario = "usuario_123"  // Reemplaza esto con el id real del usuario

        // Obtén las estadísticas de los últimos 5 meses
        val estadisticas = usuarioDAO.getEstadisticas(idUsuario)  // Llama al método en la instancia de UsuarioDAO

        // Muestra los resultados en los TextView correspondientes
        mostrarEstadisticas(estadisticas)

        return binding.root
    }

    private fun mostrarEstadisticas(estadisticas: List<Map<String, Any>>) {
        // Asumiendo que tienes un TextView para cada mes, actualiza el texto
        val textViews = listOf(
            binding.textView, binding.textView2, binding.textView3,
            binding.textView4, binding.textView5
        )

        // Asegúrate de que no haya más de 5 meses
        for (i in 0 until estadisticas.size) {
            val mes = estadisticas[i]["mes"] as String
            val pedidos = estadisticas[i]["pedidos"] as Int

            // Actualiza cada TextView con el mes y los pedidos
            textViews[i].text = "$mes: $pedidos pedidos"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

