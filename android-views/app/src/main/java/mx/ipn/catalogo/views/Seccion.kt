package mx.ipn.catalogo.views

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import mx.ipn.catalogo.views.ui.InicioFragment
import mx.ipn.catalogo.views.ui.secciones.BotonesFragment
import mx.ipn.catalogo.views.ui.secciones.ContenedoresFragment
import mx.ipn.catalogo.views.ui.secciones.EntradaFragment
import mx.ipn.catalogo.views.ui.secciones.InformacionFragment
import mx.ipn.catalogo.views.ui.secciones.ListasFragment
import mx.ipn.catalogo.views.ui.secciones.SeleccionFragment

/** Destinos de la app: la pantalla principal y las seis secciones del catálogo. */
enum class Seccion(
    @StringRes val titulo: Int,
    @StringRes val descripcion: Int,
    @DrawableRes val icono: Int,
    @IdRes val menuId: Int,
) {
    INICIO(R.string.inicio, R.string.inicio_desc, R.drawable.ic_home, R.id.nav_inicio),
    ENTRADA(R.string.sec1, R.string.sec1_desc, R.drawable.ic_edit, R.id.nav_entrada),
    BOTONES(R.string.sec2, R.string.sec2_desc, R.drawable.ic_touch, R.id.nav_botones),
    SELECCION(R.string.sec3, R.string.sec3_desc, R.drawable.ic_check_box, R.id.nav_seleccion),
    LISTAS(R.string.sec4, R.string.sec4_desc, R.drawable.ic_list, R.id.nav_listas),
    INFORMACION(R.string.sec5, R.string.sec5_desc, R.drawable.ic_info, R.id.nav_informacion),
    CONTENEDORES(R.string.sec6, R.string.sec6_desc, R.drawable.ic_dashboard, R.id.nav_contenedores);

    fun crearFragment(): Fragment = when (this) {
        INICIO -> InicioFragment()
        ENTRADA -> EntradaFragment()
        BOTONES -> BotonesFragment()
        SELECCION -> SeleccionFragment()
        LISTAS -> ListasFragment()
        INFORMACION -> InformacionFragment()
        CONTENEDORES -> ContenedoresFragment()
    }

    companion object {
        fun porMenu(@IdRes id: Int): Seccion = entries.firstOrNull { it.menuId == id } ?: INICIO
    }
}
