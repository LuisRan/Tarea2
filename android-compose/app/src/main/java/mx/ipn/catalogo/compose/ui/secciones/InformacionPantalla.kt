@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package mx.ipn.catalogo.compose.ui.secciones

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mx.ipn.catalogo.compose.CatalogoViewModel
import mx.ipn.catalogo.compose.R
import mx.ipn.catalogo.compose.ui.componentes.DemoCard
import mx.ipn.catalogo.compose.ui.componentes.Espacio
import mx.ipn.catalogo.compose.ui.componentes.LocalSnackbar
import mx.ipn.catalogo.compose.ui.componentes.PantallaSeccion
import mx.ipn.catalogo.compose.ui.componentes.Resultado
import kotlin.math.roundToInt

/** Sección 5: Información y retroalimentación. */
@Composable
fun InformacionPantalla(vm: CatalogoViewModel) {
    PantallaSeccion("Elementos que comunican información y dan retroalimentación al usuario.") {
        Textos(vm)
        Imagenes()
        Progreso()
        Mensajes()
        Dialogo()
        HojaInferior()
        TarjetaSeparadorBadge()
    }
}

@Composable
private fun Textos(vm: CatalogoViewModel) {
    var mayusculas by rememberSaveable { mutableStateOf(false) }
    fun t(s: String) = if (mayusculas) s.uppercase() else s
    val colores = MaterialTheme.colorScheme
    DemoCard(
        "Textos con distintos estilos", "Text (MaterialTheme.typography, FontWeight, AnnotatedString)",
        "La tipografía crea jerarquía: títulos grandes, subtítulos, cuerpo y etiquetas, además de énfasis con negritas, cursivas y color.",
    ) {
        Text(t("Título grande"), style = MaterialTheme.typography.displaySmall)
        Text(t("Encabezado de sección"), style = MaterialTheme.typography.headlineSmall)
        Text(t("Subtítulo mediano"), style = MaterialTheme.typography.titleMedium)
        Text(t("Texto de cuerpo para párrafos largos y descripciones."), style = MaterialTheme.typography.bodyMedium)
        Text("ETIQUETA PEQUEÑA", style = MaterialTheme.typography.labelSmall, color = colores.onSurfaceVariant)
        Text("Texto en negritas", fontWeight = FontWeight.Bold)
        Text("Texto en cursiva", fontStyle = FontStyle.Italic)
        Text("Texto con el color principal", color = colores.primary, fontWeight = FontWeight.Bold)
        Text(
            buildAnnotatedString {
                append("Un mismo texto puede combinar ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("negritas") }
                append(", ")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("cursivas") }
                append(", ")
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append("subrayado") }
                append(" y ")
                withStyle(SpanStyle(color = colores.primary)) { append("color") }
                append(".")
            }
        )
        HorizontalDivider()
        Text(
            "Este texto usa el tamaño elegido en la Sección 3 (${vm.tamanoTexto.roundToInt()} sp).",
            fontSize = vm.tamanoTexto.sp,
            color = colores.tertiary,
        )
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Mostrar en mayúsculas", Modifier.weight(1f))
            Switch(checked = mayusculas, onCheckedChange = { mayusculas = it })
        }
    }
}

