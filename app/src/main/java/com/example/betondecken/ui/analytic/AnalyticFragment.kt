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
import com.example.betondecken.Util.Tools
import com.example.betondecken.databinding.FragmentAnalyticBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

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
            Toast.makeText(requireContext(), "Usuario no autenticado, por favor inicia sesión", Toast.LENGTH_SHORT).show()
            Log.e(Tools.LOGTAG, "El ID de usuario no es válido. Redirigiendo...")
            return binding.root
        }

        try {
            // Obtén las estadísticas de los últimos 5 meses
            val estadisticas = usuarioDAO.getEstadisticas(userId.toString())
            mostrarEstadisticas(estadisticas)

            // Obtén los últimos pedidos
            val ultimosPedidos = usuarioDAO.getUltimosPedidos(userId)
            mostrarUltimosPedidos(ultimosPedidos)

            // Configurar el gráfico de barras
            configurarBarChart(ultimosPedidos)

        } catch (e: Exception) {
            Log.e(Tools.LOGTAG, "Error al obtener datos: ${e.message}")
            Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun mostrarEstadisticas(estadisticas: List<Map<String, Any>>) {
        val textViews = listOf(
            binding.textView, binding.textView2, binding.textView3,
            binding.textView4, binding.textView5
        )

        for (i in estadisticas.indices) {
            val mes = estadisticas[i]["mes"] as String
            val pedidos = estadisticas[i]["pedidos"] as Int
            textViews[i].text = "$mes: $pedidos pedidos"
        }
    }

    private fun mostrarUltimosPedidos(ultimosPedidos: List<Map<String, String>>) {
        val pedidosTextViews = listOf(binding.textDespacho, binding.textTransito, binding.textEntregado)
        val fechasTextViews = listOf(binding.timeDespacho, binding.timeTransito, binding.timeEntregado)

        for (i in ultimosPedidos.indices) {
            pedidosTextViews[i].text = ultimosPedidos[i]["pedido"]
            fechasTextViews[i].text = ultimosPedidos[i]["fecha"]
        }
    }

    private fun configurarBarChart(ultimosPedidos: List<Map<String, String>>) {
        val barChart = binding.barChart

        // Lista de entradas para el gráfico
        val barEntries = ultimosPedidos.mapIndexed { index, pedido ->
            val peso = pedido["peso"]?.toFloatOrNull() ?: 0f // Asegura un valor numérico válido
            BarEntry(index.toFloat(), peso)
        }

        // Configuración del DataSet
        val barDataSet = BarDataSet(barEntries, "Últimos Pedidos")
        barDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        barDataSet.valueTextColor = android.graphics.Color.WHITE // Color de los valores en las barras
        barDataSet.valueTextSize = 12f // Tamaño del texto en las barras

        // Configuración del BarData
        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f
        barChart.data = barData

        // Configuración del eje X
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textColor = android.graphics.Color.WHITE // Cambia el color del texto del eje X
        xAxis.textSize = 12f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return ultimosPedidos.getOrNull(value.toInt())?.get("fecha") ?: ""
            }
        }

        // Configuración del eje Y izquierdo
        val yAxisLeft: YAxis = barChart.axisLeft
        yAxisLeft.textColor = android.graphics.Color.WHITE // Cambia el color del texto del eje Y izquierdo
        yAxisLeft.textSize = 12f
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.axisMinimum = 0f // Establece un valor mínimo para que las barras no queden ocultas

        // Deshabilitar el eje Y derecho
        val yAxisRight: YAxis = barChart.axisRight
        yAxisRight.isEnabled = false

        // Personalización adicional del gráfico
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.setFitBars(true)

        // Actualizar el gráfico
        barChart.invalidate()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
