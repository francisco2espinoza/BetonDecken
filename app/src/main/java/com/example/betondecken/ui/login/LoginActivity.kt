package com.example.betondecken.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.betondecken.DAO.DAOException
import com.example.betondecken.DAO.UsuarioDAO
import com.example.betondecken.MainActivity
import com.example.betondecken.databinding.ActivityLoginBinding

import com.example.betondecken.R
import com.example.betondecken.Util.Tools

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.btnLogin
        val loading = binding.loading

        preferences = getSharedPreferences("com.concreta_preferences", Context.MODE_PRIVATE);

        Log.i(Tools.LOGTAG, "lectura de preferencia: " + preferences.getBoolean("RecordarPassword",false).toString())

        if (preferences.getBoolean("RecordarPassword",false)){
            //mCbxRecordarPassword.setChecked(true);
            username.setText(preferences.getString("LastUser",""));
            password.setText(preferences.getString("LastPassword",""));

            //Log.i(Tools.LOGTAG,"LastUser: " + preferences.getString("LastUser",""))
            //Log.i(Tools.LOGTAG,"LastPassword: " + preferences.getString("LastPassword",""))

        }


        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful

            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                /*loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
                */

                loginSuccessfullAndOpen( )
                /**
                Toast.makeText(
                    applicationContext,
                    "HOLA, ES UNA PREUBA DEL CLIC",
                    Toast.LENGTH_LONG
                ).show()
                */
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun loginSuccessfullAndOpen() {

        val username = binding.username.text.toString()
        val password = binding.password.text.toString()

        val dao = UsuarioDAO(baseContext)

        try {
            Log.i(Tools.LOGTAG, "Intentando login con username: $username y password: $password")

            // Validar existencia del usuario
            //val idUsuario = dao.fnObtenerIdUsuario(username, password)
            val userId = dao.fnObtenerIdUsuario(username, password)

//            if (idUsuario != null) {
            if (userId != null) {
                Log.i(Tools.LOGTAG, "Login exitoso para el usuario: $userId")

                // Guardar id_usuario en SharedPreferences
                val editorPreferences = preferences.edit()
                //editorPreferences.putString("id_usuario", idUsuario) // Guardamos el id_usuario.
                editorPreferences.putInt("id_usuario", userId) // Guardar el ID numérico del usuario
                editorPreferences.putString("LastUser", username)
                editorPreferences.putString("LastPassword", password)
                editorPreferences.putBoolean("RecordarPassword", true)
                editorPreferences.apply()

                Log.i(Tools.LOGTAG, "id_usuario guardado en SharedPreferences")

                // Navegar a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            Log.e(Tools.LOGTAG, "Error al validar login: ${e.message}")
            throw DAOException("UsuarioDAO: Error al validar login: ${e.message}")
        }
    }

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}