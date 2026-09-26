import 'package:flutter/material.dart';

import '../comunes.dart';

/// Sección 6: Contenedores y estructura.
class ContenedoresPantalla extends StatelessWidget {
  const ContenedoresPantalla({super.key});

  @override
  Widget build(BuildContext context) {
    return PantallaSeccion(
      destino: secciones[5],
      intro: 'Los contenedores organizan a otros elementos en la pantalla.',
      hijos: const [
        _FilaYColumna(),
        _Superpuesta(),
        _Desplazable(),
        _BarraSuperior(),
        _NavegacionInferior(),
        _PesosYAlineacion(),
      ],
    );
  }
}

/// Caja de color reutilizada en las demostraciones de distribución.
class _Caja extends StatelessWidget {
  const _Caja(this.texto, {this.fondo, this.tamano = 56});

  final String texto;
  final Color? fondo;
  final double? tamano;

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    return Container(
      width: tamano,
      height: tamano,
      margin: const EdgeInsets.all(Espacio.xs),
      alignment: Alignment.center,
      decoration: BoxDecoration(
        color: fondo ?? esquema.primaryContainer,
        borderRadius: BorderRadius.circular(12),
      ),
      child: Text(texto, style: TextStyle(fontWeight: FontWeight.bold, color: esquema.onPrimaryContainer)),
    );
  }
}

class _FilaYColumna extends StatefulWidget {
  const _FilaYColumna();
  @override
  State<_FilaYColumna> createState() => _FilaYColumnaState();
}

class _FilaYColumnaState extends State<_FilaYColumna> {
  bool _esFila = true;

  @override
  Widget build(BuildContext context) {
    const cajas = [_Caja('A'), _Caja('B'), _Caja('C')];
    return DemoCard(
      titulo: 'Distribución en fila y en columna',
      componente: 'Row / Column',
      descripcion: 'Row acomoda a sus hijos en horizontal y Column en vertical. Cambia la distribución con '
          'el selector.',
      children: [
        SegmentedButton<bool>(
          segments: const [
            ButtonSegment(value: true, label: Text('Fila')),
            ButtonSegment(value: false, label: Text('Columna')),
          ],
          selected: {_esFila},
          onSelectionChanged: (s) => setState(() => _esFila = s.first),
        ),
        AnimatedSize(
          duration: const Duration(milliseconds: 200),
          alignment: Alignment.topLeft,
          child: _esFila
              ? const Row(mainAxisSize: MainAxisSize.min, children: cajas)
              : const Column(mainAxisSize: MainAxisSize.min, children: cajas),
        ),
      ],
    );
  }
}

class _Superpuesta extends StatefulWidget {
  const _Superpuesta();
  @override
  State<_Superpuesta> createState() => _SuperpuestaState();
}

class _SuperpuestaState extends State<_Superpuesta> {
  static const _posiciones = [
    (Alignment.topLeft, 'arriba a la izquierda'),
    (Alignment.center, 'centro'),
    (Alignment.bottomRight, 'abajo a la derecha'),
  ];
  int _indice = 1;

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Distribución superpuesta',
      componente: 'Stack + Align',
      descripcion: 'Stack apila elementos uno encima de otro, como un texto sobre una imagen. Toca la imagen '
          'para mover la capa superior.',
      children: [
        ClipRRect(
          borderRadius: BorderRadius.circular(12),
          child: SizedBox(
            height: 160,
            child: Stack(
              fit: StackFit.expand,
              children: [
                Image.asset('assets/paisaje.png', fit: BoxFit.cover, semanticLabel: 'Paisaje de montañas'),
                AnimatedAlign(
                  duration: const Duration(milliseconds: 250),
                  alignment: _posiciones[_indice].$1,
                  child: Padding(
                    padding: const EdgeInsets.all(Espacio.s),
                    child: DecoratedBox(
                      decoration: BoxDecoration(
                        color: const Color(0xB3000000),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: const Padding(
                        padding: EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                        child: Text('Capa superior',
                            style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                      ),
                    ),
                  ),
                ),
                Material(
                  type: MaterialType.transparency,
                  child: InkWell(
                    onTap: () => setState(() => _indice = (_indice + 1) % _posiciones.length),
                  ),
                ),
              ],
            ),
          ),
        ),
        Resultado('Posición de la capa: ${_posiciones[_indice].$2}'),
      ],
    );
  }
}

