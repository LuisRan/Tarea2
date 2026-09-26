@file:OptIn(ExperimentalMaterial3Api::class)

package mx.ipn.catalogo.compose.ui.secciones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import mx.ipn.catalogo.compose.ui.componentes.DemoCard
import mx.ipn.catalogo.compose.ui.componentes.Espacio
import mx.ipn.catalogo.compose.ui.componentes.PantallaSeccion
import mx.ipn.catalogo.compose.ui.componentes.Resultado

/** Sección 2: Botones y acciones. */
@Composable
fun BotonesPantalla() {
    PantallaSeccion("Los botones ejecutan acciones. Cada uno responde al pulsarlo mostrando un mensaje.") {
        BotonesBasicos()
        BotonesIcono()
        BotonesFlotantes()
        BotonSegmentado()
        BotonesEstados()
    }
}

@Composable
private fun BotonesBasicos() {
    var mensaje by rememberSaveable { mutableStateOf("Toca un botón.") }
    var total by rememberSaveable { mutableIntStateOf(0) }
    val pulsar = { nombre: String ->
        total++
        mensaje = "Pulsaste «$nombre» ($total veces en total)"
    }
    DemoCard(
        "Botón relleno, con contorno y de texto", "Button / OutlinedButton / TextButton",
        "Representan distintos niveles de énfasis: el relleno es la acción principal, el de contorno una secundaria y el de texto una acción de baja prioridad.",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.s)) {
            Button(onClick = { pulsar("Relleno") }) { Text("Relleno") }
            OutlinedButton(onClick = { pulsar("Contorno") }) { Text("Contorno") }
            TextButton(onClick = { pulsar("Texto") }) { Text("Texto") }
        }
        Resultado(mensaje)
    }
}

@Composable
private fun BotonesIcono() {
    var favorito by rememberSaveable { mutableStateOf(false) }
    var mensaje by rememberSaveable { mutableStateOf("Toca un botón.") }
    DemoCard(
        "Botones con ícono", "FilledIconToggleButton / OutlinedIconButton / Button + Icon",
        "Un botón de solo ícono ahorra espacio en acciones reconocibles; el de ícono más texto refuerza el significado de la acción.",
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Espacio.s)) {
            FilledIconToggleButton(checked = favorito, onCheckedChange = {
                favorito = it
                mensaje = if (it) "Agregado a favoritos ♥" else "Quitado de favoritos"
            }) {
                Icon(
                    if (favorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Marcar como favorito",
                )
            }
            OutlinedIconButton(onClick = { mensaje = "Acción: compartir" }) {
                Icon(Icons.Filled.Share, contentDescription = "Compartir")
            }
            Button(onClick = { mensaje = "Mensaje enviado ✉" }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Enviar")
            }
        }
        Resultado(mensaje)
    }
}

@Composable
private fun BotonesFlotantes() {
    var contador by rememberSaveable { mutableIntStateOf(0) }
    var extendido by rememberSaveable { mutableStateOf(true) }
    DemoCard(
        "Botón de acción flotante (FAB)", "FloatingActionButton / ExtendedFloatingActionButton",
        "Destaca la acción más importante de una pantalla. La versión extendida agrega una etiqueta y puede contraerse para ahorrar espacio.",
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Espacio.m)) {
            FloatingActionButton(onClick = { contador++ }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar")
            }
            ExtendedFloatingActionButton(
                text = { Text("Redactar") },
                icon = { Icon(Icons.Filled.Edit, contentDescription = "Redactar") },
                onClick = { extendido = !extendido },
                expanded = extendido,
            )
        }
        Resultado("FAB: $contador · Extendido: ${if (extendido) "extendido" else "contraído"}")
    }
}

@Composable
private fun BotonSegmentado() {
    val opciones = listOf("Día", "Semana", "Mes")
    var elegido by rememberSaveable { mutableIntStateOf(0) }
    DemoCard(
        "Selector segmentado (toggle)", "SingleChoiceSegmentedButtonRow + SegmentedButton",
        "Agrupa opciones relacionadas en las que solo una puede estar activa, como cambiar entre vistas de día, semana o mes.",
    ) {
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            opciones.forEachIndexed { i, opcion ->
                SegmentedButton(
                    selected = elegido == i,
                    onClick = { elegido = i },
                    shape = SegmentedButtonDefaults.itemShape(index = i, count = opciones.size),
                ) { Text(opcion) }
            }
        }
        Resultado("Vista actual: ${opciones[elegido]}")
    }
}

@Composable
private fun BotonesEstados() {
    var habilitado by rememberSaveable { mutableStateOf(false) }
    var cargando by rememberSaveable { mutableStateOf(false) }
    var mensaje by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(cargando) {
        if (cargando) {
            delay(2000)
            cargando = false
            mensaje = "Descarga completa ✔"
        }
    }
    DemoCard(
        "Botón deshabilitado y en estado de carga", "Button(enabled = false) + CircularProgressIndicator",
        "Un botón deshabilitado indica que la acción no está disponible todavía. El estado de carga informa que la acción está en proceso y evita pulsaciones repetidas.",
    ) {
        ListItem(
            headlineContent = { Text("Habilitar el botón") },
            trailingContent = { Switch(checked = habilitado, onCheckedChange = { habilitado = it }) },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        )
        Button(onClick = { mensaje = "¡Pedido confirmado!" }, enabled = habilitado) {
            Text("Confirmar pedido")
        }
        FilledTonalButton(
            onClick = { cargando = true; mensaje = "Descargando…" },
            enabled = !cargando,
            modifier = Modifier.width(200.dp),
        ) {
            if (cargando) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            else Text("Descargar archivo")
        }
        if (mensaje.isNotEmpty()) Resultado(mensaje)
    }
}
