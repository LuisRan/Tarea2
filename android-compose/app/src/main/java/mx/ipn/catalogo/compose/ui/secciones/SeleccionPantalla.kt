@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package mx.ipn.catalogo.compose.ui.secciones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import mx.ipn.catalogo.compose.CatalogoViewModel
import mx.ipn.catalogo.compose.ui.componentes.DemoCard
import mx.ipn.catalogo.compose.ui.componentes.Espacio
import mx.ipn.catalogo.compose.ui.componentes.PantallaSeccion
import mx.ipn.catalogo.compose.ui.componentes.Resultado
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

/** Sección 3: Elementos de selección. */
@Composable
fun SeleccionPantalla(vm: CatalogoViewModel) {
    PantallaSeccion("Los elementos de selección permiten elegir entre opciones sin escribir.") {
        Casillas()
        Opciones()
        Interruptor()
        Deslizadores(vm)
        Desplegable()
        FechaYHora()
        Chips()
    }
}

@Composable
private fun Casillas() {
    val nombres = listOf("Queso", "Jamón", "Piña")
    val marcados = remember { mutableStateListOf(false, true, false) }
    val estadoPadre = when (marcados.count { it }) {
        0 -> ToggleableState.Off
        nombres.size -> ToggleableState.On
        else -> ToggleableState.Indeterminate
    }
    DemoCard(
        "Casillas de verificación", "Checkbox / TriStateCheckbox",
        "Permiten marcar varias opciones independientes. La casilla principal muestra un estado indeterminado cuando solo algunas opciones están marcadas.",
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .triStateToggleable(state = estadoPadre, role = Role.Checkbox, onClick = {
                    val nuevo = estadoPadre != ToggleableState.On
                    for (i in marcados.indices) marcados[i] = nuevo
                }),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TriStateCheckbox(state = estadoPadre, onClick = null)
            Text("Todos los ingredientes")
        }
        nombres.forEachIndexed { i, nombre ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp)
                    .toggleable(value = marcados[i], role = Role.Checkbox, onValueChange = { marcados[i] = it }),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = marcados[i], onCheckedChange = null)
                Text(nombre)
            }
        }
        val elegidos = nombres.filterIndexed { i, _ -> marcados[i] }
        Resultado("Seleccionados: ${if (elegidos.isEmpty()) "ninguno" else elegidos.joinToString(", ")}")
    }
}

@Composable
private fun Opciones() {
    val opciones = listOf("Envío estándar", "Envío exprés", "Recoger en tienda")
    var elegida by rememberSaveable { mutableStateOf(opciones[0]) }
    DemoCard(
        "Botones de opción", "RadioButton + Modifier.selectableGroup",
        "Grupo de opciones mutuamente excluyentes: al elegir una se desmarca la anterior. Útil cuando solo puede haber una respuesta.",
    ) {
        Column(Modifier.selectableGroup()) {
            opciones.forEach { opcion ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(selected = elegida == opcion, onClick = { elegida = opcion }, role = Role.RadioButton),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = elegida == opcion, onClick = null)
                    Text(opcion, Modifier.padding(start = Espacio.s))
                }
            }
        }
        Resultado("Opción elegida: $elegida")
    }
}

@Composable
private fun Interruptor() {
    var activo by rememberSaveable { mutableStateOf(false) }
    DemoCard(
        "Interruptor (switch)", "Switch",
        "Activa o desactiva una opción de forma inmediata, como una preferencia de configuración.",
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Recibir notificaciones", Modifier.weight(1f))
            Switch(checked = activo, onCheckedChange = { activo = it })
        }
        Resultado(if (activo) "Notificaciones activadas 🔔" else "Notificaciones desactivadas")
    }
}

@Composable
private fun Deslizadores(vm: CatalogoViewModel) {
    var rango by remember { mutableStateOf(200f..800f) }
    DemoCard(
        "Deslizador de valor único y de rango", "Slider / RangeSlider",
        "El deslizador elige un valor dentro de un intervalo; el de rango elige un mínimo y un máximo. El primero define el tamaño de texto que se usa en la Sección 5.",
    ) {
        Slider(
            value = vm.tamanoTexto,
            onValueChange = { vm.cambiarTamanoTexto(it.roundToInt().toFloat()) },
            valueRange = 12f..32f,
            steps = 9,
        )
        Resultado("Tamaño de texto para la Sección 5: ${vm.tamanoTexto.roundToInt()} sp")
        RangeSlider(
            value = rango,
            onValueChange = { rango = it },
            valueRange = 0f..1000f,
            steps = 19,
        )
        Resultado("Precio entre $${rango.start.roundToInt()} y $${rango.endInclusive.roundToInt()}")
    }
}

