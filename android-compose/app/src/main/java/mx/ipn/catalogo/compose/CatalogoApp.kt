package mx.ipn.catalogo.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import mx.ipn.catalogo.compose.ui.componentes.Espacio
import mx.ipn.catalogo.compose.ui.componentes.LocalSnackbar
import mx.ipn.catalogo.compose.ui.secciones.BotonesPantalla
import mx.ipn.catalogo.compose.ui.secciones.ContenedoresPantalla
import mx.ipn.catalogo.compose.ui.secciones.EntradaPantalla
import mx.ipn.catalogo.compose.ui.secciones.InformacionPantalla
import mx.ipn.catalogo.compose.ui.secciones.InicioPantalla
import mx.ipn.catalogo.compose.ui.secciones.ListasPantalla
import mx.ipn.catalogo.compose.ui.secciones.SeleccionPantalla

/**
 * Estructura principal: menú lateral (ModalNavigationDrawer), barra superior (TopAppBar)
 * y un NavHost con un destino composable por sección.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoApp(vm: CatalogoViewModel = viewModel()) {
    val nav = rememberNavController()
    val menu = rememberDrawerState(DrawerValue.Closed)
    val alcance = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val entrada by nav.currentBackStackEntryAsState()
    val actual = Destino.porRuta(entrada?.destination?.route)
    val enSeccion = actual != Destino.INICIO

    val ir: (Destino) -> Unit = { destino ->
        if (destino == Destino.INICIO) {
            nav.popBackStack(Destino.INICIO.ruta, inclusive = false)
        } else {
            nav.navigate(destino.ruta) {
                popUpTo(Destino.INICIO.ruta)
                launchSingleTop = true
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = menu,
        drawerContent = {
            ModalDrawerSheet {
                Column(Modifier.padding(Espacio.l)) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                        Icon(
                            Icons.Filled.Dashboard, contentDescription = null,
                            modifier = Modifier.padding(10.dp).size(28.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                    Spacer(Modifier.height(Espacio.m))
                    Text("Catálogo UI · Compose", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "Catálogo de elementos de interfaz",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                HorizontalDivider()
                Spacer(Modifier.height(Espacio.s))
                Destino.entries.forEach { destino ->
                    NavigationDrawerItem(
                        label = { Text(destino.titulo) },
                        icon = { Icon(destino.icono, contentDescription = null) },
                        selected = destino == actual,
                        onClick = {
                            alcance.launch { menu.close() }
                            ir(destino)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (enSeccion) actual.titulo else "Catálogo UI · Compose") },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (enSeccion) nav.popBackStack() else alcance.launch { menu.open() }
                        }) {
                            if (enSeccion) Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                            else Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    actions = {
                        if (enSeccion) {
                            IconButton(onClick = { ir(Destino.INICIO) }) {
                                Icon(Icons.Filled.Home, contentDescription = "Ir a la pantalla principal")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                )
            },
            snackbarHost = { SnackbarHost(snackbar) },
        ) { relleno ->
            CompositionLocalProvider(LocalSnackbar provides snackbar) {
                NavHost(
                    navController = nav,
                    startDestination = Destino.INICIO.ruta,
                    modifier = Modifier.padding(relleno),
                ) {
                    composable(Destino.INICIO.ruta) { InicioPantalla(vm, alAbrir = ir) }
                    composable(Destino.ENTRADA.ruta) { EntradaPantalla(vm) }
                    composable(Destino.BOTONES.ruta) { BotonesPantalla() }
                    composable(Destino.SELECCION.ruta) { SeleccionPantalla(vm) }
                    composable(Destino.LISTAS.ruta) { ListasPantalla(vm) }
                    composable(Destino.INFORMACION.ruta) { InformacionPantalla(vm) }
                    composable(Destino.CONTENEDORES.ruta) { ContenedoresPantalla() }
                }
            }
        }
    }
}
