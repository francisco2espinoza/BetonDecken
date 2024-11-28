package com.example.betondecken.Model

import java.io.Serializable
import java.util.Date

 class Trazabilidad: Serializable {
     var id_pedido: Int = 0
     lateinit var  fec_trazabilidad: String
      var  id_estado: Int = 0
}