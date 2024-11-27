package com.example.betondecken.ui.tracking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.betondecken.DAO.PedidoDAO
import com.example.betondecken.Model.Pedido
import com.example.betondecken.R
import com.example.betondecken.TrackingActivity
import com.example.betondecken.Util.Tools
import com.example.betondecken.databinding.FragmentTrackingBinding
import java.io.Serializable

class TrackingFragment : Fragment() {

    private var _binding: FragmentTrackingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflar el diseño para este fragmento
        val root1 = inflater.inflate(R.layout.fragment_tracking, container, false)

        val homeViewModel =
            ViewModelProvider(this).get(TrackingViewModel::class.java)

        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        val bindingRoot: View = binding.root

        val textViewTracking: TextView = binding.textTracking



        homeViewModel.text.observe(viewLifecycleOwner) {
            textViewTracking.text = it
        }
        // Referencias a los elementos del diseño

        val btnTracking = root1.findViewById<Button>(R.id.btnTracking)
        val editTextIdTracking = root1.findViewById<EditText>(R.id.editTextIdTracking)

        //editTextIdTracking.setText("00000004")

        btnTracking.setOnClickListener {

            Log.i(Tools.LOGTAG,"inicia setOnClickListener")

            val idTracking  = editTextIdTracking.text.toString()

            Log.i(Tools.LOGTAG,"idTracking: " + idTracking)

            if (idTracking.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Ingrese el número de tracking", Toast.LENGTH_SHORT).show()
                textViewTracking.requestFocus()
            }else{

                setupTracking(idTracking)
            }


        }





        val btn: Button = binding.btnTracking

//        btnTracking.setOnClickListener {
//            setup()
//        }
        //setup()
        setupButtonListeners(root1)
        return root1

    }

    private fun setupTracking(idTracking: String){


        val dao = PedidoDAO(requireContext())

        lateinit var pedido:Pedido

        Log.i(Tools.LOGTAG,"enviar al DAO: " + idTracking)


        pedido  = dao.fnObtenerIdPedidoByIdTracking(idTracking)

        if (pedido.id == 0){
            Toast.makeText(requireContext(), "No existe pedidos con el número de tracking $idTracking", Toast.LENGTH_SHORT).show()
            return
        }
        Log.i(Tools.LOGTAG,"id pedido BD: " + pedido.id)

        Log.i(Tools.LOGTAG,"inicia el pedido")

        var intent = Intent(activity,TrackingActivity::class.java)
        intent.putExtra("idPedido",pedido.id.toString())
        intent.putExtra("idTracking",pedido.id_tracking)
        intent.putExtra("extra_object",pedido as Serializable)

        startActivity(intent)


    }
    private fun setupButtonListeners(view: View) {
        // Botón Pedidos
        view.findViewById<ImageButton>(R.id.btn_pedidos).setOnClickListener {
            // Navegación hacia el fragmento de Pedidos
            findNavController().navigate(R.id.action_nav_tracking_to_nav_pedidos)
        }
        // Botón Contacta a Soporte
        view.findViewById<ImageButton>(R.id.btn_contacto).setOnClickListener {
            // Navegación hacia el fragmento de Soporte
            findNavController().navigate(R.id.action_nav_tracking_to_nav_support)
        }

        // Botón Servicio al Cliente
        view.findViewById<ImageButton>(R.id.btn_servicio).setOnClickListener {
            // Navegación hacia el fragmento de Servicio al Cliente
            findNavController().navigate(R.id.action_nav_tracking_to_nav_servicio)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}