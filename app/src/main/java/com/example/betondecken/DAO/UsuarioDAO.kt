package com.example.betondecken.DAO

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
//import com.example.betondecken.Model.Usuario
import com.example.betondecken.Util.Tools

class UsuarioDAO(myContext: Context) {

    private var dbHelper: DBHelper = DBHelper(myContext)

    fun insertarUsuario(nombre: String, usuario: String, password: String): Long {
        Log.i(Tools.LOGTAG, "ingresando a insertar usuario")

        val values = ContentValues().apply {
            put("nombre", nombre)
            put("id_usuario", usuario)
            put("password", password)
        }

        val db = dbHelper.writableDatabase

        return try {
            val indice = db.insert(Tools.TABLA_USUARIOS, null, values)
            Log.i(Tools.LOGTAG, "usuario indice: $indice")
            indice
        } catch (e: Exception) {
            throw DAOException("UsuariosDAO: Error al insertar: " + e.message)
        } finally {
            db.close()
        }
    }

    @SuppressLint("Range")
    fun fnValidarLogin(usuarioIn: String, passwordIn: String): Long {
        Log.i(Tools.LOGTAG, "ingresando a fnValidarLogin")

        var result: Long = 0
        val db = dbHelper.readableDatabase

        try {
            val c: Cursor = db.rawQuery(
                "SELECT id, nombre, id_usuario, password FROM ${Tools.TABLA_USUARIOS} WHERE id_usuario=? AND password=?",
                arrayOf(usuarioIn, passwordIn)
            )

            Log.i(Tools.LOGTAG, "cantidad: ${c.count}")

            if (c.count > 0) {
                c.moveToNext()
                do {
                    val id = c.getInt(c.getColumnIndex("id"))
                    val nombre = c.getString(c.getColumnIndex("nombre"))
                    val usuario = c.getString(c.getColumnIndex("id_usuario"))
                    val password = c.getString(c.getColumnIndex("password"))

                    if (usuario == usuarioIn && password == passwordIn) {
                        result = 1
                    }
                } while (c.moveToNext())
            }
            c.close()
        } catch (e: Exception) {
            throw DAOException("UsuarioDAO: Error al validar login: ${e.message}")
        } finally {
            db.close()
        }

        return result
    }

    @SuppressLint("Range")
//    fun fnObtenerIdUsuario(username: String, password: String): String? {
//        Log.i(Tools.LOGTAG, "Ingresando a fnObtenerIdUsuario")
//
//        val db = dbHelper.readableDatabase
//
//        return try {
//            val c: Cursor = db.rawQuery(
//                "SELECT id_usuario FROM ${Tools.TABLA_USUARIOS} WHERE id_usuario=? AND password=?",
//                arrayOf(username, password)
//            )
//
//            if (c.moveToFirst()) {
//                val idUsuario = c.getString(c.getColumnIndex("id_usuario"))
//                Log.i(Tools.LOGTAG, "id_usuario encontrado: $idUsuario")
//                idUsuario
//            } else {
//                Log.w(Tools.LOGTAG, "No se encontró el id_usuario para las credenciales proporcionadas")
//                null
//            }.also {
//                c.close()
//            }
//        } catch (e: Exception) {
//            throw DAOException("UsuarioDAO: Error al obtener id_usuario: ${e.message}")
//        } finally {
//            db.close()
//        }
//    }

    fun fnObtenerIdUsuario(username: String, password: String): Int? {
        Log.i(Tools.LOGTAG, "Ingresando a fnObtenerIdUsuario")

        val db = dbHelper.readableDatabase

        return try {
            val cursor = db.rawQuery(
                """
            SELECT id 
            FROM ${Tools.TABLA_USUARIOS} 
            WHERE id_usuario = ? AND password = ?
            """.trimIndent(),
                arrayOf(username, password)
            )

            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndex("id")) // Recuperar el campo `id` numérico
                Log.i(Tools.LOGTAG, "ID encontrado: $id")
                id
            } else {
                Log.w(Tools.LOGTAG, "No se encontró el ID para las credenciales proporcionadas")
                null
            }.also {
                cursor.close()
            }
        } catch (e: Exception) {
            throw DAOException("UsuarioDAO: Error al obtener ID: ${e.message}")
        } finally {
            db.close()
        }
    }


}
