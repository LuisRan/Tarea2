@file:OptIn(ExperimentalMaterial3Api::class)

package mx.ipn.catalogo.compose.ui.secciones

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mx.ipn.catalogo.compose.R
import mx.ipn.catalogo.compose.ui.componentes.DemoCard
import mx.ipn.catalogo.compose.ui.componentes.Espacio
import mx.ipn.catalogo.compose.ui.componentes.PantallaSeccion
import mx.ipn.catalogo.compose.ui.componentes.Resultado
import kotlin.math.roundToInt

/** Sección 6: Contenedores y estructura. */
@Composable
fun ContenedoresPantalla() {
    PantallaSeccion("Los contenedores organizan a otros elementos en la pantalla.") {
        FilaYColumna()
        Superpuesta()
        Desplazable()
        BarraSuperior()
        NavegacionInferior()
        PesosYSesgo()
    }
}

/** Caja de color reutilizada en las demostraciones de distribución. */
@Composable
private fun Caja(texto: String, modifier: Modifier = Modifier, fondo: Color = MaterialTheme.colorScheme.primaryContainer) {
    Box(
        modifier
            .padding(Espacio.xs)
            .background(fondo, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(texto, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
private fun FilaYColumna() {
    var esFila by rememberSaveable { mutableStateOf(true) }
    DemoCard(
        "Distribución en fila y en columna", "Row / Column",
        "Row acomoda a sus hijos en horizontal y Column en vertical. Cambia la distribución con el selector.",
    ) {
        SingleChoiceSegmentedButtonRow {
            SegmentedButton(
                selected = esFila, onClick = { esFila = true },
                shape = SegmentedButtonDefaults.itemShape(0, 2),
            ) { Text("Fila") }
            SegmentedButton(
                selected = !esFila, onClick = { esFila = false },
                shape = SegmentedButtonDefaults.itemShape(1, 2),
            ) { Text("Columna") }
        }
        Box(Modifier.animateContentSize()) {
            if (esFila) {
                Row { listOf("A", "B", "C").forEach { Caja(it, Modifier.size(56.dp)) } }
            } else {
                Column { listOf("A", "B", "C").forEach { Caja(it, Modifier.size(56.dp)) } }
            }
        }
    }
}

@Composable
private fun Superpuesta() {
    val posiciones = listOf(
        Alignment.TopStart to "arriba a la izquierda",
        Alignment.Center to "centro",
        Alignment.BottomEnd to "abajo a la derecha",
    )
    var indice by rememberSaveable { mutableIntStateOf(1) }
    DemoCard(
        "Distribución superpuesta", "Box",
        "Box apila elementos uno encima de otro, como un texto sobre una imagen. Toca la imagen para mover la capa superior.",
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { indice = (indice + 1) % posiciones.size },
        ) {
            Image(
                painterResource(R.drawable.paisaje), contentDescription = "Paisaje de montañas incluido en la app",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(),
            )
            Text(
                "Capa superior",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(posiciones[indice].first)
                    .padding(Espacio.s)
                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            )
        }
        Resultado("Posición de la capa: ${posiciones[indice].second}")
    }
}

@Composable
private fun Desplazable() {
    val estado = rememberScrollState()
    val alcance = rememberCoroutineScope()
    DemoCard(
        "Contenedor con desplazamiento", "Modifier.verticalScroll(rememberScrollState())",
        "Permite ver contenido más alto que el espacio disponible desplazándolo verticalmente. Desliza dentro del recuadro o usa los botones.",
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .verticalScroll(estado)
                .padding(Espacio.s),
        ) {
            for (i in 1..30) {
                Text("Renglón número $i del contenido desplazable", Modifier.padding(vertical = 4.dp))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.s)) {
            FilledTonalButton(onClick = { alcance.launch { estado.animateScrollTo(estado.maxValue) } }) { Text("Ir al final") }
            TextButton(onClick = { alcance.launch { estado.animateScrollTo(0) } }) { Text("Ir al inicio") }
        }
    }
}

@Composable
private fun BarraSuperior() {
    var accion by rememberSaveable { mutableStateOf("—") }
    var menuAbierto by remember { mutableStateOf(false) }
    DemoCard(
        "Barra superior", "TopAppBar (title, navigationIcon, actions)",
        "Muestra el título de la pantalla y acciones frecuentes. La app entera usa una barra superior con menú lateral.",
    ) {
        TopAppBar(
            title = { Text("Mi bandeja") },
            navigationIcon = {
                IconButton(onClick = { accion = "Menú" }) { Icon(Icons.Filled.Menu, contentDescription = "Menú") }
            },
            actions = {
                IconButton(onClick = { accion = "Buscar" }) { Icon(Icons.Filled.Search, contentDescription = "Buscar") }
                IconButton(onClick = { accion = "Compartir" }) { Icon(Icons.Filled.Share, contentDescription = "Compartir") }
                Box {
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
                    }
                    DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                        DropdownMenuItem(text = { Text("Ajustes") }, onClick = { accion = "Ajustes"; menuAbierto = false })
                    }
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.clip(RoundedCornerShape(12.dp)),
        )
        Resultado("Acción de la barra: $accion")
    }
}

@Composable
private fun NavegacionInferior() {
    val destinos = listOf(
        Triple("Inicio", Icons.Filled.Home, 0),
        Triple("Favoritos", Icons.Filled.Star, 3),
        Triple("Perfil", Icons.Filled.Person, 0),
    )
    var elegido by rememberSaveable { mutableIntStateOf(0) }
    var favoritosVistos by rememberSaveable { mutableStateOf(false) }
    DemoCard(
        "Barra de navegación inferior", "NavigationBar + NavigationBarItem (y ModalNavigationDrawer en la app)",
        "Cambia entre destinos principales de una app con un toque. Además, esta app usa un menú lateral para ir a las seis secciones.",
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp)),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("Destino: ${destinos[elegido].first}", style = MaterialTheme.typography.titleLarge)
            }
            NavigationBar(windowInsets = WindowInsets(0, 0, 0, 0)) {
                destinos.forEachIndexed { i, (nombre, icono, pendientes) ->
                    NavigationBarItem(
                        selected = elegido == i,
                        onClick = {
                            elegido = i
                            if (i == 1) favoritosVistos = true
                        },
                        icon = {
                            BadgedBox(badge = {
                                if (pendientes > 0 && !favoritosVistos) Badge { Text("$pendientes") }
                            }) { Icon(icono, contentDescription = nombre) }
                        },
                        label = { Text(nombre) },
                    )
                }
            }
        }
    }
}

