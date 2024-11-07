package com.example.betondecken
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TrackingActivity : AppCompatActivity() {

    private val estadosList: ArrayList<Estados> = ArrayList()
    private  lateinit var recicleView: RecyclerView
    private lateinit var mAdapter: EstadosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContentView(R.layout.activity_tracking)

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