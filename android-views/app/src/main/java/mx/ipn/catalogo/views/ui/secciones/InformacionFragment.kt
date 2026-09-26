package mx.ipn.catalogo.views.ui.secciones

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mx.ipn.catalogo.views.R
import mx.ipn.catalogo.views.Seccion
import mx.ipn.catalogo.views.databinding.FragmentInformacionBinding
import mx.ipn.catalogo.views.ui.SeccionFragment

/** Sección 5: Información y retroalimentación. */
class InformacionFragment : SeccionFragment(R.layout.fragment_informacion, Seccion.INFORMACION) {

    private var progreso = 40
    private var notificaciones = 0
    private var semillaImagen = 1015
    private var simulacion: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentInformacionBinding.bind(view)

        // 1. Textos: un texto con varios estilos (Spannable) y otro que depende de la Sección 3.
        val primario = MaterialColors.getColor(view, com.google.android.material.R.attr.colorPrimary)
        b.tvSpan.text = SpannableStringBuilder()
            .append("Un mismo texto puede combinar ")
            .inSpans(StyleSpan(Typeface.BOLD)) { append("negritas") }
            .append(", ")
            .inSpans(StyleSpan(Typeface.ITALIC)) { append("cursivas") }
            .append(", ")
            .inSpans(UnderlineSpan()) { append("subrayado") }
            .append(" y ")
            .inSpans(ForegroundColorSpan(primario)) { append("color") }
            .append(".")
        vm.tamanoTexto.observe(viewLifecycleOwner) { sp ->
            b.tvAjustable.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
            b.tvAjustable.text = getString(R.string.s5_ajustable, sp.toInt())
        }
        b.swMayus.setOnCheckedChangeListener { _, activo ->
            listOf<TextView>(b.tvDisplay, b.tvHeadline, b.tvTitle, b.tvBody).forEach { it.isAllCaps = activo }
        }

        // 2. Imágenes: modos de escalado aplicados a la imagen local y a la remota.
        fun cargarRemota() {
            b.ivRemota.load("https://picsum.photos/seed/$semillaImagen/600/400") {
                crossfade(true)
                placeholder(R.drawable.ic_image)
                error(R.drawable.ic_image)
            }
        }
        cargarRemota()
        b.btnOtraImagen.setOnClickListener { semillaImagen++; cargarRemota() }
        b.cgEscala.setOnCheckedStateChangeListener { _, ids ->
            val escala = when (ids.firstOrNull()) {
                R.id.chipFit -> ImageView.ScaleType.FIT_CENTER
                R.id.chipCenter -> ImageView.ScaleType.CENTER
                R.id.chipXY -> ImageView.ScaleType.FIT_XY
                else -> ImageView.ScaleType.CENTER_CROP
            }
            b.ivLocal.scaleType = escala
            b.ivRemota.scaleType = escala
        }

        // 3. Progreso determinado e indeterminado.
        fun mostrarProgreso() {
            b.lpDeterminado.setProgressCompat(progreso, true)
            b.cpDeterminado.setProgressCompat(progreso, true)
            b.tvProgreso.text = getString(R.string.s5_prog_resp, progreso)
        }
        mostrarProgreso()
        b.btnMenos.setOnClickListener { progreso = (progreso - 10).coerceAtLeast(0); mostrarProgreso() }
        b.btnMas.setOnClickListener { progreso = (progreso + 10).coerceAtMost(100); mostrarProgreso() }
        b.btnSimular.setOnClickListener {
            simulacion?.cancel()
            simulacion = viewLifecycleOwner.lifecycleScope.launch {
                progreso = 0
                while (progreso < 100) {
                    mostrarProgreso()
                    delay(150)
                    progreso += 5
                }
                mostrarProgreso()
            }
        }
        b.swIndeterminado.setOnCheckedChangeListener { _, activo -> b.filaIndeterminados.isVisible = activo }

        // 4. Toast y Snackbar con acción.
        b.btnToast.setOnClickListener {
            Toast.makeText(requireContext(), R.string.s5_toast_msg, Toast.LENGTH_SHORT).show()
        }
        b.btnSnackbar.setOnClickListener {
            b.tvMensajes.text = getString(R.string.s5_snack_msg)
            Snackbar.make(b.root, R.string.s5_snack_msg, Snackbar.LENGTH_LONG)
                .setAction(R.string.deshacer) { b.tvMensajes.setText(R.string.s5_snack_resp) }
                .show()
        }

        // 5. Diálogo de confirmación.
        b.btnDialogo.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.ic_delete)
                .setTitle(R.string.s5_dialogo_titulo)
                .setMessage(R.string.s5_dialogo_msg)
                .setNegativeButton(R.string.cancelar) { _, _ -> b.tvDialogo.setText(R.string.s5_dialogo_no) }
                .setPositiveButton(R.string.eliminar) { _, _ -> b.tvDialogo.setText(R.string.s5_dialogo_si) }
                .show()
        }

        // 6. Hoja inferior con opciones.
        b.btnHoja.setOnClickListener {
            val hoja = BottomSheetDialog(requireContext())
            val contenido = layoutInflater.inflate(R.layout.hoja_opciones, null)
            listOf(R.id.opcCompartir, R.id.opcEditar, R.id.opcEliminar).forEach { id ->
                val opcion = contenido.findViewById<TextView>(id)
                opcion.setOnClickListener {
                    b.tvHoja.text = getString(R.string.s5_hoja_resp, opcion.text)
                    hoja.dismiss()
                }
            }
            hoja.setContentView(contenido)
            hoja.show()
        }

        // 7. Tarjeta seleccionable y badge con contador.
        b.cardEjemplo.setOnClickListener {
            b.cardEjemplo.isChecked = !b.cardEjemplo.isChecked
            b.tvTarjeta.setText(if (b.cardEjemplo.isChecked) R.string.s5_tarjeta_marcada else R.string.s5_tarjeta_no)
        }
        fun mostrarBadge() {
            b.tvBadge.isVisible = notificaciones > 0
            b.tvBadge.text = if (notificaciones > 99) "99+" else notificaciones.toString()
            b.tvBadgeInfo.text = getString(R.string.s5_badge_resp, notificaciones)
        }
        mostrarBadge()
        b.btnNotificar.setOnClickListener { notificaciones++; mostrarBadge() }
        b.btnLimpiar.setOnClickListener { notificaciones = 0; mostrarBadge() }
    }
}
