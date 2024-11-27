package com.example.betondecken.Model

import java.io.Serializable
import java.util.Date

 class Pedido: Serializable {
     var id: Int = 0
     lateinit var  id_usuario: String
     lateinit var id_tracking: String
     lateinit var  fecha: String
     lateinit var  descripcion : String
      var  id_producto: Long = 0
      var  peso: Long = 0
      var  estado: Long = 0
     lateinit var   desEstado: String
}