@Composable
private fun Imagenes() {
    val escalas = listOf(
        "Crop" to ContentScale.Crop,
        "Fit" to ContentScale.Fit,
        "Inside" to ContentScale.Inside,
        "FillBounds" to ContentScale.FillBounds,
    )
    var elegida by rememberSaveable { mutableIntStateOf(0) }
    var semilla by rememberSaveable { mutableIntStateOf(1015) }
    val escala = escalas[elegida].second
    val contexto = LocalContext.current
    DemoCard(
        "Imagen local y desde URL", "Image(painterResource) / AsyncImage (Coil) + ContentScale",
        "Muestra una imagen incluida en la app y otra descargada de internet. El modo de escalado define cómo se ajusta la imagen a su contenedor.",
    ) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(Espacio.s)) {
            escalas.forEachIndexed { i, (nombre, _) ->
                FilterChip(selected = elegida == i, onClick = { elegida = i }, label = { Text(nombre) })
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.s)) {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.paisaje),
                    contentDescription = "Paisaje de montañas incluido en la app",
                    contentScale = escala,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )
                Text("Local (drawable)", style = MaterialTheme.typography.labelMedium)
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = ImageRequest.Builder(contexto)
                        .data("https://picsum.photos/seed/$semilla/600/400")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Fotografía descargada de internet",
                    placeholder = rememberVectorPainter(Icons.Filled.Image),
                    error = rememberVectorPainter(Icons.Filled.Image),
                    contentScale = escala,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )
                Text("Desde URL", style = MaterialTheme.typography.labelMedium)
            }
        }
        FilledTonalButton(onClick = { semilla++ }) {
            Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text("Cargar otra imagen")
        }
    }
}

@Composable
private fun Progreso() {
    var progreso by rememberSaveable { mutableIntStateOf(40) }
    var indeterminados by rememberSaveable { mutableStateOf(true) }
    var simulando by remember { mutableStateOf(false) }
    val animado by animateFloatAsState(progreso / 100f, label = "progreso")
    val alcance = rememberCoroutineScope()
    DemoCard(
        "Indicadores de progreso", "LinearProgressIndicator / CircularProgressIndicator",
        "El modo determinado muestra cuánto falta para terminar; el indeterminado indica que hay trabajo en curso de duración desconocida.",
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(progress = { animado }, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(Espacio.m))
            CircularProgressIndicator(progress = { animado })
        }
        Resultado("Progreso: $progreso %")
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.s)) {
            OutlinedButton(onClick = { progreso = (progreso - 10).coerceAtLeast(0) }) { Text("−10") }
            OutlinedButton(onClick = { progreso = (progreso + 10).coerceAtMost(100) }) { Text("+10") }
            Button(enabled = !simulando, onClick = {
                alcance.launch {
                    simulando = true
                    progreso = 0
                    while (progreso < 100) {
                        delay(150)
                        progreso += 5
                    }
                    simulando = false
                }
            }) { Text("Simular") }
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Mostrar indicadores indeterminados", Modifier.weight(1f))
            Switch(checked = indeterminados, onCheckedChange = { indeterminados = it })
        }
        if (indeterminados) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(Modifier.weight(1f))
                Spacer(Modifier.width(Espacio.m))
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun Mensajes() {
    val contexto = LocalContext.current
    val snackbar = LocalSnackbar.current
    val alcance = rememberCoroutineScope()
    var mensaje by rememberSaveable { mutableStateOf("") }
    DemoCard(
        "Toast y Snackbar", "Toast (API de Android) / SnackbarHost + SnackbarHostState",
        "El toast es un aviso breve que desaparece solo. El snackbar aparece en la parte inferior y puede incluir una acción, como deshacer.",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.s)) {
            FilledTonalButton(onClick = {
                Toast.makeText(contexto, "Esto es un toast", Toast.LENGTH_SHORT).show()
            }) { Text("Mostrar toast") }
            FilledTonalButton(onClick = {
                mensaje = "Se archivó el mensaje"
                alcance.launch {
                    val r = snackbar.showSnackbar(
                        "Se archivó el mensaje", actionLabel = "Deshacer", duration = SnackbarDuration.Long,
                    )
                    if (r == SnackbarResult.ActionPerformed) mensaje = "Pulsaste «Deshacer»: el mensaje se restauró"
                }
            }) { Text("Mostrar snackbar") }
        }
        if (mensaje.isNotEmpty()) Resultado(mensaje)
    }
}

