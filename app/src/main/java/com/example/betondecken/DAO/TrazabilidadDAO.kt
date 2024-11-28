package com.example.betondecken.DAO

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.betondecken.Model.Pedido
import com.example.betondecken.Model.Producto
import com.example.betondecken.Model.Trazabilidad
import com.example.betondecken.Util.Tools

class TrazabilidadDAO(myContext: Context) {

    private var dbHelper: DBHelper = DBHelper(myContext)

    @SuppressLint("Range")
    fun fnObtenerTrazabilidadByPedido(idPedido: Int) : ArrayList<Trazabilidad> {
        Log.i(Tools.LOGTAG, "Ingresando a fnObtenerTrazabilidadByPedido")

        val lista = ArrayList<Trazabilidad>()

        val db = dbHelper.readableDatabase

        try {
            val query = "SELECT n_pedido, fec_trazabilidad, cod_estado  FROM " + Tools.TABLA_TRAZABILIDAD_PEDIDOS + " WHERE n_pedido=$idPedido"

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

                    val trazabilidad = Trazabilidad()

                    trazabilidad.id_pedido  = c.getInt(c.getColumnIndex("n_pedido"))
                    trazabilidad.fec_trazabilidad   = c.getString(c.getColumnIndex("fec_trazabilidad"))
                    trazabilidad.id_estado          = c.getInt(c.getColumnIndex("cod_estado"))

                    Log.i(Tools.LOGTAG, "estado trazabilidad: " + trazabilidad.fec_trazabilidad)

                    lista.add(trazabilidad)

                } while (c.moveToNext())
            }
        } catch (e: Exception) {
            throw DAOException("TrazabilidadDAO: Error al obtener la trazabilidad del pedido: ${e.message}")
        } finally {
            db.close()
        }
        return lista
    }
}