@Composable
private fun Desplegable() {
    val paises = listOf("México", "Argentina", "Chile", "Colombia", "España", "Perú", "Uruguay")
    var abierto by remember { mutableStateOf(false) }
    var pais by rememberSaveable { mutableStateOf("") }
    DemoCard(
        "Lista desplegable", "ExposedDropdownMenuBox + ExposedDropdownMenu",
        "Muestra una lista de opciones al tocarla y conserva la elegida. Ocupa poco espacio cuando hay muchas opciones.",
    ) {
        ExposedDropdownMenuBox(expanded = abierto, onExpandedChange = { abierto = it }) {
            OutlinedTextField(
                value = pais,
                onValueChange = {},
                readOnly = true,
                label = { Text("País") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = abierto) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
                paises.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = { pais = opcion; abierto = false },
                    )
                }
            }
        }
        Resultado("País elegido: ${pais.ifEmpty { "—" }}")
    }
}

@Composable
private fun FechaYHora() {
    var mostrarFecha by remember { mutableStateOf(false) }
    var mostrarHora by remember { mutableStateOf(false) }
    var fecha by rememberSaveable { mutableStateOf<String?>(null) }
    var hora by rememberSaveable { mutableStateOf<String?>(null) }
    val estadoFecha = rememberDatePickerState()
    val estadoHora = rememberTimePickerState(initialHour = 12, initialMinute = 0, is24Hour = true)

    DemoCard(
        "Selector de fecha y de hora", "DatePickerDialog / TimePicker",
        "Abren diálogos con un calendario o un reloj para elegir fechas y horas válidas sin escribirlas a mano.",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.s)) {
            OutlinedButton(onClick = { mostrarFecha = true }) {
                Icon(Icons.Filled.CalendarMonth, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Elegir fecha")
            }
            OutlinedButton(onClick = { mostrarHora = true }) {
                Icon(Icons.Filled.Schedule, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Elegir hora")
            }
        }
        Resultado("Fecha: ${fecha ?: "sin elegir"}")
        Resultado("Hora: ${hora ?: "sin elegir"}")
    }

    if (mostrarFecha) {
        DatePickerDialog(
            onDismissRequest = { mostrarFecha = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoFecha.selectedDateMillis?.let { millis ->
                        val formato = SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", Locale("es", "MX"))
                        formato.timeZone = TimeZone.getTimeZone("UTC")
                        fecha = formato.format(Date(millis))
                    }
                    mostrarFecha = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { mostrarFecha = false }) { Text("Cancelar") } },
        ) {
            DatePicker(state = estadoFecha)
        }
    }

    if (mostrarHora) {
        AlertDialog(
            onDismissRequest = { mostrarHora = false },
            title = { Text("Selecciona una hora") },
            text = { TimePicker(state = estadoHora) },
            confirmButton = {
                TextButton(onClick = {
                    hora = String.format(Locale.getDefault(), "%02d:%02d", estadoHora.hour, estadoHora.minute)
                    mostrarHora = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { mostrarHora = false }) { Text("Cancelar") } },
        )
    }
}

@Composable
private fun Chips() {
    val filtros = listOf("Vegano", "Sin gluten", "Orgánico", "Local", "De temporada")
    val activos = remember { mutableStateListOf<String>() }
    DemoCard(
        "Chips de filtro", "FilterChip + FlowRow",
        "Etiquetas compactas que se activan o desactivan para filtrar contenido. Se pueden combinar varias a la vez.",
    ) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(Espacio.s)) {
            filtros.forEach { filtro ->
                val seleccionado = filtro in activos
                FilterChip(
                    selected = seleccionado,
                    onClick = { if (seleccionado) activos.remove(filtro) else activos.add(filtro) },
                    label = { Text(filtro) },
                    leadingIcon = if (seleccionado) {
                        { Icon(Icons.Filled.Done, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                    } else null,
                )
            }
        }
        Resultado(
            "Filtros activos: ${if (activos.isEmpty()) "ninguno" else activos.joinToString(", ")}",
            Modifier.padding(top = Espacio.xs),
        )
    }
}
