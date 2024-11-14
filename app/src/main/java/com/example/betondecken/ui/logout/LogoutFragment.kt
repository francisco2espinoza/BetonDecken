// LogoutFragment.kt
package com.example.betondecken.ui.logout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.betondecken.ui.login.LoginActivity

class LogoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Lógica de cierre de sesión
        performLogout()

        // Redirige a LoginActivity después de cerrar sesión
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        // Retorna `null` porque este fragmento no necesita un layout
        return null
    }

    private fun performLogout() {
        // Coloca aquí la lógica de cierre de sesión, por ejemplo:
        // - Limpiar SharedPreferences
        // - Cerrar sesión en Firebase Auth, si lo usas
        // - Eliminar tokens de autenticación, etc.
    }
}
