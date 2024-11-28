package com.example.betondecken.Model

import java.io.Serializable
import java.util.Date

 class Producto: Serializable {
     var id_producto: Int = 0
     lateinit var  des_producto: String
      var  peso: Long = 0
}