@Composable
private fun Dialogo() {
    var abierto by remember { mutableStateOf(false) }
    var mensaje by rememberSaveable { mutableStateOf("") }
    DemoCard(
        "Diálogo de confirmación", "AlertDialog",
        "Interrumpe al usuario para confirmar una acción importante o irreversible antes de ejecutarla.",
    ) {
        Button(onClick = { abierto = true }) {
            Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text("Eliminar archivo")
        }
        if (mensaje.isNotEmpty()) Resultado(mensaje)
    }
    if (abierto) {
        AlertDialog(
            onDismissRequest = { abierto = false },
            icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
            title = { Text("¿Eliminar archivo?") },
            text = { Text("El archivo «reporte.pdf» se eliminará de forma permanente.") },
            confirmButton = {
                TextButton(onClick = { abierto = false; mensaje = "Confirmaste: archivo eliminado" }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { abierto = false; mensaje = "Cancelaste la eliminación" }) { Text("Cancelar") }
            },
        )
    }
}

private data class Opcion(val nombre: String, val icono: ImageVector)

@Composable
private fun HojaInferior() {
    var abierta by remember { mutableStateOf(false) }
    var mensaje by rememberSaveable { mutableStateOf("") }
    val estado = rememberModalBottomSheetState()
    val alcance = rememberCoroutineScope()
    val opciones = listOf(
        Opcion("Compartir", Icons.Filled.Share),
        Opcion("Editar", Icons.Filled.Edit),
        Opcion("Eliminar", Icons.Filled.Delete),
    )
    DemoCard(
        "Hoja inferior (bottom sheet)", "ModalBottomSheet",
        "Panel que sube desde la parte inferior con opciones o contenido adicional, sin salir de la pantalla actual.",
    ) {
        Button(onClick = { abierta = true }) { Text("Abrir hoja inferior") }
        if (mensaje.isNotEmpty()) Resultado(mensaje)
    }
    if (abierta) {
        ModalBottomSheet(onDismissRequest = { abierta = false }, sheetState = estado) {
            Column(Modifier.navigationBarsPadding().padding(bottom = Espacio.l)) {
                Text(
                    "¿Qué deseas hacer?",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = Espacio.l, vertical = Espacio.s),
                )
                opciones.forEach { o ->
                    ListItem(
                        headlineContent = { Text(o.nombre) },
                        leadingContent = { Icon(o.icono, contentDescription = null) },
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                        modifier = Modifier.clickable {
                            mensaje = "Elegiste en la hoja: ${o.nombre}"
                            alcance.launch { estado.hide() }.invokeOnCompletion { abierta = false }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaSeparadorBadge() {
    var marcada by rememberSaveable { mutableStateOf(false) }
    var notificaciones by rememberSaveable { mutableIntStateOf(0) }
    DemoCard(
        "Tarjeta, separador y badge", "ElevatedCard / HorizontalDivider / BadgedBox + Badge",
        "La tarjeta agrupa información relacionada, el separador divide contenido y el badge muestra un contador sobre un ícono.",
    ) {
        ElevatedCard(
            onClick = { marcada = !marcada },
            modifier = Modifier.fillMaxWidth(),
            colors = if (marcada) CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ) else CardDefaults.elevatedCardColors(),
        ) {
            Column(Modifier.padding(Espacio.m)) {
                Text("Tarjeta seleccionable", style = MaterialTheme.typography.titleMedium)
                HorizontalDivider(Modifier.padding(vertical = Espacio.s))
                Text(
                    if (marcada) "Tarjeta marcada ✔" else "Toca la tarjeta para marcarla o desmarcarla.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        HorizontalDivider(Modifier.padding(vertical = Espacio.s))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Espacio.m)) {
            BadgedBox(badge = {
                if (notificaciones > 0) Badge { Text(if (notificaciones > 99) "99+" else "$notificaciones") }
            }) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones", modifier = Modifier.size(32.dp))
            }
            FilledTonalButton(onClick = { notificaciones++ }) { Text("Nueva notificación") }
            TextButton(onClick = { notificaciones = 0 }) { Text("Limpiar") }
        }
        Resultado("Notificaciones pendientes: $notificaciones")
    }
}
