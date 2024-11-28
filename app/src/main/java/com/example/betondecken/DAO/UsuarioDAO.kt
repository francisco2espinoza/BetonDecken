package com.example.betondecken.DAO
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.betondecken.Util.Tools

class UsuarioDAO(context: Context) {
    private val dbHelper: DBHelper = DBHelper(context)

    fun getEstadisticas(idUsuario: String): List<Map<String, Any>> {
            val db = dbHelper.readableDatabase
            val estadisticas = mutableListOf<Map<String, Any>>()
        // Log para verificar el ID del usuario
        Log.d(Tools.LOGTAG, "Iniciando getEstadisticas con idUsuario: $idUsuario")
            val cursor = db.rawQuery(
                """
            SELECT strftime('%m', p.fecha) as mes, count(p.id_tracking) as pedidos
            FROM ${Tools.TABLA_PEDIDOS} p
            WHERE p.id_usuario = ? 
            AND p.fecha >= strftime('%Y-%m-%d', 'now', '-5 months')  -- Últimos 5 meses
            GROUP BY strftime('%Y-%m', p.fecha)
            ORDER BY p.fecha DESC  -- Ordenar de más reciente a más antiguo
            LIMIT 5
            """.trimIndent(),
                arrayOf(idUsuario.toString())
            )

            if (cursor.moveToFirst()) {
                do {
                    val mesNumero = cursor.getString(cursor.getColumnIndexOrThrow("mes"))
                    val pedidos = cursor.getInt(cursor.getColumnIndexOrThrow("pedidos"))
                    val mesNombre = when (mesNumero) {
                        "01" -> "enero"
                        "02" -> "febrero"
                        "03" -> "marzo"
                        "04" -> "abril"
                        "05" -> "mayo"
                        "06" -> "junio"
                        "07" -> "julio"
                        "08" -> "agosto"
                        "09" -> "septiembre"
                        "10" -> "octubre"
                        "11" -> "noviembre"
                        "12" -> "diciembre"
                        else -> "desconocido"
                    }

                    estadisticas.add(
                        mapOf(
                            "mes" to mesNombre,
                            "pedidos" to pedidos
                        )
                    )
                } while (cursor.moveToNext())
            }

            cursor.close()
            return estadisticas
    }

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
