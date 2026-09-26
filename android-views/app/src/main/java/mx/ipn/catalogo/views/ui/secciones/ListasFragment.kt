package mx.ipn.catalogo.views.ui.secciones

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mx.ipn.catalogo.views.R
import mx.ipn.catalogo.views.Seccion
import mx.ipn.catalogo.views.data.DatosCatalogo
import mx.ipn.catalogo.views.data.Elemento
import mx.ipn.catalogo.views.databinding.FragmentListasBinding
import mx.ipn.catalogo.views.ui.SeccionFragment
import mx.ipn.catalogo.views.ui.adaptadores.ColorAdapter
import mx.ipn.catalogo.views.ui.adaptadores.ElementoAdapter
import mx.ipn.catalogo.views.ui.adaptadores.Fila
import mx.ipn.catalogo.views.ui.adaptadores.PaginasAdapter
import mx.ipn.catalogo.views.ui.adaptadores.SeccionadoAdapter

/** Sección 4: Listas y colecciones. */
class ListasFragment : SeccionFragment(R.layout.fragment_listas, Seccion.LISTAS) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentListasBinding.bind(view)

        // 1. Lista vertical.
        val adaptador = ElementoAdapter { mostrarDetalle(it) }
        b.rvLista.layoutManager = LinearLayoutManager(requireContext())
        b.rvLista.adapter = adaptador
        b.rvLista.addItemDecoration(
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                isLastItemDecorated = false
            }
        )

        // Deslizar a cualquier lado para eliminar, con opción de deshacer.
        val pintura = Paint().apply { color = ContextCompat.getColor(requireContext(), R.color.rojo_eliminar) }
        val deslizar = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, destino: RecyclerView.ViewHolder) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, direccion: Int) {
                val posicion = vh.bindingAdapterPosition
                if (posicion == RecyclerView.NO_POSITION) return
                val elemento = adaptador.currentList[posicion]
                vm.eliminar(elemento)
                Snackbar.make(b.root, getString(R.string.s4_eliminado, elemento.nombre), Snackbar.LENGTH_LONG)
                    .setAction(R.string.deshacer) { vm.restaurar(elemento, posicion) }
                    .show()
            }

            override fun onChildDraw(
                c: Canvas, rv: RecyclerView, vh: RecyclerView.ViewHolder,
                dX: Float, dY: Float, estado: Int, activo: Boolean,
            ) {
                val v = vh.itemView
                if (dX > 0) c.drawRect(v.left.toFloat(), v.top.toFloat(), v.left + dX, v.bottom.toFloat(), pintura)
                else if (dX < 0) c.drawRect(v.right + dX, v.top.toFloat(), v.right.toFloat(), v.bottom.toFloat(), pintura)
                super.onChildDraw(c, rv, vh, dX, dY, estado, activo)
            }
        }
        ItemTouchHelper(deslizar).attachToRecyclerView(b.rvLista)

        // Arrastrar hacia abajo para actualizar.
        b.srl.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1200)
                vm.recargar()
                b.srl.isRefreshing = false
                Toast.makeText(requireContext(), R.string.s4_actualizada, Toast.LENGTH_SHORT).show()
            }
        }

        // Estado vacío.
        b.btnVaciar.setOnClickListener { vm.vaciar() }
        b.btnVerVacio.setOnClickListener { vm.vaciar() }
        b.btnRestaurar.setOnClickListener { vm.recargar() }

        // 3. Cuadrícula de colores.
        b.rvCuadricula.layoutManager = GridLayoutManager(requireContext(), 4)
        b.rvCuadricula.adapter = ColorAdapter(DatosCatalogo.colores) { color ->
            b.tvColor.text = getString(R.string.s4_grid_resp, color.nombre)
            b.tvColor.setTextColor(color.valor)
        }

        // 4. Lista con encabezados agrupada por categoría.
        val seccionado = SeccionadoAdapter { mostrarDetalle(it) }
        b.rvSecciones.layoutManager = LinearLayoutManager(requireContext())
        b.rvSecciones.adapter = seccionado

        // Observa la lista compartida (incluye lo agregado en la Sección 1).
        vm.elementos.observe(viewLifecycleOwner) { lista ->
            adaptador.submitList(lista)
            b.tvConteo.text = getString(R.string.s4_conteo, lista.size)
            b.estadoVacio.isVisible = lista.isEmpty()
            b.rvLista.isVisible = lista.isNotEmpty()
            val filas = mutableListOf<Fila>()
            lista.groupBy { it.categoria }.forEach { (categoria, elementos) ->
                filas += Fila.Encabezado(getString(R.string.s4_encabezado, categoria, elementos.size))
                elementos.forEach { filas += Fila.Item(it) }
            }
            seccionado.enviar(filas)
        }

        // 5. Pestañas sincronizadas con ViewPager2.
        val paginas = PaginasAdapter()
        b.pager.adapter = paginas
        TabLayoutMediator(b.tabs, b.pager) { tab, posicion ->
            tab.setText(paginas.paginas[posicion].titulo)
            tab.setIcon(paginas.paginas[posicion].icono)
        }.attach()
    }

    /** Abre el detalle del elemento seleccionado en un diálogo. */
    private fun mostrarDetalle(e: Elemento) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(e.nombre)
            .setMessage(getString(R.string.s4_detalle, e.categoria, e.descripcion))
            .setPositiveButton(R.string.cerrar, null)
            .show()
    }
}
