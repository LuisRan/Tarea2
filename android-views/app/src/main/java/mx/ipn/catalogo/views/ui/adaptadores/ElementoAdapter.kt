package mx.ipn.catalogo.views.ui.adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mx.ipn.catalogo.views.data.Elemento
import mx.ipn.catalogo.views.databinding.ItemElementoBinding

/** Adaptador de la lista vertical. ListAdapter calcula las diferencias con DiffUtil. */
class ElementoAdapter(
    private val alTocar: (Elemento) -> Unit,
) : ListAdapter<Elemento, ElementoAdapter.Fila>(Diferencias) {

    class Fila(val b: ItemElementoBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Fila(ItemElementoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Fila, position: Int) {
        val e = getItem(position)
        holder.b.avatar.text = e.nombre.take(1).uppercase()
        holder.b.nombre.text = e.nombre
        holder.b.categoria.text = e.categoria
        holder.b.root.setOnClickListener { alTocar(e) }
    }

    object Diferencias : DiffUtil.ItemCallback<Elemento>() {
        override fun areItemsTheSame(a: Elemento, b: Elemento) = a.id == b.id
        override fun areContentsTheSame(a: Elemento, b: Elemento) = a == b
    }
}
