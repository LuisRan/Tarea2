@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package mx.ipn.catalogo.compose.ui.secciones

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mx.ipn.catalogo.compose.CatalogoViewModel
import mx.ipn.catalogo.compose.data.DatosCatalogo
import mx.ipn.catalogo.compose.data.Elemento
import mx.ipn.catalogo.compose.ui.componentes.DemoCard
import mx.ipn.catalogo.compose.ui.componentes.Espacio
import mx.ipn.catalogo.compose.ui.componentes.LocalSnackbar
import mx.ipn.catalogo.compose.ui.componentes.PantallaSeccion
import mx.ipn.catalogo.compose.ui.componentes.Resultado

/** Sección 4: Listas y colecciones. */
@Composable
fun ListasPantalla(vm: CatalogoViewModel) {
    var detalle by remember { mutableStateOf<Elemento?>(null) }

    PantallaSeccion("Las listas muestran colecciones de datos. La lista de esta sección es compartida con la Sección 1.") {
        ListaVertical(vm, alTocar = { detalle = it })
        DemoCard(
            "Estado vacío", "Composable condicional (if lista.isEmpty())",
            "Cuando no hay elementos se muestra un mensaje con una ilustración en lugar de un espacio en blanco. Pulsa «Vaciar lista» para verlo arriba.",
        ) {
            OutlinedButton(onClick = { vm.vaciar() }) { Text("Vaciar lista") }
        }
        Cuadricula()
        ListaConEncabezados(vm, alTocar = { detalle = it })
        Pestanas()
    }

    detalle?.let { e ->
        AlertDialog(
            onDismissRequest = { detalle = null },
            title = { Text(e.nombre) },
            text = { Text("Categoría: ${e.categoria}\n\n${e.descripcion}") },
            confirmButton = { TextButton(onClick = { detalle = null }) { Text("Cerrar") } },
        )
    }
}

