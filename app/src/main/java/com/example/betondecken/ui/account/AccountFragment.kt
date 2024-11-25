package com.example.betondecken.ui.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.betondecken.DAO.DBHelper
import com.example.betondecken.Model.Usuario
import com.example.betondecken.R
import com.example.betondecken.Util.Tools

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account, container, false)

        // Recuperar el id_usuario desde SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("com.concreta_preferences", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences.getString("id_usuario", null)

        if (idUsuario == null) {
            Toast.makeText(requireContext(), "Usuario no encontrado en preferencias", Toast.LENGTH_SHORT).show()
            Log.e(Tools.LOGTAG, "No se encontró el id_usuario en SharedPreferences")
            return root
        }

        // Consultar información del usuario..
        val dbHelper = DBHelper(requireContext())
        val usuario: Usuario? = dbHelper.getUsuarioInfo(idUsuario)

        if (usuario == null) {
            Toast.makeText(requireContext(), "No se encontró información del usuario", Toast.LENGTH_SHORT).show()
            Log.e(Tools.LOGTAG, "No se encontraron datos para el usuario con id_usuario: $idUsuario")
            return root
        }

        // Actualizar TextViews con los datos del usuario
        root.findViewById<TextView>(R.id.tvNombreCompleto).text = usuario.nombre
        root.findViewById<TextView>(R.id.tvEmail).text = usuario.correo
        root.findViewById<TextView>(R.id.tvTelefono).text = usuario.telefono
        root.findViewById<TextView>(R.id.tvFechaRegistro).text = usuario.fecRegistro

        // Actualizar datos simulados de pedidos (puedes reemplazar esto por datos reales)
        root.findViewById<TextView>(R.id.tvPedidoTotal).text = "1000 Kg" // Ajusta esto con datos reales si es necesario
        root.findViewById<TextView>(R.id.tvPendienteEntrega).text = "500 Kg"

        return root
    }
}
