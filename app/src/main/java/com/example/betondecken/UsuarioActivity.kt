//package com.example.betondecken
//
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.EditText
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.betondecken.DAO.DAOException
//import com.example.betondecken.DAO.UsuarioDAO
//import com.example.betondecken.Util.Tools
//
//class UsuarioActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_usuario)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//
//    fun fnGrabarNuevoUsuario(view: View){
//
//        val nombre = findViewById<EditText>(R.id.editTextNombres)
//        val usuario = findViewById<EditText>(R.id.editTextEmail)
//        val password = findViewById<EditText>(R.id.editTextPassword)
//
//        val dao = UsuarioDAO(baseContext)
//
//        try {
//            val indice = dao.insertarUsuario(nombre.text.toString(), usuario.text.toString(), password.text.toString())
//
//            if (indice > 0){
//                nombre.setText("")
//                usuario.setText("")
//                password.setText("")
//
//                Toast.makeText(baseContext, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
//            }
//            else{
//                Toast.makeText(baseContext, "Ocurrió un error al crear el usuario", Toast.LENGTH_SHORT).show()
//            }
//        }catch (e: DAOException){
//            Log.i(Tools.LOGTAG, "====> " + e.message)
//        }
//
//
//    }
//}

package com.example.betondecken

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.betondecken.DAO.DAOException
import com.example.betondecken.DAO.UsuarioDAO
import com.example.betondecken.Util.Tools
import com.example.betondecken.ui.login.LoginActivity

class UsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun fnGrabarNuevoUsuario(view: View) {
        val nombre = findViewById<EditText>(R.id.editTextNombres)
        val usuario = findViewById<EditText>(R.id.editTextEmail)
        val password = findViewById<EditText>(R.id.editTextPassword)

        val dao = UsuarioDAO(baseContext)

        try {
            val indice = dao.insertarUsuario(
                nombre.text.toString(),
                usuario.text.toString(),
                password.text.toString()
            )

            if (indice > 0) {
                // Limpiar los campos después de registrar al usuario
                nombre.setText("")
                usuario.setText("")
                password.setText("")

                Toast.makeText(baseContext, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()

                // Redirigir a LoginActivity después de 2 segundos
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Finalizar UsuarioActivity
                }, 2000) // 2000ms = 2 segundos
            } else {
                Toast.makeText(baseContext, "Ocurrió un error al crear el usuario", Toast.LENGTH_SHORT).show()
            }
        } catch (e: DAOException) {
            Log.i(Tools.LOGTAG, "====> ${e.message}")
        }
    }
}