class _Desplazable extends StatefulWidget {
  const _Desplazable();
  @override
  State<_Desplazable> createState() => _DesplazableState();
}

class _DesplazableState extends State<_Desplazable> {
  final _control = ScrollController();

  @override
  void dispose() {
    _control.dispose();
    super.dispose();
  }

  void _ir(double posicion) => _control.animateTo(
        posicion,
        duration: const Duration(milliseconds: 400),
        curve: Curves.easeInOut,
      );

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return DemoCard(
      titulo: 'Contenedor con desplazamiento',
      componente: 'SingleChildScrollView (ScrollController)',
      descripcion: 'Permite ver contenido más alto que el espacio disponible desplazándolo verticalmente. '
          'Desliza dentro del recuadro o usa los botones.',
      children: [
        Container(
          height: 150,
          decoration: BoxDecoration(
            border: Border.all(color: tema.colorScheme.outline),
            borderRadius: BorderRadius.circular(12),
          ),
          child: Scrollbar(
            controller: _control,
            child: SingleChildScrollView(
              controller: _control,
              padding: const EdgeInsets.all(Espacio.s),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  for (var i = 1; i <= 30; i++)
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 4),
                      child: Text('Renglón número $i del contenido desplazable'),
                    ),
                ],
              ),
            ),
          ),
        ),
        Wrap(
          spacing: Espacio.s,
          children: [
            FilledButton.tonal(
              onPressed: () => _ir(_control.position.maxScrollExtent),
              child: const Text('Ir al final'),
            ),
            TextButton(onPressed: () => _ir(0), child: const Text('Ir al inicio')),
          ],
        ),
      ],
    );
  }
}

class _BarraSuperior extends StatefulWidget {
  const _BarraSuperior();
  @override
  State<_BarraSuperior> createState() => _BarraSuperiorState();
}

class _BarraSuperiorState extends State<_BarraSuperior> {
  String _accion = '—';

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    return DemoCard(
      titulo: 'Barra superior',
      componente: 'AppBar (title, leading, actions) + PopupMenuButton',
      descripcion: 'Muestra el título de la pantalla y acciones frecuentes. La app entera usa una barra '
          'superior con menú lateral.',
      children: [
        ClipRRect(
          borderRadius: BorderRadius.circular(12),
          child: AppBar(
            primary: false,
            automaticallyImplyLeading: false,
            backgroundColor: esquema.primaryContainer,
            foregroundColor: esquema.onPrimaryContainer,
            leading: IconButton(
              tooltip: 'Menú',
              icon: const Icon(Icons.menu),
              onPressed: () => setState(() => _accion = 'Menú'),
            ),
            title: const Text('Mi bandeja'),
            actions: [
              IconButton(
                tooltip: 'Buscar',
                icon: const Icon(Icons.search),
                onPressed: () => setState(() => _accion = 'Buscar'),
              ),
              IconButton(
                tooltip: 'Compartir',
                icon: const Icon(Icons.share),
                onPressed: () => setState(() => _accion = 'Compartir'),
              ),
              PopupMenuButton<String>(
                tooltip: 'Más opciones',
                onSelected: (v) => setState(() => _accion = v),
                itemBuilder: (_) => const [PopupMenuItem(value: 'Ajustes', child: Text('Ajustes'))],
              ),
            ],
          ),
        ),
        Resultado('Acción de la barra: $_accion'),
      ],
    );
  }
}

