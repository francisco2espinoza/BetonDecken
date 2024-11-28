//package com.example.betondecken.ui.analytic
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.betondecken.DAO.DBHelper
//import com.example.betondecken.DAO.UsuarioDAO
//import com.example.betondecken.R
//import com.example.betondecken.databinding.FragmentAnalyticBinding
//
//class AnalyticFragment : Fragment() {
//
//    private var _binding: FragmentAnalyticBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var usuarioDAO: UsuarioDAO
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentAnalyticBinding.inflate(inflater, container, false)
//
//        // Inicializa UsuarioDAO con el contexto del fragmento
//        usuarioDAO = UsuarioDAO(requireContext())  // 'requireContext()' es el Context del Fragment
//
//        // Supongamos que el idUsuario está disponible, puedes obtenerlo de SharedPreferences o de algún otro lugar
//        val idUsuario = "1"  // Reemplaza esto con el id real del usuario
//
//        // Obtén las estadísticas de los últimos 5 meses
//        val estadisticas = usuarioDAO.getEstadisticas(idUsuario)
//
//        // Muestra los resultados en los TextView correspondientes
//        mostrarEstadisticas(estadisticas)
//
//        return binding.root
//    }
//
//    private fun mostrarEstadisticas(estadisticas: List<Map<String, Any>>) {
//        // Asumiendo que tienes un TextView para cada mes, actualiza el texto
//        val textViews = listOf(
//            binding.textView, binding.textView2, binding.textView3,
//            binding.textView4, binding.textView5
//        )
//
//        // Asegúrate de que no haya más de 5 meses
//        for (i in 0 until estadisticas.size) {
//            val mes = estadisticas[i]["mes"] as String
//            val pedidos = estadisticas[i]["pedidos"] as Int
//
//            // Actualiza cada TextView con el mes y los pedidos
//            textViews[i].text = "$mes: $pedidos pedidos"
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
//
package com.example.betondecken.ui.analytic

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.betondecken.DAO.UsuarioDAO
import com.example.betondecken.databinding.FragmentAnalyticBinding
import com.example.betondecken.Util.Tools

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

        // Inicializa UsuarioDAO
        usuarioDAO = UsuarioDAO(requireContext())

        // Obtener el ID del usuario desde SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("com.concreta_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id_usuario", -1)

        Log.d(Tools.LOGTAG, "ID de usuario obtenido para AnalyticFragment: $userId")

        if (userId == -1) {
            // ID de usuario no encontrado, muestra un mensaje y redirige si es necesario
            Toast.makeText(requireContext(), "Usuario no autenticado, por favor inicia sesión", Toast.LENGTH_SHORT).show()
            Log.e(Tools.LOGTAG, "El ID de usuario no es válido. Redirigiendo...")
            // Aquí podrías manejar la redirección si es necesario.
            return binding.root
        }

        try {
            // Obtén las estadísticas de los últimos 5 meses
            val estadisticas = usuarioDAO.getEstadisticas(userId.toString())

            // Muestra los resultados en los TextView correspondientes
            mostrarEstadisticas(estadisticas)

        } catch (e: Exception) {
            Log.e(Tools.LOGTAG, "Error al obtener estadísticas: ${e.message}")
            Toast.makeText(requireContext(), "Error al cargar estadísticas", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun mostrarEstadisticas(estadisticas: List<Map<String, Any>>) {
        val textViews = listOf(
            binding.textView, binding.textView2, binding.textView3,
            binding.textView4, binding.textView5
        )

        // Asegúrate de que no haya más de 5 meses
        for (i in estadisticas.indices) {
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
