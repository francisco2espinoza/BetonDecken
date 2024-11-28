package com.example.betondecken
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betondecken.DAO.ProductoDAO
import com.example.betondecken.DAO.TrazabilidadDAO
import com.example.betondecken.Model.Pedido
import com.example.betondecken.Model.Producto
import com.example.betondecken.Model.Trazabilidad
import com.example.betondecken.Util.Tools

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
        val pedidoRequest = intent.getSerializableExtra("extra_object")  as Pedido

        val textTracking = findViewById<TextView>(R.id.textViewIdTracking)
        val textViewEstadoPedido = findViewById<TextView>(R.id.textViewEstadoPedido)
        val textViewPeso        = findViewById<TextView>(R.id.textViewPeso)
        val textViewDescripcion = findViewById<TextView>(R.id.textViewDescripcion)
        val textViewProducto    = findViewById<TextView>(R.id.textViewProducto)

        Log.i(Tools.LOGTAG,"id_traking_recibe desde extra: " + pedidoRequest.id_tracking)

        textTracking.setText(pedidoRequest.id_tracking)
        textViewEstadoPedido.setText(pedidoRequest.desEstado)
        textViewPeso.setText(pedidoRequest.peso.toString() + " KG")
        textViewDescripcion.setText(pedidoRequest.descripcion)
        textViewProducto.setText(pedidoRequest.id_producto.toString())

        // obtener la descripcion del producto a partir del id
        var daoProducto = ProductoDAO(baseContext)

        lateinit var producto: Producto

        producto = daoProducto.fnObtenerProductoById(pedidoRequest.id_producto)

        textViewProducto.setText(producto.des_producto)

        // fin obtener producto


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

        preparePeliculaData(pedidoRequest.id)
    }

    private fun preparePeliculaData(idPedido: Int) {

        var daoTrazabilidad = TrazabilidadDAO(baseContext)

        lateinit var trazabilidad: ArrayList<Trazabilidad>

        trazabilidad = daoTrazabilidad.fnObtenerTrazabilidadByPedido(idPedido)

        var index:Int = 0
        var desEStado: String

        for (num in trazabilidad){
            Log.i(Tools.LOGTAG,"leyendod: " + trazabilidad[index].fec_trazabilidad)

            when(trazabilidad[index].id_estado){
                1-> desEStado = "Pedido Generado"
                2-> desEStado = "Pedido Aprobado"
                3-> desEStado = "Preparando despacho"
                4-> desEStado = "Despacho en tránsito"
                5-> desEStado = "Despacho entregado"
                else-> desEStado = "Pedido Generado"
            }
            var estado = Estados(desEStado, "", trazabilidad[index].fec_trazabilidad)
            estadosList.add(estado)
            index++
        }

        mAdapter.notifyDataSetChanged()
    }
}