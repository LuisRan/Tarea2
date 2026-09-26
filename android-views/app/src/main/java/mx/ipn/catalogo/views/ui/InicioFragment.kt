package mx.ipn.catalogo.views.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import mx.ipn.catalogo.views.MainActivity
import mx.ipn.catalogo.views.R
import mx.ipn.catalogo.views.Seccion
import mx.ipn.catalogo.views.databinding.FragmentInicioBinding
import mx.ipn.catalogo.views.databinding.ItemSeccionBinding

/** Pantalla principal: presenta el catálogo y una tarjeta por cada sección. */
class InicioFragment : SeccionFragment(R.layout.fragment_inicio, Seccion.INICIO) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentInicioBinding.bind(view)
        val inflater = LayoutInflater.from(requireContext())
        Seccion.entries.filter { it != Seccion.INICIO }.forEach { seccion ->
            val item = ItemSeccionBinding.inflate(inflater, b.listaSecciones, false)
            item.icono.setImageResource(seccion.icono)
            item.titulo.setText(seccion.titulo)
            item.descripcion.setText(seccion.descripcion)
            item.root.setOnClickListener { (activity as? MainActivity)?.abrir(seccion) }
            b.listaSecciones.addView(item.root)
        }
        vm.elementos.observe(viewLifecycleOwner) { lista ->
            b.resumen.text = getString(R.string.s1_total, lista.size)
        }
    }
}
