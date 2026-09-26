package mx.ipn.catalogo.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import mx.ipn.catalogo.compose.data.DatosCatalogo
import mx.ipn.catalogo.compose.data.Elemento

/**
 * Estado compartido entre pantallas.
 * - elementos: conecta la Sección 1 (captura) con la Sección 4 (listas).
 * - tamanoTexto: conecta la Sección 3 (deslizador) con la Sección 5 (textos).
 */
class CatalogoViewModel : ViewModel() {

    private val agregados = mutableListOf<Elemento>()
    private var siguienteId = 1000L

    val elementos = mutableStateListOf<Elemento>().apply { addAll(DatosCatalogo.iniciales()) }

    var tamanoTexto by mutableFloatStateOf(18f)
        private set

    fun agregar(nombre: String) {
        val nuevo = Elemento(
            id = siguienteId++,
            nombre = nombre.trim(),
            categoria = DatosCatalogo.AGREGADOS,
            descripcion = "Elemento capturado en la Sección 1 (Entrada de texto).",
        )
        agregados += nuevo
        elementos.add(0, nuevo)
    }

    fun eliminar(elemento: Elemento) {
        elementos.removeAll { it.id == elemento.id }
    }

    fun restaurar(elemento: Elemento, posicion: Int) {
        if (elementos.none { it.id == elemento.id }) {
            elementos.add(posicion.coerceIn(0, elementos.size), elemento)
        }
    }

    fun vaciar() = elementos.clear()

    /** Recarga la lista original más lo agregado desde la Sección 1. */
    fun recargar() {
        elementos.clear()
        elementos.addAll(agregados.reversed() + DatosCatalogo.iniciales())
    }

    fun cambiarTamanoTexto(sp: Float) {
        tamanoTexto = sp
    }
}
