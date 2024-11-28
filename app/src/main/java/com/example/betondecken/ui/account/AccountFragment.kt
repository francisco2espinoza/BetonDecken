package com.example.betondecken.ui.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
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
        val userId = sharedPreferences.getInt("id_usuario", -1) // Recuperar como Int
        Log.d(Tools.LOGTAG, "ID de usuario obtenido de SharedPreferences: $userId")

        if (userId == -1) {
            Toast.makeText(requireContext(), "Usuario no encontrado en preferencias", Toast.LENGTH_SHORT).show()
            Log.e(Tools.LOGTAG, "No se encontró el id_usuario en SharedPreferences")
            return root
        }

        // Consultar información del usuario
        val dbHelper = DBHelper(requireContext())
        val usuario: Usuario? = dbHelper.getUsuarioInfo(userId) // Se pasa como Int

        if (usuario == null) {
            Toast.makeText(requireContext(), "No se encontró información del usuario", Toast.LENGTH_SHORT).show()
            Log.e(Tools.LOGTAG, "No se encontraron datos para el usuario con id_usuario: $userId")
            return root
        }

        // Actualizar TextViews con los datos del usuario
        root.findViewById<TextView>(R.id.tvNombreCompleto).text = usuario.nombre
        root.findViewById<TextView>(R.id.tvEmail).text = usuario.correo
        root.findViewById<TextView>(R.id.tvTelefono).text = usuario.telefono
        root.findViewById<TextView>(R.id.tvFechaRegistro).text = usuario.fecRegistro

        // Obtener datos de pedidos desde la base de datos
        val pedidoTotal = dbHelper.getTotalPedidoPeso(userId) // Actualizado
        val pedidoPendiente = dbHelper.getPendienteEntregaPeso(userId) // Actualizado

        // Actualizar TextViews con los datos de pedidos
        root.findViewById<TextView>(R.id.tvPedidoTotal).text = "$pedidoTotal Kg"
        root.findViewById<TextView>(R.id.tvPendienteEntrega).text = "$pedidoPendiente Kg"

        // Configurar botón de edición para editar datos del usuario
        root.findViewById<ImageView>(R.id.btnEditProfile).setOnClickListener {
            showEditProfileDialog(userId, dbHelper, root) // Se pasa userId como Int
        }

        return root
    }

    private fun showEditProfileDialog(idUsuario: Int, dbHelper: DBHelper, root: View) {
        Log.d(Tools.LOGTAG, "ID de usuario pasado a showEditProfileDialog: $idUsuario")

        // Crear un diálogo para editar el perfil
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)
        val editNombre = dialogView.findViewById<TextView>(R.id.editNombre)
        val editCorreo = dialogView.findViewById<TextView>(R.id.editCorreo)
        val editTelefono = dialogView.findViewById<TextView>(R.id.editTelefono)

        // Prellenar campos con datos actuales
        val usuario = dbHelper.getUsuarioInfo(idUsuario)
        if (usuario != null) {
            Log.d(Tools.LOGTAG, "Usuario encontrado para edición: ${usuario.nombre}")
            editNombre.text = usuario.nombre
            editCorreo.text = usuario.correo
            editTelefono.text = usuario.telefono
        } else {
            Log.e(Tools.LOGTAG, "No se encontró usuario para el ID: $idUsuario")
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Editar Perfil")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoNombre = editNombre.text.toString()
                val nuevoCorreo = editCorreo.text.toString()
                val nuevoTelefono = editTelefono.text.toString()

                // Actualizar la base de datos
                val filasActualizadas = dbHelper.updateUsuario(idUsuario, nuevoNombre, nuevoCorreo, nuevoTelefono)
                if (filasActualizadas > 0) {
                    Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()

                    // Refrescar los datos mostrados en la pantalla
                    root.findViewById<TextView>(R.id.tvNombreCompleto).text = nuevoNombre
                    root.findViewById<TextView>(R.id.tvEmail).text = nuevoCorreo
                    root.findViewById<TextView>(R.id.tvTelefono).text = nuevoTelefono
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar el perfil", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }
}
