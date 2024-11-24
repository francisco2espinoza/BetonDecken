package com.example.betondecken.DAO

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.betondecken.Util.Tools

class DBHelper(myContext: Context): SQLiteOpenHelper(myContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "concreta_db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {


        val sql_usuario = "CREATE TABLE T_USUARIOS (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre text, id_usuario text, password text)"

        db.execSQL(sql_usuario)

        val sql_pedido = "CREATE TABLE IF NOT EXISTS T_PEDIDOS (id INTEGER PRIMARY KEY AUTOINCREMENT, id_tracking text, fecha DATE, descripcion TEXT NOT NULL, peso numeric, estado int)"

        db.execSQL(sql_pedido)

    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS " + Tools.TABLA_USUARIOS)

        onCreate(db)
    }


}