package mx.ipn.catalogo.views.ui.adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import mx.ipn.catalogo.views.R
import mx.ipn.catalogo.views.databinding.ItemPaginaBinding

/** Páginas del ViewPager2 usado con TabLayout. */
class PaginasAdapter : RecyclerView.Adapter<PaginasAdapter.Pagina>() {

    data class Contenido(@StringRes val titulo: Int, @StringRes val texto: Int, @DrawableRes val icono: Int)

    val paginas = listOf(
        Contenido(R.string.tab_resumen, R.string.tab_resumen_txt, R.drawable.ic_home),
        Contenido(R.string.tab_detalles, R.string.tab_detalles_txt, R.drawable.ic_info),
        Contenido(R.string.tab_ajustes, R.string.tab_ajustes_txt, R.drawable.ic_settings),
    )

    class Pagina(val b: ItemPaginaBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Pagina(ItemPaginaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = paginas.size

    override fun onBindViewHolder(holder: Pagina, position: Int) {
        val p = paginas[position]
        holder.b.iconoPagina.setImageResource(p.icono)
        holder.b.tituloPagina.setText(p.titulo)
        holder.b.textoPagina.setText(p.texto)
    }
}
