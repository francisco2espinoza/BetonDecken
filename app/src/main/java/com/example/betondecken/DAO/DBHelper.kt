//package com.example.betondecken.DAO
//
//import android.annotation.SuppressLint
//import android.content.ContentValues
//import android.content.Context
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//import com.example.betondecken.Model.Usuario
//import com.example.betondecken.Util.Tools
//
//class DBHelper(myContext: Context) : SQLiteOpenHelper(myContext, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    companion object {
//        private const val DATABASE_NAME = "concreta_db" // Nombre de la base de datos
//        private const val DATABASE_VERSION = 2         // Versión de la base de datos
//    }
//
//    override fun onCreate(db: SQLiteDatabase) {
//        // Crear la tabla de usuarios
//        val sql_usuario = """
//            CREATE TABLE ${Tools.TABLA_USUARIOS} (
//                id INTEGER PRIMARY KEY AUTOINCREMENT,
//                nombre TEXT,
//                id_usuario TEXT UNIQUE, -- id_usuario debe ser único para relacionarlo
//                password TEXT,
//                correo TEXT,
//                telefono TEXT,
//                fec_registro DATE
//            )
//        """
//        db.execSQL(sql_usuario)
//
//        // Crear la tabla de pedidos.
//        val sql_pedido = """
//            CREATE TABLE ${Tools.TABLA_PEDIDOS} (
//                id INTEGER PRIMARY KEY AUTOINCREMENT,
//                id_usuario TEXT NOT NULL,  -- Relación con TABLA_USUARIOS
//                id_tracking TEXT,
//                fecha DATE,
//                descripcion TEXT NOT NULL,
//                id_producto INTEGER,
//                peso NUMERIC,
//                estado INTEGER,
//                FOREIGN KEY (id_usuario) REFERENCES ${Tools.TABLA_USUARIOS}(id_usuario),
//                FOREIGN KEY (id_producto) REFERENCES ${Tools.TABLA_PRODUCTOS}(id_producto))
//         """
//        db.execSQL(sql_pedido)
//
//        // Crear la tabla de productos
//        val sql_producto = """
//            CREATE TABLE ${Tools.TABLA_PRODUCTOS}(
//                id_producto INTEGER PRIMARY KEY AUTOINCREMENT,
//                des_producto TEXT,
//                peso_kg INTEGER)
//        """
//        db.execSQL(sql_producto)
//
//        // Crear la tabla de estados de pedido
//        val sql_estado_pedido = """
//            CREATE TABLE ${Tools.TABLA_ESTADOS_PEDIDO} (
//                cod_estado TEXT PRIMARY KEY,
//                des_estado TEXT)
//        """
//        db.execSQL(sql_estado_pedido)
//
//        // Crear la tabla de trazabilidad de pedidos
//        val sql_pedido_trazabilidad = """
//            CREATE TABLE ${Tools.TABLA_TRAZABILIDAD_PEDIDOS} (
//                n_pedido INTEGER NOT NULL,
//                fec_trazabilidad DATE,
//                cod_estado TEXT NOT NULL,
//                PRIMARY KEY (n_pedido, cod_estado),
//                FOREIGN KEY (n_pedido) REFERENCES ${Tools.TABLA_PEDIDOS} (id),
//                FOREIGN KEY (cod_estado) REFERENCES ${Tools.TABLA_ESTADOS_PEDIDO} (cod_estado))
//        """
//        db.execSQL(sql_pedido_trazabilidad)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        // Borrar tablas existentes al actualizar la base de datos
//        db.execSQL("DROP TABLE IF EXISTS ${Tools.TABLA_USUARIOS}")
//        db.execSQL("DROP TABLE IF EXISTS ${Tools.TABLA_PEDIDOS}")
//        db.execSQL("DROP TABLE IF EXISTS ${Tools.TABLA_PRODUCTOS}")
//        db.execSQL("DROP TABLE IF EXISTS ${Tools.TABLA_ESTADOS_PEDIDO}")
//        db.execSQL("DROP TABLE IF EXISTS ${Tools.TABLA_TRAZABILIDAD_PEDIDOS}")
//        onCreate(db)
//    }
//
//
//    // Método para obtener pedidos de un usuario específico
//    fun getPedidos(userId: String): Cursor {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(
//            """
//        SELECT p.id, p.fecha, p.descripcion, p.peso, e.des_estado
//        FROM ${Tools.TABLA_PEDIDOS} p
//        LEFT JOIN ${Tools.TABLA_ESTADOS_PEDIDO} e ON p.estado = e.cod_estado
//        WHERE p.id_usuario = ?
//        """.trimIndent(),
//            arrayOf(userId.toString())
//        )
//        return cursor
//    }
//
//
//    // Método para actualizar los datos de un usuario
//    fun updateUsuario(idUsuario: Int, nombre: String, correo: String, telefono: String): Int {
//        val db = this.writableDatabase
//        val values = ContentValues().apply {
//            put("nombre", nombre)
//            put("correo", correo)
//            put("telefono", telefono)
//        }
//        return db.update(Tools.TABLA_USUARIOS, values, "id = ?", arrayOf(idUsuario.toString()))
//    }
//
//    // Método para calcular el peso total de pedidos realizados por un usuario
//    fun getTotalPedidoPeso(userId: Int): Double {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(
//            """
//            SELECT SUM(peso)
//            FROM ${Tools.TABLA_PEDIDOS}
//            WHERE id_usuario = ?
//            """.trimIndent(),
//            arrayOf(userId.toString())
//        )
//        var total = 0.0
//        if (cursor.moveToFirst()) {
//            total = cursor.getDouble(0)
//        }
//        cursor.close()
//        return total
//    }
//
//    // Método para calcular el peso de pedidos pendientes de entrega
//    fun getPendienteEntregaPeso(userId: Int): Double {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(
//            """
//            SELECT SUM(peso)
//            FROM ${Tools.TABLA_PEDIDOS}
//            WHERE id_usuario = ? AND estado != 4 -- Estado 4 = Entregado
//            """.trimIndent(),
//            arrayOf(userId.toString())
//        )
//        var pendiente = 0.0
//        if (cursor.moveToFirst()) {
//            pendiente = cursor.getDouble(0)
//        }
//        cursor.close()
//        return pendiente
//    }
//
//    // Método para obtener la información de un usuario específico
//    @SuppressLint("Range")
//    fun getUsuarioInfo(id: Int): Usuario? {
//        val db = this.readableDatabase
//        var usuario: Usuario? = null
//
//        try {
//            val cursor = db.rawQuery(
//                """
//            SELECT nombre, correo, telefono, fec_registro
//            FROM ${Tools.TABLA_USUARIOS}
//            WHERE id = ?
//            """.trimIndent(),
//                arrayOf(id.toString())
//            )
//
//            if (cursor.moveToFirst()) {
//                usuario = Usuario(
//                    id = id.toString(), // No se usa el campo `id` en esta consulta.
//                    nombre = cursor.getString(cursor.getColumnIndex("nombre")) ?: "No disponible",
//                    usuario = "N/A",
//                    correo = if (cursor.isNull(cursor.getColumnIndex("correo"))) "No disponible" else cursor.getString(cursor.getColumnIndex("correo")),
//                    telefono = if (cursor.isNull(cursor.getColumnIndex("telefono"))) "No disponible" else cursor.getString(cursor.getColumnIndex("telefono")),
//                    fecRegistro = if (cursor.isNull(cursor.getColumnIndex("fec_registro"))) "Fecha no disponible" else cursor.getString(cursor.getColumnIndex("fec_registro"))
//                )
//            }
//            cursor.close()
//        } catch (e: Exception) {
//            android.util.Log.e(Tools.LOGTAG, "Error al obtener datos del usuario: ${e.message}")
//        } finally {
//            db.close()
//        }
//
//        return usuario
//    }
//}
package com.example.betondecken.DAO

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.betondecken.Model.Usuario
import com.example.betondecken.Util.Tools

