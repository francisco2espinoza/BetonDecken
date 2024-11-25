//package com.example.betondecken.ui.pedidos
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.betondecken.DAO.DBHelper
//import com.example.betondecken.R
//import com.example.betondecken.adapters.PedidosAdapter
////import com.example.betondecken.database.DatabaseHelper
//import com.example.betondecken.adapters.Pedido
//
//class PedidosFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val root = inflater.inflate(R.layout.fragment_pedidos, container, false)
//
//        // Configurar RecyclerView
//        val rvPedidos = root.findViewById<RecyclerView>(R.id.rvPedidos)
//        rvPedidos.layoutManager = LinearLayoutManager(context)
//
//        // Obtener datos del usuario logeado
//        val usuario = "usuario_logeado" // Cambiar por el usuario obtenido
//
//        // Obtener pedidos desde la base de datos
//        val dbHelper = DBHelper(requireContext())
//        val cursor = dbHelper.getPedidos(usuario)
//
//        val pedidosList = mutableListOf<Pedido>()
//        while (cursor.moveToNext()) {
//            val nPedido = cursor.getInt(0)
//            val fecha = cursor.getString(1)
//            val producto = cursor.getString(2)
//            val peso = cursor.getInt(3)
//            pedidosList.add(Pedido(nPedido, fecha, producto, peso))
//        }
//        cursor.close()
//
//        // Configurar adaptador
//        rvPedidos.adapter = PedidosAdapter(pedidosList)
//
//        return root
//    }
//}


package com.example.betondecken.ui.pedidos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betondecken.DAO.DBHelper
import com.example.betondecken.R
import com.example.betondecken.Util.Tools
import com.example.betondecken.adapters.Pedido
import com.example.betondecken.adapters.PedidosAdapter

class PedidosFragment : Fragment() {

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pedidos, container, false)

        // Recuperar el id_usuario desde SharedPreferences.
        val sharedPreferences = requireContext().getSharedPreferences("com.concreta_preferences", android.content.Context.MODE_PRIVATE)
        val emailUsuario = sharedPreferences.getString("id_usuario", null)

        if (emailUsuario == null) {
            Log.e(Tools.LOGTAG, "No se encontró un usuario logeado")
            return root
        }

        // Configurar RecyclerView
        val rvPedidos = root.findViewById<RecyclerView>(R.id.rvPedidos)
        rvPedidos.layoutManager = LinearLayoutManager(context)

        // Agregar un pedido de prueba (temporalmente)
//        val dbHelper = DBHelper(requireContext())
//        dbHelper.insertPedido(
//            idUsuario = idUsuario,
//            idTracking = "PRUEBA_TRACKING",
//            fecha = "2024-11-24",
//            descripcion = "Pedido de prueba para el usuario $idUsuario",
//            peso = 5.0,
//            estado = 1
//        )
//        Log.i(Tools.LOGTAG, "Pedido de prueba insertado para el usuario $idUsuario")

        // Consultar los pedidos desde la base de datos
        val dbHelper = DBHelper(requireContext())
        val cursor = dbHelper.getPedidos(emailUsuario)

        val pedidosList = mutableListOf<Pedido>()
        while (cursor.moveToNext()) {
            val nPedido = cursor.getInt(cursor.getColumnIndex("id"))
            val fecha = cursor.getString(cursor.getColumnIndex("fecha"))
            val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
            val peso = cursor.getInt(cursor.getColumnIndex("peso"))
            pedidosList.add(Pedido(nPedido, fecha, descripcion, peso))
        }
        cursor.close()

        // Configurar adaptador
        rvPedidos.adapter = PedidosAdapter(pedidosList)

        // Depuración de pedidos cargados
        if (pedidosList.isNotEmpty()) {
            Log.i(Tools.LOGTAG, "Pedidos cargados: $pedidosList")
        } else {
            Log.w(Tools.LOGTAG, "No se encontraron pedidos para el usuario $emailUsuario")
        }

        return root
    }
}

