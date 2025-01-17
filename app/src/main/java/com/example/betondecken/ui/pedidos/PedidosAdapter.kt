//package com.example.betondecken.adapters
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.betondecken.R
////
//data class Pedido(
//    val nPedido: Int,
//    val fecha: String,
//    val producto: String,
//    val peso: Double,
//    val estado: String
//)
//
//class PedidosAdapter(private val pedidosList: List<Pedido>) :
//    RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {
//
//    class PedidoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val tvPedido: TextView = view.findViewById(R.id.tvPedido)
//        val tvProducto: TextView = view.findViewById(R.id.tvProducto)
//        val tvPeso: TextView = view.findViewById(R.id.tvPeso)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_pedido, parent, false)
//        return PedidoViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
//        val pedido = pedidosList[position]
//        holder.tvPedido.text = "Pedido #${pedido.nPedido}"
//        holder.tvProducto.text = "Producto: ${pedido.producto}"
//        holder.tvPeso.text = "Peso: ${pedido.peso} kg"
//    }
//
//    override fun getItemCount() = pedidosList.size
//}
package com.example.betondecken.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.betondecken.R

data class Pedido(
    val id: Int,
    val fecha: String,
    val descripcion: String,
    val peso: Double,
    val estado: String
)

class PedidosAdapter(private val pedidosList: List<Pedido>) :
    RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val tvPeso: TextView = view.findViewById(R.id.tvPeso)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidosList[position]
        holder.tvFecha.text = pedido.fecha
        holder.tvDescripcion.text = pedido.descripcion
        holder.tvPeso.text = "${pedido.peso} Kg"
        holder.tvEstado.text = pedido.estado
    }

    override fun getItemCount(): Int = pedidosList.size
}