class DBHelper(myContext: Context) : SQLiteOpenHelper(myContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "concreta_db" // Nombre de la base de datos
        private const val DATABASE_VERSION = 2         // Versión de la base de datos
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear la tabla de usuarios
        val sql_usuario = """
            CREATE TABLE ${Tools.TABLA_USUARIOS} (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                id_usuario TEXT UNIQUE, -- id_usuario debe ser único para relacionarlo
                password TEXT,
                correo TEXT,
                telefono TEXT,
                fec_registro DATE
            )
        """
        db.execSQL(sql_usuario)

        // Crear la tabla de pedidos.
        val sql_pedido = """
            CREATE TABLE ${Tools.TABLA_PEDIDOS} (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_usuario TEXT NOT NULL,  -- Relación con TABLA_USUARIOS
                id_tracking TEXT,
                fecha DATE,
                descripcion TEXT NOT NULL,
                id_producto INTEGER,
                peso NUMERIC,
                estado INTEGER,
                FOREIGN KEY (id_usuario) REFERENCES ${Tools.TABLA_USUARIOS}(id_usuario),
                FOREIGN KEY (id_producto) REFERENCES ${Tools.TABLA_PRODUCTOS}(id_producto))
         """
        db.execSQL(sql_pedido)

        // Crear la tabla de productos
        val sql_producto = """
            CREATE TABLE ${Tools.TABLA_PRODUCTOS}(
                id_producto INTEGER PRIMARY KEY AUTOINCREMENT,
                des_producto TEXT,
                peso_kg INTEGER)
        """
        db.execSQL(sql_producto)

        // Crear la tabla de estados de pedido
        val sql_estado_pedido = """
            CREATE TABLE ${Tools.TABLA_ESTADOS_PEDIDO} (
                cod_estado TEXT PRIMARY KEY,
                des_estado TEXT)
        """
        db.execSQL(sql_estado_pedido)

        // Crear la tabla de trazabilidad de pedidos
        val sql_pedido_trazabilidad = """
            CREATE TABLE ${Tools.TABLA_TRAZABILIDAD_PEDIDOS} (
                n_pedido INTEGER NOT NULL,
                fec_trazabilidad DATE,
                cod_estado TEXT NOT NULL,
                PRIMARY KEY (n_pedido, cod_estado),
                FOREIGN KEY (n_pedido) REFERENCES ${Tools.TABLA_PEDIDOS} (id),
                FOREIGN KEY (cod_estado) REFERENCES ${Tools.TABLA_ESTADOS_PEDIDO} (cod_estado))
        """
        db.execSQL(sql_pedido_trazabilidad)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Borrar tablas existentes al actualizar la base de datos
        db.execSQL("DROP TABLE IF EXISTS ${Tools.TABLA_USUARIOS}")
        db.execSQL("DROP TABLE IF EXISTS ${Tools.TABLA_PEDIDOS}")
        db.execSQL("DROP TABLE IF EXISTS ${Tools.TABLA_PRODUCTOS}")
        db.execSQL("DROP TABLE IF EXISTS ${Tools.TABLA_ESTADOS_PEDIDO}")
        db.execSQL("DROP TABLE IF EXISTS ${Tools.TABLA_TRAZABILIDAD_PEDIDOS}")
        onCreate(db)
    }

    // Método para insertar un nuevo usuario
    fun insertUsuario(nombre: String, idUsuario: String, password: String, correo: String, telefono: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("id_usuario", idUsuario)
            put("password", password)
            put("correo", correo)
            put("telefono", telefono)
            put("fec_registro", System.currentTimeMillis()) // Fecha actual
        }
        return try {
            db.insertOrThrow(Tools.TABLA_USUARIOS, null, values)
        } catch (e: Exception) {
            android.util.Log.e(Tools.LOGTAG, "Error al insertar usuario: ${e.message}")
            -1L // Retorna -1 si ocurre un error
        }
    }

    // Método para obtener pedidos de un usuario específico
    fun getPedidos(userId: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            """
        SELECT p.id, p.fecha, p.descripcion, p.peso, e.des_estado
        FROM ${Tools.TABLA_PEDIDOS} p
        LEFT JOIN ${Tools.TABLA_ESTADOS_PEDIDO} e ON p.estado = e.cod_estado
        WHERE p.id_usuario = ?
        """.trimIndent(),
            arrayOf(userId)
        )
    }

    // Método para actualizar los datos de un usuario
    fun updateUsuario(idUsuario: Int, nombre: String, correo: String, telefono: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("telefono", telefono)
        }
        return db.update(Tools.TABLA_USUARIOS, values, "id = ?", arrayOf(idUsuario.toString()))
    }

    // Método para calcular el peso total de pedidos realizados por un usuario
    fun getTotalPedidoPeso(userId: Int): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT SUM(peso) 
            FROM ${Tools.TABLA_PEDIDOS} 
            WHERE id_usuario = ?
            """.trimIndent(),
            arrayOf(userId.toString())
        )
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        return total
    }

    // Método para calcular el peso de pedidos pendientes de entrega
    fun getPendienteEntregaPeso(userId: Int): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT SUM(peso) 
            FROM ${Tools.TABLA_PEDIDOS} 
            WHERE id_usuario = ? AND estado != 4 -- Estado 4 = Entregado
            """.trimIndent(),
            arrayOf(userId.toString())
        )
        var pendiente = 0.0
        if (cursor.moveToFirst()) {
            pendiente = cursor.getDouble(0)
        }
        cursor.close()
        return pendiente
    }

    // Método para obtener la información de un usuario específico
    @SuppressLint("Range")
    fun getUsuarioInfo(id: Int): Usuario? {
        val db = this.readableDatabase
        var usuario: Usuario? = null

        try {
            val cursor = db.rawQuery(
                """
            SELECT nombre, correo, telefono, fec_registro 
            FROM ${Tools.TABLA_USUARIOS} 
            WHERE id = ?
            """.trimIndent(),
                arrayOf(id.toString())
            )

            if (cursor.moveToFirst()) {
                usuario = Usuario(
                    id = id.toString(),
                    nombre = cursor.getString(cursor.getColumnIndex("nombre")) ?: "No disponible",
                    usuario = "N/A",
                    correo = if (cursor.isNull(cursor.getColumnIndex("correo"))) "No disponible" else cursor.getString(cursor.getColumnIndex("correo")),
                    telefono = if (cursor.isNull(cursor.getColumnIndex("telefono"))) "No disponible" else cursor.getString(cursor.getColumnIndex("telefono")),
                    fecRegistro = if (cursor.isNull(cursor.getColumnIndex("fec_registro"))) "Fecha no disponible" else cursor.getString(cursor.getColumnIndex("fec_registro"))
                )
            }
            cursor.close()
        } catch (e: Exception) {
            android.util.Log.e(Tools.LOGTAG, "Error al obtener datos del usuario: ${e.message}")
        } finally {
            db.close()
        }

        return usuario
    }
}
