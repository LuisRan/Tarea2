package mx.ipn.catalogo.compose.ui.secciones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.ipn.catalogo.compose.CatalogoViewModel
import mx.ipn.catalogo.compose.Destino
import mx.ipn.catalogo.compose.ui.componentes.Espacio
import mx.ipn.catalogo.compose.ui.componentes.Resultado

/** Pantalla principal con una tarjeta por sección. */
@Composable
fun InicioPantalla(vm: CatalogoViewModel, alAbrir: (Destino) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Espacio.m),
        verticalArrangement = Arrangement.spacedBy(Espacio.s),
    ) {
        Text("Catálogo de elementos de interfaz", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Explora los componentes básicos de una interfaz móvil construidos con Jetpack Compose. " +
                "Cada sección muestra el nombre de cada elemento, para qué sirve y una demostración " +
                "con la que puedes interactuar.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Destino.entries.filter { it != Destino.INICIO }.forEach { destino ->
            Card(onClick = { alAbrir(destino) }, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(Espacio.m), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                        Icon(
                            destino.icono, contentDescription = null,
                            modifier = Modifier.padding(10.dp).size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(start = Espacio.m),
                    ) {
                        Text(destino.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(
                            destino.descripcion,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
        }
        OutlinedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(Espacio.m)) {
                Text(
                    "Conexión entre secciones: lo que agregues en la Sección 1 aparece en la lista de la " +
                        "Sección 4, y el tamaño de texto elegido en la Sección 3 cambia el texto de muestra " +
                        "de la Sección 5. El tema claro u oscuro sigue la configuración del sistema.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Resultado("La lista compartida tiene ${vm.elementos.size} elementos")
            }
        }
    }
}