@Composable
private fun ListaVertical(vm: CatalogoViewModel, alTocar: (Elemento) -> Unit) {
    val snackbar = LocalSnackbar.current
    val alcance = rememberCoroutineScope()
    val contexto = LocalContext.current
    var actualizando by remember { mutableStateOf(false) }

    LaunchedEffect(actualizando) {
        if (actualizando) {
            delay(1200)
            vm.recargar()
            actualizando = false
            Toast.makeText(contexto, "Lista actualizada", Toast.LENGTH_SHORT).show()
        }
    }

    DemoCard(
        "Lista vertical (con detalle, deslizar y actualizar)", "LazyColumn + SwipeToDismissBox + PullToRefreshBox",
        "Muestra muchos elementos de forma eficiente componiendo solo los visibles. Toca un elemento para ver su detalle, deslízalo a un lado para eliminarlo o arrastra hacia abajo para actualizar la lista.",
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${vm.elementos.size} elementos",
                Modifier.weight(1f),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            TextButton(onClick = { vm.vaciar() }) {
                Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Vaciar lista")
            }
        }
        PullToRefreshBox(
            isRefreshing = actualizando,
            onRefresh = { actualizando = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        ) {
            if (vm.elementos.isEmpty()) {
                EstadoVacio(alRestaurar = { vm.recargar() })
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    itemsIndexed(vm.elementos, key = { _, e -> e.id }) { indice, e ->
                        FilaDeslizable(
                            elemento = e,
                            alTocar = { alTocar(e) },
                            alEliminar = {
                                vm.eliminar(e)
                                alcance.launch {
                                    val r = snackbar.showSnackbar(
                                        "Eliminaste «${e.nombre}»", actionLabel = "Deshacer",
                                        duration = SnackbarDuration.Short,
                                    )
                                    if (r == SnackbarResult.ActionPerformed) vm.restaurar(e, indice)
                                }
                            },
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaDeslizable(elemento: Elemento, alTocar: () -> Unit, alEliminar: () -> Unit) {
    val estado = rememberSwipeToDismissBoxState(
        confirmValueChange = { valor ->
            if (valor != SwipeToDismissBoxValue.Settled) {
                alEliminar(); true
            } else false
        },
    )
    SwipeToDismissBox(
        state = estado,
        backgroundContent = {
            val alineacion = if (estado.dismissDirection == SwipeToDismissBoxValue.StartToEnd)
                Alignment.CenterStart else Alignment.CenterEnd
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.error)
                    .padding(horizontal = Espacio.l),
                contentAlignment = alineacion,
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.onError)
            }
        },
    ) {
        ListItem(
            headlineContent = { Text(elemento.nombre) },
            supportingContent = { Text(elemento.categoria) },
            leadingContent = {
                Box(
                    Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        elemento.nombre.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            },
            trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            modifier = Modifier.clickable(onClick = alTocar),
        )
    }
}

@Composable
private fun EstadoVacio(alRestaurar: () -> Unit) {
    // verticalScroll permite seguir arrastrando hacia abajo para actualizar aun sin elementos.
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Espacio.l),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.height(Espacio.l))
        Icon(
            Icons.Filled.Inbox, contentDescription = "No hay elementos",
            modifier = Modifier.size(96.dp), tint = MaterialTheme.colorScheme.outline,
        )
        Text("No hay elementos", style = MaterialTheme.typography.titleMedium)
        Text(
            "Agrega uno desde la Sección 1 o restaura la lista.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Espacio.m))
        Button(onClick = alRestaurar) {
            Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text("Restaurar lista")
        }
    }
}

@Composable
private fun Cuadricula() {
    var elegido by rememberSaveable { mutableStateOf<String?>(null) }
    val colorElegido = DatosCatalogo.colores.firstOrNull { it.nombre == elegido }
    DemoCard(
        "Cuadrícula", "LazyVerticalGrid(GridCells.Fixed(4))",
        "Organiza los elementos en filas y columnas; es ideal para contenido visual como colores, fotos o productos.",
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .height(252.dp),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.spacedBy(Espacio.s),
            verticalArrangement = Arrangement.spacedBy(Espacio.s),
        ) {
            items(DatosCatalogo.colores, key = { it.nombre }) { c ->
                Column(
                    Modifier.clickable { elegido = c.nombre },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(Color(c.valor), RoundedCornerShape(12.dp)),
                    )
                    Text(c.nombre, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        Text(
            colorElegido?.let { "Elegiste el color: ${it.nombre}" } ?: "Toca un color.",
            style = MaterialTheme.typography.bodyMedium,
            color = colorElegido?.let { Color(it.valor) } ?: MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun ListaConEncabezados(vm: CatalogoViewModel, alTocar: (Elemento) -> Unit) {
    val grupos = vm.elementos.groupBy { it.categoria }
    DemoCard(
        "Lista con encabezados de sección", "LazyColumn + stickyHeader (2 tipos de elemento)",
        "Combina dos tipos de elemento: encabezados que agrupan y filas de contenido. Aquí se agrupa la lista compartida por categoría; el encabezado se queda fijo al desplazarse.",
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .height(300.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        ) {
            grupos.forEach { (categoria, elementos) ->
                stickyHeader(key = "encabezado_$categoria") {
                    Text(
                        "$categoria (${elementos.size})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(horizontal = Espacio.m, vertical = Espacio.s),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
                items(elementos, key = { "item_${it.id}" }) { e ->
                    Text(
                        e.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alTocar(e) }
                            .padding(horizontal = Espacio.m, vertical = 12.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

private data class Pagina(val titulo: String, val texto: String, val icono: ImageVector)

@Composable
private fun Pestanas() {
    val paginas = listOf(
        Pagina("Resumen", "Esta es la primera página. Desliza hacia la izquierda para ver la siguiente.", Icons.Filled.Home),
        Pagina("Detalles", "Segunda página: las pestañas se sincronizan con el deslizamiento.", Icons.Filled.Info),
        Pagina("Ajustes", "Tercera página: toca «Resumen» para volver al inicio.", Icons.Filled.Settings),
    )
    val estado = rememberPagerState(pageCount = { paginas.size })
    val alcance = rememberCoroutineScope()
    DemoCard(
        "Pestañas con contenido deslizable", "TabRow + HorizontalPager",
        "Dividen contenido relacionado en páginas. Puedes tocar una pestaña o deslizar horizontalmente para cambiar de página.",
    ) {
        TabRow(selectedTabIndex = estado.currentPage) {
            paginas.forEachIndexed { i, p ->
                Tab(
                    selected = estado.currentPage == i,
                    onClick = { alcance.launch { estado.animateScrollToPage(i) } },
                    text = { Text(p.titulo) },
                    icon = { Icon(p.icono, contentDescription = null) },
                )
            }
        }
        HorizontalPager(
            state = estado,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
        ) { pagina ->
            val p = paginas[pagina]
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(Espacio.m),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(p.icono, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                Text(p.titulo, style = MaterialTheme.typography.titleLarge)
                Text(p.texto, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            }
        }
        Resultado("Página ${estado.currentPage + 1} de ${paginas.size}")
    }
}
