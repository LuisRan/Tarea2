package mx.ipn.catalogo.compose.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Permite a cualquier pantalla mostrar un Snackbar en el Scaffold principal. */
val LocalSnackbar = staticCompositionLocalOf<SnackbarHostState> { error("Sin SnackbarHostState") }

/** Espaciado consistente usado en toda la app. */
object Espacio {
    val xs = 4.dp
    val s = 8.dp
    val m = 16.dp
    val l = 24.dp
}

/**
 * Tarjeta de documentación: nombre del elemento, componente utilizado,
 * explicación breve y, debajo, la demostración interactiva.
 */
@Composable
fun DemoCard(
    titulo: String,
    componente: String,
    descripcion: String,
    modifier: Modifier = Modifier,
    contenido: @Composable ColumnScope.() -> Unit,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(Modifier.padding(Espacio.m), verticalArrangement = Arrangement.spacedBy(Espacio.s)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(componente, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Text(
                descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(Espacio.xs))
            contenido()
        }
    }
}

/** Texto que muestra la respuesta de una demostración. */
@Composable
fun Resultado(texto: String, modifier: Modifier = Modifier) {
    Text(texto, modifier = modifier, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
}

/** Contenedor desplazable común para cada sección. */
@Composable
fun PantallaSeccion(intro: String, contenido: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Espacio.m),
        verticalArrangement = Arrangement.spacedBy(Espacio.m),
    ) {
        Text(intro, style = MaterialTheme.typography.bodyLarge)
        contenido()
    }
}
