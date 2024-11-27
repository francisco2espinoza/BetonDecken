package com.example.betondecken
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betondecken.Model.Pedido
import com.example.betondecken.Util.Tools
import com.example.betondecken.ui.pedidos.PedidosFragment

class TrackingActivity : AppCompatActivity() {

    private val estadosList: ArrayList<Estados> = ArrayList()
    private  lateinit var recicleView: RecyclerView
    private lateinit var mAdapter: EstadosAdapter
    lateinit var idPedido: String
    lateinit var idTracking: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContentView(R.layout.activity_tracking)

        // recibir los parametros
        val bundle = intent.extras

        idPedido    = bundle?.getString("idPedido")!!
        idTracking  = bundle?.getString("idTracking")!!
        val pedido = intent.getSerializableExtra("extra_object")  as Pedido

        val textTracking = findViewById<TextView>(R.id.textViewIdTracking)
        val textViewEstadoPedido = findViewById<TextView>(R.id.textViewEstadoPedido)
        val textViewPeso        = findViewById<TextView>(R.id.textViewPeso)
        val textViewDescripcion = findViewById<TextView>(R.id.textViewDescripcion)
        val textViewProducto    = findViewById<TextView>(R.id.textViewProducto)

        Log.i(Tools.LOGTAG,"id_traking_recibe desde extra: " + pedido.id_tracking)

        textTracking.setText(pedido.id_tracking)
        textViewEstadoPedido.setText(pedido.desEstado)
        textViewPeso.setText(pedido.peso.toString() + " KG")
        textViewDescripcion.setText(pedido.descripcion)
        textViewProducto.setText(pedido.id_producto.toString())

        recicleView = findViewById(R.id.recicleView_estado)
        mAdapter    = EstadosAdapter(estadosList)

        recicleView.layoutManager = LinearLayoutManager(this)
        recicleView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        recicleView.adapter = mAdapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        preparePeliculaData()
    }

    private fun preparePeliculaData() {
        var estado = Estados("Preparando despacho", "Jicamarca, Perú", "11:45 A.M")
        estadosList.add(estado)

        estado = Estados("Despacho en tránsito", "", "11:55 AM")
        estadosList.add(estado)

        estado = Estados("Pedido entregado", "", "13:15 AM")
        estadosList.add(estado)

        mAdapter.notifyDataSetChanged()
    }
}