class _NavegacionInferior extends StatefulWidget {
  const _NavegacionInferior();
  @override
  State<_NavegacionInferior> createState() => _NavegacionInferiorState();
}

class _NavegacionInferiorState extends State<_NavegacionInferior> {
  static const _destinos = [('Inicio', Icons.home), ('Favoritos', Icons.star), ('Perfil', Icons.person)];
  int _elegido = 0;
  bool _favoritosVistos = false;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return DemoCard(
      titulo: 'Barra de navegación inferior',
      componente: 'NavigationBar + NavigationDestination (y NavigationDrawer en la app)',
      descripcion: 'Cambia entre destinos principales de una app con un toque. Además, esta app usa un menú '
          'lateral para ir a las seis secciones.',
      children: [
        Container(
          clipBehavior: Clip.antiAlias,
          decoration: BoxDecoration(
            border: Border.all(color: tema.colorScheme.outline),
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            children: [
              SizedBox(
                height: 110,
                child: Center(
                  child: Text('Destino: ${_destinos[_elegido].$1}', style: tema.textTheme.titleLarge),
                ),
              ),
              // Se quita el margen inferior del sistema porque la barra está dentro de una tarjeta.
              MediaQuery.removePadding(
                context: context,
                removeBottom: true,
                child: NavigationBar(
                  selectedIndex: _elegido,
                  onDestinationSelected: (i) => setState(() {
                    _elegido = i;
                    if (i == 1) _favoritosVistos = true;
                  }),
                  destinations: [
                    for (var i = 0; i < _destinos.length; i++)
                      NavigationDestination(
                        icon: Badge(
                          isLabelVisible: i == 1 && !_favoritosVistos,
                          label: const Text('3'),
                          child: Icon(_destinos[i].$2),
                        ),
                        label: _destinos[i].$1,
                      ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}

class _PesosYAlineacion extends StatefulWidget {
  const _PesosYAlineacion();
  @override
  State<_PesosYAlineacion> createState() => _PesosYAlineacionState();
}

class _PesosYAlineacionState extends State<_PesosYAlineacion> {
  double _pesoA = 1;
  double _sesgo = 50;

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    return DemoCard(
      titulo: 'Pesos proporcionales y alineación con sesgo',
      componente: 'Expanded(flex) / Align(Alignment(x, y))',
      descripcion: 'Con Expanded y flex el espacio se reparte en proporción. Flutter no usa restricciones '
          'entre vistas; Align con Alignment(x, y) equivale al sesgo de ConstraintLayout. Mueve los deslizadores.',
      children: [
        SizedBox(
          height: 56,
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Expanded(flex: _pesoA.round(), child: const _Caja('A', tamano: null)),
              Expanded(flex: 2, child: _Caja('B', fondo: esquema.secondaryContainer, tamano: null)),
              Expanded(flex: 1, child: _Caja('C', fondo: esquema.tertiaryContainer, tamano: null)),
            ],
          ),
        ),
        Slider(
          value: _pesoA,
          min: 1,
          max: 5,
          divisions: 4,
          label: '${_pesoA.round()}',
          onChanged: (v) => setState(() => _pesoA = v),
        ),
        Resultado('Pesos → A: ${_pesoA.round()} · B: 2 · C: 1'),
        Container(
          height: 100,
          padding: const EdgeInsets.all(Espacio.s),
          decoration: BoxDecoration(
            border: Border.all(color: esquema.outline),
            borderRadius: BorderRadius.circular(12),
          ),
          child: Align(
            alignment: Alignment(_sesgo / 50 - 1, 0),
            child: _Caja('2', fondo: esquema.tertiaryContainer),
          ),
        ),
        Slider(value: _sesgo, min: 0, max: 100, onChanged: (v) => setState(() => _sesgo = v)),
        Resultado('Sesgo horizontal: ${_sesgo.round()} %'),
      ],
    );
  }
}
