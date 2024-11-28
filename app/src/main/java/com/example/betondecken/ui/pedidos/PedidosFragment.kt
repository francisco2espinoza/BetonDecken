package com.example.betondecken.ui.pedidos

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betondecken.DAO.DBHelper
import com.example.betondecken.R
import com.example.betondecken.Util.Tools
import com.example.betondecken.adapters.Pedido
import com.example.betondecken.adapters.PedidosAdapter

class PedidosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pedidos, container, false)

        // Recuperar el id_usuario desde SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("com.concreta_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id_usuario", -1)
        Log.d(Tools.LOGTAG, "ID de usuario obtenido para PedidosFragment: $userId")

        if (userId == -1) {
            Toast.makeText(requireContext(), "Usuario no encontrado en preferencias", Toast.LENGTH_SHORT).show()
            Log.e(Tools.LOGTAG, "No se encontró el id_usuario en SharedPreferences")
            return root
        }

        // Configurar RecyclerView
        val rvPedidos = root.findViewById<RecyclerView>(R.id.rvPedidos)
        rvPedidos.layoutManager = LinearLayoutManager(context)

        // Obtener pedidos desde la base de datos
        val dbHelper = DBHelper(requireContext())
        val cursor = dbHelper.getPedidos(userId.toString()) // Se pasa el ID numérico del usuario

        val pedidosList = mutableListOf<Pedido>()
        while (cursor.moveToNext()) {
            val nPedido = cursor.getInt(0) // id del pedido
            val fecha = cursor.getString(1) // fecha del pedido
            val descripcion = cursor.getString(2) // descripción del pedido
            val peso = cursor.getDouble(3) // peso del pedido
            val estado = cursor.getString(4) ?: "Estado desconocido" // estado del pedido
            pedidosList.add(Pedido(nPedido, fecha, descripcion, peso, estado))
        }
        cursor.close()

        // Configurar adaptador
        rvPedidos.adapter = PedidosAdapter(pedidosList)

        // Depuración de pedidos cargados
        if (pedidosList.isNotEmpty()) {
            Log.d(Tools.LOGTAG, "Pedidos cargados: $pedidosList")
        } else {
            Toast.makeText(requireContext(), "No se encontraron pedidos para el usuario", Toast.LENGTH_SHORT).show()
            Log.w(Tools.LOGTAG, "No se encontraron pedidos para el usuario con ID: $userId")
        }

        return root
    }
}
