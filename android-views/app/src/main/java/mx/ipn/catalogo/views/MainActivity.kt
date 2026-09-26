package mx.ipn.catalogo.views

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import mx.ipn.catalogo.views.databinding.ActivityMainBinding

/**
 * Actividad única. Contiene la barra superior, el menú lateral (NavigationView)
 * y el contenedor donde se reemplazan los Fragments de cada sección.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var enSeccion = false

    private val cerrarMenu = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            binding.drawer.closeDrawer(GravityCompat.START)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón de navegación: abre el menú en Inicio y regresa en las secciones.
        binding.toolbar.setNavigationOnClickListener {
            if (enSeccion) onBackPressedDispatcher.onBackPressed()
            else binding.drawer.openDrawer(GravityCompat.START)
        }
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.accion_inicio) {
                abrir(Seccion.INICIO); true
            } else false
        }

        binding.navView.setNavigationItemSelectedListener { item ->
            abrir(Seccion.porMenu(item.itemId))
            binding.drawer.closeDrawer(GravityCompat.START)
            true
        }

        binding.drawer.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) { cerrarMenu.isEnabled = true }
            override fun onDrawerClosed(drawerView: View) { cerrarMenu.isEnabled = false }
        })
        onBackPressedDispatcher.addCallback(this, cerrarMenu)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.contenedor, Seccion.INICIO.crearFragment())
            }
        }
    }

    /** Navega a una sección. Inicio limpia la pila; las secciones se apilan sobre Inicio. */
    fun abrir(seccion: Seccion) {
        val fm = supportFragmentManager
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        if (seccion == Seccion.INICIO) return
        fm.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out,
            )
            replace(R.id.contenedor, seccion.crearFragment())
            addToBackStack(seccion.name)
        }
    }

    /** Cada Fragment llama a este método al mostrarse para actualizar barra y menú. */
    fun mostrar(seccion: Seccion) {
        enSeccion = seccion != Seccion.INICIO
        binding.toolbar.title = getString(
            if (seccion == Seccion.INICIO) R.string.app_name else seccion.titulo
        )
        binding.toolbar.setNavigationIcon(if (enSeccion) R.drawable.ic_arrow_back else R.drawable.ic_menu)
        binding.toolbar.navigationContentDescription =
            getString(if (enSeccion) R.string.regresar else R.string.abrir_menu)
        binding.toolbar.menu.findItem(R.id.accion_inicio)?.isVisible = enSeccion
        binding.navView.setCheckedItem(seccion.menuId)
    }
}
