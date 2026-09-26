@file:OptIn(ExperimentalMaterial3Api::class)

package mx.ipn.catalogo.compose.ui.secciones

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.launch
import mx.ipn.catalogo.compose.CatalogoViewModel
import mx.ipn.catalogo.compose.ui.componentes.DemoCard
import mx.ipn.catalogo.compose.ui.componentes.LocalSnackbar
import mx.ipn.catalogo.compose.ui.componentes.PantallaSeccion
import mx.ipn.catalogo.compose.ui.componentes.Resultado

private val estados = listOf(
    "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas", "Chihuahua",
    "Ciudad de México", "Coahuila", "Colima", "Durango", "Estado de México", "Guanajuato", "Guerrero",
    "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla",
    "Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas",
    "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas",
)

/** Sección 1: Entrada de texto. */
@Composable
fun EntradaPantalla(vm: CatalogoViewModel) {
    PantallaSeccion("Los campos de texto permiten al usuario capturar información. Prueba cada uno y observa la respuesta debajo.") {
        CampoSimple()
        CampoValidado()
        CampoContrasena()
        CamposTeclado()
        CampoMultilinea()
        CampoSugerencias()
        BarraBusqueda(vm)
        AgregarALista(vm)
    }
}

@Composable
private fun CampoSimple() {
    var nombre by rememberSaveable { mutableStateOf("") }
    DemoCard(
        "Campo de texto simple", "OutlinedTextField (label)",
        "Captura texto libre de una sola línea. La etiqueta flotante indica qué dato se espera y sube cuando el campo recibe el foco.",
    ) {
        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it },
            label = { Text("Tu nombre") }, singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth(),
        )
        Resultado(if (nombre.isBlank()) "Escribe tu nombre para saludarte." else "¡Hola, ${nombre.trim()}!")
    }
}