@Composable
private fun PesosYSesgo() {
    var pesoA by rememberSaveable { mutableFloatStateOf(1f) }
    var sesgo by rememberSaveable { mutableFloatStateOf(50f) }
    DemoCard(
        "Pesos proporcionales y alineación con sesgo", "Modifier.weight / BiasAlignment",
        "Con Modifier.weight el espacio se reparte en proporción. En lugar de restricciones, Compose posiciona con alineaciones; BiasAlignment equivale al sesgo de ConstraintLayout. Mueve los deslizadores.",
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Caja("A", Modifier.weight(pesoA).fillMaxSize())
            Caja("B", Modifier.weight(2f).fillMaxSize(), MaterialTheme.colorScheme.secondaryContainer)
            Caja("C", Modifier.weight(1f).fillMaxSize(), MaterialTheme.colorScheme.tertiaryContainer)
        }
        Slider(value = pesoA, onValueChange = { pesoA = it.roundToInt().toFloat() }, valueRange = 1f..5f, steps = 3)
        Resultado("Pesos → A: ${pesoA.roundToInt()} · B: 2 · C: 1")

        Box(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .padding(Espacio.s),
        ) {
            Caja(
                "2",
                Modifier
                    .size(56.dp)
                    .align(BiasAlignment(horizontalBias = sesgo / 50f - 1f, verticalBias = 0f)),
                MaterialTheme.colorScheme.tertiaryContainer,
            )
        }
        Slider(value = sesgo, onValueChange = { sesgo = it }, valueRange = 0f..100f)
        Resultado("Sesgo horizontal: ${sesgo.roundToInt()} %")
    }
}
