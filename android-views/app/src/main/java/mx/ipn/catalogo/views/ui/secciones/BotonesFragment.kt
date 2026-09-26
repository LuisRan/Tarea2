package mx.ipn.catalogo.views.ui.secciones

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mx.ipn.catalogo.views.R
import mx.ipn.catalogo.views.Seccion
import mx.ipn.catalogo.views.databinding.FragmentBotonesBinding
import mx.ipn.catalogo.views.ui.SeccionFragment

/** Sección 2: Botones y acciones. */
class BotonesFragment : SeccionFragment(R.layout.fragment_botones, Seccion.BOTONES) {

    private var pulsaciones = 0
    private var favorito = false
    private var fabContador = 0
    private var extendido = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentBotonesBinding.bind(view)
        extendido = true

        // 1. Botones básicos: cada uno reporta su nombre y el total de pulsaciones.
        listOf(b.btnRelleno, b.btnContorno, b.btnTexto).forEach { boton ->
            boton.setOnClickListener {
                pulsaciones++
                b.tvBasicos.text = getString(R.string.s2_pulsaste, boton.text, pulsaciones)
            }
        }

        // 2. Botones con ícono.
        b.btnFavorito.setOnClickListener {
            favorito = !favorito
            b.btnFavorito.setIconResource(if (favorito) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
            b.tvIconos.setText(if (favorito) R.string.s2_fav_si else R.string.s2_fav_no)
        }
        b.btnCompartir.setOnClickListener { b.tvIconos.setText(R.string.s2_compartido) }
        b.btnEnviar.setOnClickListener { b.tvIconos.setText(R.string.s2_enviado) }

        // 3. FAB normal y extendido (el extendido se contrae y se expande).
        fun actualizarFab() {
            val estado = getString(if (extendido) R.string.extendido else R.string.contraido)
            b.tvFab.text = getString(R.string.s2_fab_resp, fabContador, estado)
        }
        actualizarFab()
        b.fab.setOnClickListener { fabContador++; actualizarFab() }
        b.efab.setOnClickListener {
            extendido = !extendido
            if (extendido) b.efab.extend() else b.efab.shrink()
            actualizarFab()
        }

        // 4. Selector segmentado.
        fun mostrarVista(id: Int) {
            val boton = view.findViewById<MaterialButton>(id)
            b.tvVista.text = getString(R.string.s2_vista, boton.text)
        }
        mostrarVista(b.grupoVista.checkedButtonId)
        b.grupoVista.addOnButtonCheckedListener { _, id, marcado -> if (marcado) mostrarVista(id) }

        // 5. Deshabilitado (se habilita con el interruptor) y estado de carga.
        b.swHabilitar.setOnCheckedChangeListener { _, activo -> b.btnDeshabilitado.isEnabled = activo }
        b.btnDeshabilitado.setOnClickListener { b.tvEstados.setText(R.string.s2_confirmado) }
        b.btnCargar.setOnClickListener {
            b.btnCargar.isEnabled = false
            b.btnCargar.text = ""
            b.pbCargar.isVisible = true
            b.tvEstados.setText(R.string.s2_cargando)
            viewLifecycleOwner.lifecycleScope.launch {
                delay(2000)
                b.pbCargar.isVisible = false
                b.btnCargar.isEnabled = true
                b.btnCargar.setText(R.string.s2_cargar)
                b.tvEstados.setText(R.string.s2_cargado)
            }
        }
    }
}
