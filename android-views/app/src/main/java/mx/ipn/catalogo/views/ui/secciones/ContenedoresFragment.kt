package mx.ipn.catalogo.views.ui.secciones

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import mx.ipn.catalogo.views.R
import mx.ipn.catalogo.views.Seccion
import mx.ipn.catalogo.views.databinding.FragmentContenedoresBinding
import mx.ipn.catalogo.views.ui.SeccionFragment

/** Sección 6: Contenedores y estructura. */
class ContenedoresFragment : SeccionFragment(R.layout.fragment_contenedores, Seccion.CONTENEDORES) {

    private var posicionCapa = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentContenedoresBinding.bind(view)

        // 1. Fila / columna: se cambia la orientación del LinearLayout.
        b.tgOrientacion.addOnButtonCheckedListener { _, id, marcado ->
            if (marcado) {
                b.llDistribucion.orientation =
                    if (id == R.id.btnColumna) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL
            }
        }

        // 2. Capas: la etiqueta superior se mueve entre tres posiciones del FrameLayout.
        val posiciones = listOf(
            Gravity.TOP or Gravity.START to R.string.arriba_izq,
            Gravity.CENTER to R.string.centro,
            Gravity.BOTTOM or Gravity.END to R.string.abajo_der,
        )
        fun moverCapa() {
            val (gravedad, nombre) = posiciones[posicionCapa]
            (b.tvCapa.layoutParams as FrameLayout.LayoutParams).gravity = gravedad
            b.tvCapa.requestLayout()
            b.tvCapaInfo.text = getString(R.string.s6_capa_resp, getString(nombre))
        }
        moverCapa()
        b.flCapas.setOnClickListener {
            posicionCapa = (posicionCapa + 1) % posiciones.size
            moverCapa()
        }

        // 3. Contenedor desplazable con 30 renglones.
        for (i in 1..30) {
            b.llRenglones.addView(TextView(requireContext()).apply {
                text = getString(R.string.s6_linea, i)
                setPadding(0, 8, 0, 8)
            })
        }
        b.btnIrFinal.setOnClickListener { b.svInterno.smoothScrollTo(0, b.llRenglones.height) }
        b.btnIrInicio.setOnClickListener { b.svInterno.smoothScrollTo(0, 0) }

        // 4. Barra superior con acciones.
        b.tvToolbar.text = getString(R.string.s6_toolbar_resp, "—")
        b.tbDemo.setNavigationOnClickListener {
            b.tvToolbar.text = getString(R.string.s6_toolbar_resp, getString(R.string.menu))
        }
        b.tbDemo.setOnMenuItemClickListener { item ->
            b.tvToolbar.text = getString(R.string.s6_toolbar_resp, item.title)
            true
        }

        // 5. Navegación inferior con un badge en «Favoritos».
        b.bnDemo.getOrCreateBadge(R.id.demo_favoritos).number = 3
        fun mostrarDestino(titulo: CharSequence) {
            b.tvNavContenido.text = getString(R.string.s6_nav_resp, titulo)
        }
        mostrarDestino(getString(R.string.inicio))
        b.bnDemo.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.demo_favoritos) b.bnDemo.removeBadge(R.id.demo_favoritos)
            mostrarDestino(item.title ?: "")
            true
        }

        // 6. Pesos proporcionales y sesgo de restricciones.
        fun aplicarPeso(peso: Float) {
            (b.vPeso1.layoutParams as LinearLayout.LayoutParams).weight = peso
            b.vPeso1.requestLayout()
            b.tvPesos.text = getString(R.string.s6_pesos_resp, peso.toInt())
        }
        aplicarPeso(b.sliderPeso.value)
        b.sliderPeso.addOnChangeListener { _, valor, _ -> aplicarPeso(valor) }

        fun aplicarSesgo(porcentaje: Float) {
            (b.cBias.layoutParams as ConstraintLayout.LayoutParams).horizontalBias = porcentaje / 100f
            b.cBias.requestLayout()
            b.tvBias.text = getString(R.string.s6_bias_resp, porcentaje.toInt())
        }
        aplicarSesgo(b.sliderBias.value)
        b.sliderBias.addOnChangeListener { _, valor, _ -> aplicarSesgo(valor) }
    }
}
