package mx.ipn.catalogo.views.ui.secciones

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import mx.ipn.catalogo.views.R
import mx.ipn.catalogo.views.Seccion
import mx.ipn.catalogo.views.databinding.FragmentSeleccionBinding
import mx.ipn.catalogo.views.ui.SeccionFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/** Sección 3: Elementos de selección. */
class SeleccionFragment : SeccionFragment(R.layout.fragment_seleccion, Seccion.SELECCION) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentSeleccionBinding.bind(view)

        // 1. Casillas: la principal pasa a indeterminada si solo algunas están marcadas.
        val hijos = listOf(b.cbQueso, b.cbJamon, b.cbPina)
        var actualizando = false
        fun resumen() {
            val marcados = hijos.filter { it.isChecked }.joinToString(", ") { it.text }
            b.tvCasillas.text = getString(R.string.s3_check_resp, marcados.ifEmpty { getString(R.string.ninguno) })
        }
        fun actualizarPadre() {
            val n = hijos.count { it.isChecked }
            actualizando = true
            b.cbTodos.checkedState = when (n) {
                0 -> MaterialCheckBox.STATE_UNCHECKED
                hijos.size -> MaterialCheckBox.STATE_CHECKED
                else -> MaterialCheckBox.STATE_INDETERMINATE
            }
            actualizando = false
            resumen()
        }
        hijos.forEach { hijo -> hijo.setOnCheckedChangeListener { _, _ -> if (!actualizando) actualizarPadre() } }
        b.cbTodos.addOnCheckedStateChangedListener { _, estado ->
            if (actualizando || estado == MaterialCheckBox.STATE_INDETERMINATE) return@addOnCheckedStateChangedListener
            actualizando = true
            hijos.forEach { it.isChecked = estado == MaterialCheckBox.STATE_CHECKED }
            actualizando = false
            resumen()
        }
        actualizarPadre()

        // 2. Botones de opción.
        fun mostrarEnvio(id: Int) {
            val texto = view.findViewById<RadioButton>(id)?.text ?: ""
            b.tvEnvio.text = getString(R.string.s3_radio_resp, texto)
        }
        mostrarEnvio(b.rgEnvio.checkedRadioButtonId)
        b.rgEnvio.setOnCheckedChangeListener { _, id -> mostrarEnvio(id) }

        // 3. Interruptor.
        b.swNotificaciones.setOnCheckedChangeListener { _, activo ->
            b.tvSwitch.setText(if (activo) R.string.s3_switch_on else R.string.s3_switch_off)
        }

        // 4. Deslizador conectado con la Sección 5 y deslizador de rango.
        val tamano = vm.tamanoTexto.value ?: 18f
        b.sliderTexto.value = tamano
        b.tvSlider.text = getString(R.string.s3_slider_resp, tamano.toInt())
        b.sliderTexto.addOnChangeListener { _, valor, _ ->
            vm.cambiarTamanoTexto(valor)
            b.tvSlider.text = getString(R.string.s3_slider_resp, valor.toInt())
        }
        b.rangoPrecio.values = listOf(200f, 800f)
        fun mostrarRango() {
            val v = b.rangoPrecio.values
            b.tvRango.text = getString(R.string.s3_rango_resp, v[0].toInt(), v[1].toInt())
        }
        mostrarRango()
        b.rangoPrecio.addOnChangeListener { _, _, _ -> mostrarRango() }

        // 5. Lista desplegable.
        b.tvPais.text = getString(R.string.s3_pais_resp, "—")
        b.actvPais.setOnItemClickListener { parent, _, posicion, _ ->
            b.tvPais.text = getString(R.string.s3_pais_resp, parent.getItemAtPosition(posicion).toString())
        }

        // 6. Selectores de fecha y hora.
        b.btnFecha.setOnClickListener {
            val selector = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.s3_fecha_titulo)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            selector.addOnPositiveButtonClickListener { millis ->
                val formato = SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", Locale("es", "MX"))
                formato.timeZone = TimeZone.getTimeZone("UTC")
                b.tvFecha.text = getString(R.string.s3_fecha_resp, formato.format(Date(millis)))
            }
            selector.show(childFragmentManager, "fecha")
        }
        b.btnHora.setOnClickListener {
            val selector = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText(R.string.s3_hora_titulo)
                .build()
            selector.addOnPositiveButtonClickListener {
                val hora = String.format(Locale.getDefault(), "%02d:%02d", selector.hour, selector.minute)
                b.tvHora.text = getString(R.string.s3_hora_resp, hora)
            }
            selector.show(childFragmentManager, "hora")
        }

        // 7. Chips de filtro.
        fun mostrarChips() {
            val activos = b.cgFiltros.checkedChipIds.mapNotNull { id -> view.findViewById<Chip>(id)?.text }
            b.tvChips.text = getString(
                R.string.s3_chips_resp,
                if (activos.isEmpty()) getString(R.string.ninguno) else activos.joinToString(", "),
            )
        }
        mostrarChips()
        b.cgFiltros.setOnCheckedStateChangeListener { _, _ -> mostrarChips() }
    }
}
