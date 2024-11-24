package com.example.betondecken.DAO
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.betondecken.Model.Usuario
import com.example.betondecken.Util.Tools

class UsuarioDAO(myContext: Context) {

    private var dbHelper: DBHelper = DBHelper(myContext)

    fun insertarUsuario( nombre: String, usuario: String, password: String): Long{

        Log.i(Tools.LOGTAG,"ingresando a insertar usuario")

        val indice: Long

        val values = ContentValues().apply {
            put("nombre", nombre)
            put("id_usuario", usuario)
            put("password",password)
        }

        val db = dbHelper.writableDatabase

        try {

            indice = db.insert(Tools.TABLA_USUARIOS, null, values)

            Log.i(Tools.LOGTAG,"usuario indice: " + indice.toString())

            return indice
        }catch (e: Exception){
            throw DAOException("UsuariosDAO: Error al insertar: " + e.message)
        }finally {
            db.close()
        }

    }

    @SuppressLint("Range")
    fun fnValidarLogin(usuarioIn: String, passwordIn: String): Long{
        Log.i(Tools.LOGTAG,"ingresando a fnValidarLogin")

        var result: Long

        val db = dbHelper.readableDatabase

        val modelo = Usuario()

        result = 0

        try {
            val c: Cursor = db.rawQuery("select id, nombre, id_usuario, password from " + Tools.TABLA_USUARIOS + " WHERE id_usuario='$usuarioIn' and password='$passwordIn'", null)

            Log.i(Tools.LOGTAG,"cantidad: " + c.count)

            if (c.count > 0){

                c.moveToNext()
                do{
                    val id: Int         = c.getInt(c.getColumnIndex("id"))
                    val nombre: String  = c.getString(c.getColumnIndex("nombre"))
                    val usuario:String  = c.getString(c.getColumnIndex("id_usuario"))
                    val password: String= c.getString(c.getColumnIndex("password"))

                    modelo.id           = id
                    modelo.nombre       = nombre
                    modelo.usuario      = usuario
                    modelo.passwoord    = password

                    if (modelo.usuario == usuarioIn && modelo.passwoord == passwordIn){
                        result = 1
                    }

                }while(c.moveToNext())

            }
            c.close()
        }catch (e: Exception){
            throw DAOException("GeneroMusicalDAO: Error al obtener: " + e.message)
        }finally {
            db.close()
        }

        return result


    }

}