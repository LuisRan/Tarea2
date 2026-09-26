import 'package:flutter/material.dart';

/// Espaciado consistente en toda la app.
class Espacio {
  static const double xs = 4;
  static const double s = 8;
  static const double m = 16;
  static const double l = 24;
}

/// Destinos de navegación: la pantalla principal y las seis secciones.
class Destino {
  const Destino(this.ruta, this.titulo, this.descripcion, this.icono);

  final String ruta;
  final String titulo;
  final String descripcion;
  final IconData icono;
}

const inicio = Destino('/', 'Inicio', 'Pantalla principal', Icons.home);
const secciones = <Destino>[
  Destino('/entrada', '1. Entrada de texto',
      'Campos de texto, validación, contraseñas, teclados, sugerencias y búsqueda.', Icons.edit),
  Destino('/botones', '2. Botones y acciones',
      'Botones rellenos, con contorno, con ícono, flotantes, segmentados y sus estados.', Icons.touch_app),
  Destino('/seleccion', '3. Elementos de selección',
      'Casillas, opciones, interruptores, deslizadores, listas desplegables, fecha, hora y chips.',
      Icons.check_box),
  Destino('/listas', '4. Listas y colecciones',
      'Listas, cuadrículas, encabezados, deslizar para eliminar, actualizar y pestañas.', Icons.list),
  Destino('/informacion', '5. Información y retroalimentación',
      'Estilos de texto, imágenes, progreso, mensajes, diálogos, hojas inferiores y badges.', Icons.info),
  Destino('/contenedores', '6. Contenedores y estructura',
      'Filas, columnas, capas, desplazamiento, barras de navegación y pesos.', Icons.dashboard),
];

/// Navega a un destino. Inicio limpia la pila; las secciones se apilan sobre Inicio.
void irA(BuildContext context, Destino destino) {
  final navegador = Navigator.of(context);
  if (destino.ruta == inicio.ruta) {
    navegador.popUntil(ModalRoute.withName(inicio.ruta));
  } else {
    navegador.pushNamedAndRemoveUntil(destino.ruta, ModalRoute.withName(inicio.ruta));
  }
}

/// Menú lateral (Drawer) con las seis secciones.
class MenuLateral extends StatelessWidget {
  const MenuLateral({super.key, required this.actual});

  final String actual;

  @override
  Widget build(BuildContext context) {
    final todos = [inicio, ...secciones];
    final indice = todos.indexWhere((d) => d.ruta == actual);
    final esquema = Theme.of(context).colorScheme;
    return NavigationDrawer(
      selectedIndex: indice < 0 ? 0 : indice,
      onDestinationSelected: (i) {
        Navigator.of(context).pop(); // Cierra el menú.
        irA(context, todos[i]);
      },
      children: [
        Padding(
          padding: const EdgeInsets.all(Espacio.l),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              CircleAvatar(
                radius: 24,
                backgroundColor: esquema.primaryContainer,
                child: Icon(Icons.dashboard, color: esquema.onPrimaryContainer),
              ),
              const SizedBox(height: Espacio.m),
              Text('Catálogo UI · Flutter', style: Theme.of(context).textTheme.titleLarge),
              Text('Catálogo de elementos de interfaz',
                  style: Theme.of(context).textTheme.bodyMedium?.copyWith(color: esquema.onSurfaceVariant)),
            ],
          ),
        ),
        const Divider(indent: Espacio.m, endIndent: Espacio.m),
        for (final d in todos)
          NavigationDrawerDestination(icon: Icon(d.icono), label: Text(d.titulo)),
      ],
    );
  }
}

/// Estructura común de cada sección: barra superior, menú lateral y lista desplazable de tarjetas.
class PantallaSeccion extends StatelessWidget {
  const PantallaSeccion({
    super.key,
    required this.destino,
    required this.intro,
    required this.hijos,
  });

  final Destino destino;
  final String intro;
  final List<Widget> hijos;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: const BackButton(),
        title: Text(destino.titulo),
        backgroundColor: Theme.of(context).colorScheme.surfaceContainer,
        actions: [
          IconButton(
            tooltip: 'Ir a la pantalla principal',
            icon: const Icon(Icons.home),
            onPressed: () => irA(context, inicio),
          ),
        ],
      ),
      drawer: MenuLateral(actual: destino.ruta),
      body: ListView(
        padding: const EdgeInsets.all(Espacio.m),
        children: [
          Text(intro, style: Theme.of(context).textTheme.bodyLarge),
          for (final hijo in hijos) ...[const SizedBox(height: Espacio.m), hijo],
          const SizedBox(height: Espacio.l),
        ],
      ),
    );
  }
}

/// Tarjeta de documentación: nombre, componente, explicación y demostración.
class DemoCard extends StatelessWidget {
  const DemoCard({
    super.key,
    required this.titulo,
    required this.componente,
    required this.descripcion,
    required this.children,
  });

  final String titulo;
  final String componente;
  final String descripcion;
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return Card.outlined(
      margin: EdgeInsets.zero,
      color: tema.colorScheme.surfaceContainerLow,
      child: Padding(
        padding: const EdgeInsets.all(Espacio.m),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(titulo, style: tema.textTheme.titleMedium),
            const SizedBox(height: Espacio.xs),
            Text(componente, style: tema.textTheme.labelMedium?.copyWith(color: tema.colorScheme.primary)),
            const SizedBox(height: Espacio.xs),
            Text(descripcion,
                style: tema.textTheme.bodyMedium?.copyWith(color: tema.colorScheme.onSurfaceVariant)),
            const SizedBox(height: Espacio.m),
            for (var i = 0; i < children.length; i++) ...[
              if (i > 0) const SizedBox(height: Espacio.s),
              children[i],
            ],
          ],
        ),
      ),
    );
  }
}

/// Texto que muestra la respuesta de una demostración.
class Resultado extends StatelessWidget {
  const Resultado(this.texto, {super.key, this.color});

  final String texto;
  final Color? color;

  @override
  Widget build(BuildContext context) {
    return Text(
      texto,
      style: Theme.of(context).textTheme.bodyMedium?.copyWith(
            color: color ?? Theme.of(context).colorScheme.primary,
          ),
    );
  }
}

/// Flutter no incluye un «toast» nativo: se implementa con un OverlayEntry temporal.
void mostrarToast(BuildContext context, String mensaje) {
  final overlay = Overlay.of(context);
  final entrada = OverlayEntry(
    builder: (context) => Positioned(
      left: 0,
      right: 0,
      bottom: 96,
      child: IgnorePointer(
        child: Center(
          child: TweenAnimationBuilder<double>(
            tween: Tween(begin: 0, end: 1),
            duration: const Duration(milliseconds: 200),
            builder: (context, valor, hijo) => Opacity(opacity: valor, child: hijo),
            child: Material(
              color: const Color(0xDD323232),
              borderRadius: BorderRadius.circular(24),
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                child: Text(mensaje, style: const TextStyle(color: Colors.white)),
              ),
            ),
          ),
        ),
      ),
    ),
  );
  overlay.insert(entrada);
  Future.delayed(const Duration(seconds: 2), entrada.remove);
}