@Composable
private fun CampoValidado() {
    var cp by rememberSaveable { mutableStateOf("") }
    val error = cp.isNotEmpty() && cp.length != 5
    DemoCard(
        "Campo con validación", "OutlinedTextField (isError, supportingText)",
        "Comprueba el dato mientras se escribe y muestra un mensaje de error visible cuando no cumple la regla. Aquí se exige un código postal de 5 dígitos.",
    ) {
        OutlinedTextField(
            value = cp,
            onValueChange = { nuevo -> cp = nuevo.filter { it.isDigit() }.take(5) },
            label = { Text("Código postal") },
            isError = error,
            supportingText = {
                Text(
                    when {
                        error -> "El código postal debe tener exactamente 5 dígitos"
                        cp.length == 5 -> "Código postal válido ✔"
                        else -> "Debe tener 5 dígitos · ${cp.length}/5"
                    }
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CampoContrasena() {
    var clave by rememberSaveable { mutableStateOf("") }
    var visible by rememberSaveable { mutableStateOf(false) }
    val puntos = listOf(
        clave.length >= 8, clave.any { it.isDigit() }, clave.any { it.isUpperCase() },
        clave.any { !it.isLetterOrDigit() },
    ).count { it }
    DemoCard(
        "Campo de contraseña", "OutlinedTextField + PasswordVisualTransformation",
        "Oculta los caracteres para proteger datos sensibles. El ícono del ojo permite mostrar u ocultar el contenido.",
    ) {
        OutlinedTextField(
            value = clave, onValueChange = { clave = it },
            label = { Text("Contraseña") }, singleLine = true,
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Resultado(
            when {
                clave.isEmpty() -> "Seguridad: escribe una contraseña"
                puntos <= 1 -> "Seguridad: débil"
                puntos <= 3 -> "Seguridad: media"
                else -> "Seguridad: fuerte"
            }
        )
    }
}

@Composable
private fun CamposTeclado() {
    var edad by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    val correoInvalido = correo.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    DemoCard(
        "Tipos de teclado", "KeyboardOptions(keyboardType = …)",
        "KeyboardType indica qué teclado mostrar: numérico, de correo (con @) o telefónico. Así se facilita la captura y se reducen errores.",
    ) {
        OutlinedTextField(
            value = edad, onValueChange = { edad = it.filter(Char::isDigit).take(3) },
            label = { Text("Edad (teclado numérico)") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = correo, onValueChange = { correo = it },
            label = { Text("Correo electrónico") }, singleLine = true, isError = correoInvalido,
            supportingText = if (correoInvalido) ({ Text("Formato de correo no válido") }) else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = telefono, onValueChange = { telefono = it },
            label = { Text("Teléfono") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CampoMultilinea() {
    var notas by rememberSaveable { mutableStateOf("") }
    val lineas = if (notas.isEmpty()) 0 else notas.count { it == '\n' } + 1
    val palabras = notas.split(Regex("\\s+")).count { it.isNotBlank() }
    DemoCard(
        "Campo multilínea", "OutlinedTextField (minLines / maxLines)",
        "Admite varias líneas para textos largos como comentarios o notas. Crece conforme se escribe y muestra un contador de caracteres.",
    ) {
        OutlinedTextField(
            value = notas, onValueChange = { notas = it.take(200) },
            label = { Text("Comentarios") },
            minLines = 3, maxLines = 6,
            supportingText = { Text("${notas.length}/200") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            modifier = Modifier.fillMaxWidth(),
        )
        Resultado("Líneas: $lineas · Palabras: $palabras")
    }
}

@Composable
private fun CampoSugerencias() {
    var texto by rememberSaveable { mutableStateOf("") }
    var elegido by rememberSaveable { mutableStateOf<String?>(null) }
    val sugerencias = if (texto.isBlank() || texto == elegido) emptyList()
    else estados.filter { it.contains(texto.trim(), ignoreCase = true) }.take(5)
    DemoCard(
        "Campo con sugerencias automáticas", "OutlinedTextField + lista filtrada",
        "Propone opciones mientras el usuario escribe, reduciendo errores de captura. Escribe las primeras letras de un estado de la República.",
    ) {
        OutlinedTextField(
            value = texto, onValueChange = { texto = it },
            label = { Text("Estado de la República") }, singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Place, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
        )
        if (sugerencias.isNotEmpty()) {
            OutlinedCard(Modifier.fillMaxWidth()) {
                Column {
                    sugerencias.forEachIndexed { i, estado ->
                        if (i > 0) HorizontalDivider()
                        ListItem(
                            headlineContent = { Text(estado) },
                            colors = ListItemDefaults.colors(),
                            modifier = Modifier.clickable {
                                texto = estado
                                elegido = estado
                            },
                        )
                    }
                }
            }
        }
        Resultado(elegido?.let { "Seleccionaste: $it" } ?: "Aún no eliges un estado.")
    }
}

@Composable
private fun BarraBusqueda(vm: CatalogoViewModel) {
    var consulta by rememberSaveable { mutableStateOf("") }
    var expandida by rememberSaveable { mutableStateOf(false) }
    val resultados = if (consulta.isBlank()) emptyList()
    else vm.elementos.filter { it.nombre.contains(consulta.trim(), ignoreCase = true) }
    DemoCard(
        "Barra de búsqueda", "DockedSearchBar + SearchBarDefaults.InputField",
        "Campo especializado para filtrar contenido. Aquí busca dentro de los elementos de la lista de la Sección 4.",
    ) {
        DockedSearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = consulta,
                    onQueryChange = { consulta = it; expandida = it.isNotBlank() },
                    onSearch = { expandida = false },
                    expanded = expandida,
                    onExpandedChange = { expandida = it },
                    placeholder = { Text("Buscar frutas o verduras…") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        if (consulta.isNotEmpty()) {
                            IconButton(onClick = { consulta = ""; expandida = false }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Borrar búsqueda")
                            }
                        }
                    },
                )
            },
            expanded = expandida,
            onExpandedChange = { expandida = it },
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (resultados.isEmpty()) {
                ListItem(headlineContent = { Text("Sin resultados para «${consulta.trim()}»") })
            } else {
                resultados.take(6).forEach { e ->
                    ListItem(
                        headlineContent = { Text(e.nombre) },
                        supportingContent = { Text(e.categoria) },
                        modifier = Modifier.clickable { consulta = e.nombre; expandida = false },
                    )
                }
            }
        }
        if (consulta.isNotBlank()) {
            Resultado(
                if (resultados.isEmpty()) "Sin resultados para «${consulta.trim()}»"
                else "${resultados.size} resultado(s): ${resultados.joinToString(", ") { it.nombre }}"
            )
        }
    }
}

@Composable
private fun AgregarALista(vm: CatalogoViewModel) {
    var nuevo by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    val snackbar = LocalSnackbar.current
    val alcance = rememberCoroutineScope()
    val agregar: () -> Unit = {
        if (nuevo.isBlank()) {
            error = true
        } else {
            val nombre = nuevo.trim()
            vm.agregar(nombre)
            nuevo = ""
            error = false
            alcance.launch { snackbar.showSnackbar("«$nombre» se agregó a la Sección 4") }
        }
    }
    DemoCard(
        "Conexión con la Sección 4", "OutlinedTextField + Button + ViewModel compartido",
        "El texto capturado aquí se agrega a la lista compartida que se muestra en la Sección 4 (Listas y colecciones).",
    ) {
        OutlinedTextField(
            value = nuevo, onValueChange = { nuevo = it; error = false },
            label = { Text("Nuevo elemento") }, singleLine = true, isError = error,
            supportingText = if (error) ({ Text("Escribe un nombre antes de agregar") }) else null,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { agregar() }),
            modifier = Modifier.fillMaxWidth(),
        )
        Button(onClick = agregar) {
            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text("Agregar a la lista")
        }
        Resultado("La lista compartida tiene ${vm.elementos.size} elementos")
    }
}
