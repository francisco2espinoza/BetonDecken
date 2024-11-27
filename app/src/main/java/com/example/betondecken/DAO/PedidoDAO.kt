package com.example.betondecken.DAO

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.betondecken.Model.Pedido
import com.example.betondecken.Util.Tools

class PedidoDAO(myContext: Context) {

    private var dbHelper: DBHelper = DBHelper(myContext)

    @SuppressLint("Range")
    fun fnObtenerIdPedidoByIdTracking(idTracking: String) :Pedido {
        Log.i(Tools.LOGTAG, "Ingresando a fnObtenerIdPedidoByIdTracking")

        val result = Pedido()

        val db = dbHelper.readableDatabase

        try {
            val query = "SELECT id, id_usuario, id_tracking, fecha, descripcion, id_producto, peso, estado  FROM " + Tools.TABLA_PEDIDOS + " WHERE id_tracking='$idTracking'"

            Log.i(Tools.LOGTAG, query)

            val c: Cursor = db.rawQuery(
                query,
                null
            )

            var contador: Int = 0

            Log.i(Tools.LOGTAG, "cantidad pedidos: " + c.count)

            if (c.count > 0) {
                c.moveToFirst()
                do {
                    contador++
                    Log.i(Tools.LOGTAG, "contador: " + contador)

                    result.id               = c.getInt(c.getColumnIndex("id"))
                    result.id_usuario       = c.getString(c.getColumnIndex("id_usuario"))
                    result.id_tracking      = c.getString(c.getColumnIndex("id_tracking"))
                   // result.fecha            = c.getString(c.getColumnIndex("fecha"))
                    result.descripcion      = c.getString(c.getColumnIndex("descripcion"))
                    result.id_producto      = c.getLong(c.getColumnIndex("id_producto"))
                    result.peso             = c.getLong(c.getColumnIndex("peso"))
                    result.desEstado           = c.getString(c.getColumnIndex("estado"))

                } while (c.moveToNext())
            }
        } catch (e: Exception) {
            throw DAOException("PedidoDAO: Error al pedido mediante el tracking: ${e.message}")
        } finally {
            db.close()
        }
        return result
    }
}