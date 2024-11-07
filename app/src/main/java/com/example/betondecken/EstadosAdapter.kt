package com.example.betondecken

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EstadosAdapter(private val estadosList: List<Estados>):
    RecyclerView.Adapter<EstadosAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = MyViewHolder(layoutInflater.inflate(R.layout.activity_traking_item, parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val estado = estadosList[position]

        holder.estadoPedido.text = estado.estado
        holder.estadoDescripcion.text = estado.descripcion
        holder.fechaHora.text = estado.hora

    }

    override fun getItemCount(): Int {
        return estadosList.size
    }

    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val estadoPedido = view.findViewById<TextView>(R.id.txtEstadoPedido)
        val estadoDescripcion = view.findViewById<TextView>(R.id.txtEstadoDescripcion)
        val fechaHora = view.findViewById<TextView>(R.id.txtEstadoFechaHora)

    }

}
