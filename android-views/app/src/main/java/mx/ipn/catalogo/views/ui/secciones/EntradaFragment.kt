package mx.ipn.catalogo.views.ui.secciones

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doAfterTextChanged
import mx.ipn.catalogo.views.R
import mx.ipn.catalogo.views.Seccion
import mx.ipn.catalogo.views.databinding.FragmentEntradaBinding
import mx.ipn.catalogo.views.ui.SeccionFragment

/** Sección 1: Entrada de texto. */
class EntradaFragment : SeccionFragment(R.layout.fragment_entrada, Seccion.ENTRADA) {

    private var _b: FragmentEntradaBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentEntradaBinding.bind(view)

        // 1. Campo simple: saluda con el nombre escrito.
        b.etNombre.doAfterTextChanged { texto ->
            val nombre = texto?.toString()?.trim().orEmpty()
            b.tvSaludo.text = if (nombre.isEmpty()) getString(R.string.s1_simple_vacio)
            else getString(R.string.s1_simple_resp, nombre)
        }

        // 2. Validación de código postal.
        b.etCp.doAfterTextChanged { texto ->
            val cp = texto?.toString().orEmpty()
            when {
                cp.isEmpty() -> {
                    b.tilCp.error = null
                    b.tilCp.helperText = getString(R.string.s1_valid_ayuda)
                }
                cp.length != 5 -> b.tilCp.error = getString(R.string.s1_valid_error)
                else -> {
                    b.tilCp.error = null
                    b.tilCp.helperText = getString(R.string.s1_valid_ok)
                }
            }
        }

        // 3. Contraseña: calcula una seguridad aproximada.
        b.etPass.doAfterTextChanged { texto ->
            val p = texto?.toString().orEmpty()
            var puntos = 0
            if (p.length >= 8) puntos++
            if (p.any { it.isDigit() }) puntos++
            if (p.any { it.isUpperCase() }) puntos++
            if (p.any { !it.isLetterOrDigit() }) puntos++
            b.tvFuerza.setText(
                when {
                    p.isEmpty() -> R.string.s1_pass_vacia
                    puntos <= 1 -> R.string.s1_pass_debil
                    puntos <= 3 -> R.string.s1_pass_media
                    else -> R.string.s1_pass_fuerte
                }
            )
        }

        // 4. Teclado de correo con validación del formato.
        b.etCorreo.doAfterTextChanged { texto ->
            val correo = texto?.toString().orEmpty()
            b.tilCorreo.error = if (correo.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(correo).matches())
                getString(R.string.s1_correo_error) else null
        }

        // 5. Multilínea: cuenta líneas y palabras.
        fun contar(texto: String) {
            val lineas = if (texto.isEmpty()) 0 else texto.count { it == '\n' } + 1
            val palabras = texto.split(Regex("\\s+")).count { it.isNotBlank() }
            b.tvLineas.text = getString(R.string.s1_multi_resp, lineas, palabras)
        }
        contar("")
        b.etNotas.doAfterTextChanged { contar(it?.toString().orEmpty()) }

        // 6. Autocompletar con los estados de la República.
        val estados = resources.getStringArray(R.array.estados)
        b.actvEstado.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, estados)
        )
        b.actvEstado.setOnItemClickListener { parent, _, posicion, _ ->
            b.tvEstado.text = getString(R.string.s1_auto_resp, parent.getItemAtPosition(posicion).toString())
        }

        // 7. Búsqueda dentro de la lista compartida.
        b.buscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                buscar(query.orEmpty()); b.buscador.clearFocus(); return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                buscar(newText.orEmpty()); return true
            }
        })

        // 8. Agregar a la lista de la Sección 4.
        b.btnAgregar.setOnClickListener { agregar() }
        b.etNuevo.setOnEditorActionListener { _, accion, _ ->
            if (accion == EditorInfo.IME_ACTION_DONE) { agregar(); true } else false
        }
        vm.elementos.observe(viewLifecycleOwner) { lista ->
            b.tvTotal.text = getString(R.string.s1_total, lista.size)
            buscar(b.buscador.query?.toString().orEmpty())
        }
    }

    private fun buscar(consulta: String) {
        val q = consulta.trim()
        if (q.isEmpty()) {
            b.tvResultados.text = ""
            return
        }
        val encontrados = vm.elementos.value.orEmpty().filter { it.nombre.contains(q, ignoreCase = true) }
        b.tvResultados.text = if (encontrados.isEmpty()) getString(R.string.s1_buscar_sin, q)
        else getString(R.string.s1_buscar_res, encontrados.size, encontrados.joinToString(", ") { it.nombre })
    }

    private fun agregar() {
        val nombre = b.etNuevo.text?.toString()?.trim().orEmpty()
        if (nombre.isEmpty()) {
            b.tilNuevo.error = getString(R.string.s1_agregar_error)
            return
        }
        b.tilNuevo.error = null
        vm.agregar(nombre)
        b.etNuevo.setText("")
        snackbar(getString(R.string.s1_agregar_ok, nombre))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
