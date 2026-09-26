package mx.ipn.catalogo.views.ui.adaptadores

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.ipn.catalogo.views.data.ColorMuestra
import mx.ipn.catalogo.views.databinding.ItemColorBinding

/** Adaptador de la cuadrícula de colores. */
class ColorAdapter(
    private val colores: List<ColorMuestra>,
    private val alTocar: (ColorMuestra) -> Unit,
) : RecyclerView.Adapter<ColorAdapter.Celda>() {

    class Celda(val b: ItemColorBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Celda(ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = colores.size

    override fun onBindViewHolder(holder: Celda, position: Int) {
        val c = colores[position]
        holder.b.muestra.backgroundTintList = ColorStateList.valueOf(c.valor)
        holder.b.nombreColor.text = c.nombre
        holder.b.root.setOnClickListener { alTocar(c) }
    }
}
