package mx.ipn.catalogo.views.ui.adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.ipn.catalogo.views.data.Elemento
import mx.ipn.catalogo.views.databinding.ItemCompactoBinding
import mx.ipn.catalogo.views.databinding.ItemEncabezadoBinding

/** Filas de la lista con encabezados: dos tipos de elemento distintos. */
sealed class Fila {
    data class Encabezado(val texto: String) : Fila()
    data class Item(val elemento: Elemento) : Fila()
}

class SeccionadoAdapter(
    private val alTocar: (Elemento) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filas: List<Fila> = emptyList()

    fun enviar(nuevas: List<Fila>) {
        filas = nuevas
        notifyDataSetChanged()
    }

    class EncabezadoVH(val b: ItemEncabezadoBinding) : RecyclerView.ViewHolder(b.root)
    class ItemVH(val b: ItemCompactoBinding) : RecyclerView.ViewHolder(b.root)

    override fun getItemViewType(position: Int) = when (filas[position]) {
        is Fila.Encabezado -> TIPO_ENCABEZADO
        is Fila.Item -> TIPO_ITEM
    }

    override fun getItemCount() = filas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TIPO_ENCABEZADO)
            EncabezadoVH(ItemEncabezadoBinding.inflate(inflater, parent, false))
        else ItemVH(ItemCompactoBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val fila = filas[position]) {
            is Fila.Encabezado -> (holder as EncabezadoVH).b.encabezado.text = fila.texto
            is Fila.Item -> (holder as ItemVH).b.texto.apply {
                text = fila.elemento.nombre
                setOnClickListener { alTocar(fila.elemento) }
            }
        }
    }

    companion object {
        const val TIPO_ENCABEZADO = 0
        const val TIPO_ITEM = 1
    }
}
