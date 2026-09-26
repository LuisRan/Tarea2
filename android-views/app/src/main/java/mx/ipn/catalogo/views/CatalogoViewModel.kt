package mx.ipn.catalogo.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mx.ipn.catalogo.views.data.DatosCatalogo
import mx.ipn.catalogo.views.data.Elemento

/**
 * Estado compartido por todas las secciones (vive mientras exista la Activity).
 * - La lista de elementos conecta la Sección 1 (captura) con la Sección 4 (listas).
 * - El tamaño de texto conecta la Sección 3 (deslizador) con la Sección 5 (textos).
 */
class CatalogoViewModel : ViewModel() {

    private val agregados = mutableListOf<Elemento>()
    private var siguienteId = 1000L

    private val _elementos = MutableLiveData(DatosCatalogo.iniciales())
    val elementos: LiveData<List<Elemento>> = _elementos

    private val _tamanoTexto = MutableLiveData(18f)
    val tamanoTexto: LiveData<Float> = _tamanoTexto

    private fun lista() = _elementos.value.orEmpty()

    fun agregar(nombre: String) {
        val nuevo = Elemento(
            id = siguienteId++,
            nombre = nombre.trim(),
            categoria = DatosCatalogo.AGREGADOS,
            descripcion = "Elemento capturado en la Sección 1 (Entrada de texto).",
        )
        agregados += nuevo
        _elementos.value = listOf(nuevo) + lista()
    }

    fun eliminar(elemento: Elemento) {
        _elementos.value = lista().filterNot { it.id == elemento.id }
    }

    fun restaurar(elemento: Elemento, posicion: Int) {
        val nueva = lista().toMutableList()
        nueva.add(posicion.coerceIn(0, nueva.size), elemento)
        _elementos.value = nueva
    }

    fun vaciar() {
        _elementos.value = emptyList()
    }

    /** Recarga la lista original más los elementos agregados desde la Sección 1. */
    fun recargar() {
        _elementos.value = agregados.reversed() + DatosCatalogo.iniciales()
    }

    fun cambiarTamanoTexto(sp: Float) {
        if (_tamanoTexto.value != sp) _tamanoTexto.value = sp
    }
}
