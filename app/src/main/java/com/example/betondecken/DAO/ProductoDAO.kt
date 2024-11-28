package com.example.betondecken.DAO

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.betondecken.Model.Pedido
import com.example.betondecken.Model.Producto
import com.example.betondecken.Util.Tools

class ProductoDAO(myContext: Context) {

    private var dbHelper: DBHelper = DBHelper(myContext)

    @SuppressLint("Range")
    fun fnObtenerProductoById(idProducto: Long) :Producto {
        Log.i(Tools.LOGTAG, "Ingresando a fnObtenerProductoById")

        val result = Producto()

        val db = dbHelper.readableDatabase

        try {
            val query = "SELECT id_producto, des_producto, peso_kg  FROM " + Tools.TABLA_PRODUCTOS + " WHERE id_producto=$idProducto"

            Log.i(Tools.LOGTAG, query)

            val c: Cursor = db.rawQuery(
                query,
                null
            )

            var contador: Int = 0

            Log.i(Tools.LOGTAG, "cantidad productos: " + c.count)

            if (c.count > 0) {
                c.moveToFirst()
                do {
                    contador++
                    Log.i(Tools.LOGTAG, "contador: " + contador)

                    result.id_producto      = c.getInt(c.getColumnIndex("id_producto"))
                    result.des_producto     = c.getString(c.getColumnIndex("des_producto"))
                    result.peso             = c.getLong(c.getColumnIndex("peso_kg"))


                } while (c.moveToNext())
            }
        } catch (e: Exception) {
            throw DAOException("ProductoDAO: Error al obtener el producto mediante el id: ${e.message}")
        } finally {
            db.close()
        }
        return result
    }
}