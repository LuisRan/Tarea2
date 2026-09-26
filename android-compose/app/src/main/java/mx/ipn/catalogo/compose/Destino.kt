package mx.ipn.catalogo.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.ui.graphics.vector.ImageVector

/** Destinos de navegación: la pantalla principal y las seis secciones. */
enum class Destino(
    val ruta: String,
    val titulo: String,
    val descripcion: String,
    val icono: ImageVector,
) {
    INICIO("inicio", "Inicio", "Pantalla principal", Icons.Filled.Home),
    ENTRADA("entrada", "1. Entrada de texto",
        "Campos de texto, validación, contraseñas, teclados, sugerencias y búsqueda.", Icons.Filled.Edit),
    BOTONES("botones", "2. Botones y acciones",
        "Botones rellenos, con contorno, con ícono, flotantes, segmentados y sus estados.", Icons.Filled.TouchApp),
    SELECCION("seleccion", "3. Elementos de selección",
        "Casillas, opciones, interruptores, deslizadores, listas desplegables, fecha, hora y chips.", Icons.Filled.CheckBox),
    LISTAS("listas", "4. Listas y colecciones",
        "Listas, cuadrículas, encabezados, deslizar para eliminar, actualizar y pestañas.", Icons.AutoMirrored.Filled.List),
    INFORMACION("informacion", "5. Información y retroalimentación",
        "Estilos de texto, imágenes, progreso, mensajes, diálogos, hojas inferiores y badges.", Icons.Filled.Info),
    CONTENEDORES("contenedores", "6. Contenedores y estructura",
        "Filas, columnas, capas, desplazamiento, barras de navegación y pesos.", Icons.Filled.Dashboard);

    companion object {
        fun porRuta(ruta: String?): Destino = entries.firstOrNull { it.ruta == ruta } ?: INICIO
    }
}
