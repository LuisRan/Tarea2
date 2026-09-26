package mx.ipn.catalogo.views.ui

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import mx.ipn.catalogo.views.CatalogoViewModel
import mx.ipn.catalogo.views.MainActivity
import mx.ipn.catalogo.views.Seccion

/** Base de los Fragments de sección: comparte el ViewModel y actualiza la barra superior. */
abstract class SeccionFragment(
    @LayoutRes layout: Int,
    private val seccion: Seccion,
) : Fragment(layout) {

    protected val vm: CatalogoViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.mostrar(seccion)
    }

    protected fun snackbar(mensaje: String) {
        view?.let { Snackbar.make(it, mensaje, Snackbar.LENGTH_SHORT).show() }
    }
}
