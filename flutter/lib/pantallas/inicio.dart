import 'package:flutter/material.dart';

import '../comunes.dart';
import '../estado.dart';

/// Pantalla principal con una tarjeta por sección.
class InicioPantalla extends StatelessWidget {
  const InicioPantalla({super.key});

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    final estado = EstadoScope.of(context);
    return Scaffold(
      appBar: AppBar(
        title: const Text('Catálogo UI · Flutter'),
        backgroundColor: tema.colorScheme.surfaceContainer,
      ),
      drawer: MenuLateral(actual: inicio.ruta),
      body: ListView(
        padding: const EdgeInsets.all(Espacio.m),
        children: [
          Text('Catálogo de elementos de interfaz', style: tema.textTheme.headlineSmall),
          const SizedBox(height: Espacio.s),
          Text(
            'Explora los componentes básicos de una interfaz móvil construidos con Flutter. '
            'Cada sección muestra el nombre de cada elemento, para qué sirve y una demostración '
            'con la que puedes interactuar.',
            style: tema.textTheme.bodyMedium?.copyWith(color: tema.colorScheme.onSurfaceVariant),
          ),
          const SizedBox(height: Espacio.m),
          for (final d in secciones)
            Card.filled(
              margin: const EdgeInsets.only(bottom: Espacio.s),
              clipBehavior: Clip.antiAlias,
              child: ListTile(
                contentPadding: const EdgeInsets.symmetric(horizontal: Espacio.m, vertical: Espacio.xs),
                leading: CircleAvatar(
                  backgroundColor: tema.colorScheme.primaryContainer,
                  child: Icon(d.icono, color: tema.colorScheme.onPrimaryContainer),
                ),
                title: Text(d.titulo, style: tema.textTheme.titleMedium),
                subtitle: Text(d.descripcion),
                trailing: const Icon(Icons.chevron_right),
                onTap: () => irA(context, d),
              ),
            ),
          const SizedBox(height: Espacio.s),
          Card.outlined(
            margin: EdgeInsets.zero,
            child: Padding(
              padding: const EdgeInsets.all(Espacio.m),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Conexión entre secciones: lo que agregues en la Sección 1 aparece en la lista de la '
                    'Sección 4, y el tamaño de texto elegido en la Sección 3 cambia el texto de muestra '
                    'de la Sección 5. El tema claro u oscuro sigue la configuración del sistema.',
                    style: tema.textTheme.bodyMedium,
                  ),
                  const SizedBox(height: Espacio.s),
                  Resultado('La lista compartida tiene ${estado.elementos.length} elementos'),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
