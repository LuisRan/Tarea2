import 'package:flutter/material.dart';

import '../comunes.dart';

/// Sección 2: Botones y acciones.
class BotonesPantalla extends StatelessWidget {
  const BotonesPantalla({super.key});

  @override
  Widget build(BuildContext context) {
    return PantallaSeccion(
      destino: secciones[1],
      intro: 'Los botones ejecutan acciones. Cada uno responde al pulsarlo mostrando un mensaje.',
      hijos: const [
        _BotonesBasicos(),
        _BotonesIcono(),
        _BotonesFlotantes(),
        _BotonSegmentado(),
        _BotonesEstados(),
      ],
    );
  }
}

class _BotonesBasicos extends StatefulWidget {
  const _BotonesBasicos();
  @override
  State<_BotonesBasicos> createState() => _BotonesBasicosState();
}

class _BotonesBasicosState extends State<_BotonesBasicos> {
  String _mensaje = 'Toca un botón.';
  int _total = 0;

  void _pulsar(String nombre) => setState(() {
        _total++;
        _mensaje = 'Pulsaste «$nombre» ($_total veces en total)';
      });

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Botón relleno, con contorno y de texto',
      componente: 'FilledButton / OutlinedButton / TextButton',
      descripcion: 'Representan distintos niveles de énfasis: el relleno es la acción principal, el de '
          'contorno una secundaria y el de texto una acción de baja prioridad.',
      children: [
        Wrap(
          spacing: Espacio.s,
          runSpacing: Espacio.s,
          children: [
            FilledButton(onPressed: () => _pulsar('Relleno'), child: const Text('Relleno')),
            OutlinedButton(onPressed: () => _pulsar('Contorno'), child: const Text('Contorno')),
            TextButton(onPressed: () => _pulsar('Texto'), child: const Text('Texto')),
          ],
        ),
        Resultado(_mensaje),
      ],
    );
  }
}

class _BotonesIcono extends StatefulWidget {
  const _BotonesIcono();
  @override
  State<_BotonesIcono> createState() => _BotonesIconoState();
}

class _BotonesIconoState extends State<_BotonesIcono> {
  bool _favorito = false;
  String _mensaje = 'Toca un botón.';

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Botones con ícono',
      componente: 'IconButton.filled / IconButton.outlined / FilledButton.icon',
      descripcion: 'Un botón de solo ícono ahorra espacio en acciones reconocibles; el de ícono más texto '
          'refuerza el significado de la acción.',
      children: [
        Wrap(
          spacing: Espacio.s,
          runSpacing: Espacio.s,
          crossAxisAlignment: WrapCrossAlignment.center,
          children: [
            IconButton.filled(
              tooltip: 'Marcar como favorito',
              isSelected: _favorito,
              icon: const Icon(Icons.favorite_border),
              selectedIcon: const Icon(Icons.favorite),
              onPressed: () => setState(() {
                _favorito = !_favorito;
                _mensaje = _favorito ? 'Agregado a favoritos ♥' : 'Quitado de favoritos';
              }),
            ),
            IconButton.outlined(
              tooltip: 'Compartir',
              icon: const Icon(Icons.share),
              onPressed: () => setState(() => _mensaje = 'Acción: compartir'),
            ),
            FilledButton.icon(
              onPressed: () => setState(() => _mensaje = 'Mensaje enviado ✉'),
              icon: const Icon(Icons.send),
              label: const Text('Enviar'),
            ),
          ],
        ),
        Resultado(_mensaje),
      ],
    );
  }
}

class _BotonesFlotantes extends StatefulWidget {
  const _BotonesFlotantes();
  @override
  State<_BotonesFlotantes> createState() => _BotonesFlotantesState();
}

class _BotonesFlotantesState extends State<_BotonesFlotantes> {
  int _contador = 0;
  bool _extendido = true;

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Botón de acción flotante (FAB)',
      componente: 'FloatingActionButton / FloatingActionButton.extended',
      descripcion: 'Destaca la acción más importante de una pantalla. La versión extendida agrega una '
          'etiqueta y puede contraerse para ahorrar espacio.',
      children: [
        Row(
          children: [
            FloatingActionButton(
              heroTag: 'fab_normal',
              tooltip: 'Agregar',
              onPressed: () => setState(() => _contador++),
              child: const Icon(Icons.add),
            ),
            const SizedBox(width: Espacio.m),
            FloatingActionButton.extended(
              heroTag: 'fab_extendido',
              isExtended: _extendido,
              onPressed: () => setState(() => _extendido = !_extendido),
              icon: const Icon(Icons.edit),
              label: const Text('Redactar'),
            ),
          ],
        ),
        Resultado('FAB: $_contador · Extendido: ${_extendido ? 'extendido' : 'contraído'}'),
      ],
    );
  }
}

class _BotonSegmentado extends StatefulWidget {
  const _BotonSegmentado();
  @override
  State<_BotonSegmentado> createState() => _BotonSegmentadoState();
}

class _BotonSegmentadoState extends State<_BotonSegmentado> {
  static const _opciones = ['Día', 'Semana', 'Mes'];
  int _elegido = 0;

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Selector segmentado (toggle)',
      componente: 'SegmentedButton<int>',
      descripcion: 'Agrupa opciones relacionadas en las que solo una puede estar activa, como cambiar '
          'entre vistas de día, semana o mes.',
      children: [
        SizedBox(
          width: double.infinity,
          child: SegmentedButton<int>(
            segments: [
              for (var i = 0; i < _opciones.length; i++)
                ButtonSegment(value: i, label: Text(_opciones[i])),
            ],
            selected: {_elegido},
            onSelectionChanged: (s) => setState(() => _elegido = s.first),
          ),
        ),
        Resultado('Vista actual: ${_opciones[_elegido]}'),
      ],
    );
  }
}

class _BotonesEstados extends StatefulWidget {
  const _BotonesEstados();
  @override
  State<_BotonesEstados> createState() => _BotonesEstadosState();
}

class _BotonesEstadosState extends State<_BotonesEstados> {
  bool _habilitado = false;
  bool _cargando = false;
  String _mensaje = '';

  Future<void> _descargar() async {
    setState(() {
      _cargando = true;
      _mensaje = 'Descargando…';
    });
    await Future.delayed(const Duration(seconds: 2));
    if (!mounted) return;
    setState(() {
      _cargando = false;
      _mensaje = 'Descarga completa ✔';
    });
  }

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Botón deshabilitado y en estado de carga',
      componente: 'FilledButton(onPressed: null) + CircularProgressIndicator',
      descripcion: 'Un botón deshabilitado indica que la acción no está disponible todavía. El estado de '
          'carga informa que la acción está en proceso y evita pulsaciones repetidas.',
      children: [
        SwitchListTile(
          contentPadding: EdgeInsets.zero,
          title: const Text('Habilitar el botón'),
          value: _habilitado,
          onChanged: (v) => setState(() => _habilitado = v),
        ),
        FilledButton(
          // onPressed nulo = botón deshabilitado.
          onPressed: _habilitado ? () => setState(() => _mensaje = '¡Pedido confirmado!') : null,
          child: const Text('Confirmar pedido'),
        ),
        SizedBox(
          width: 200,
          child: FilledButton.tonal(
            onPressed: _cargando ? null : _descargar,
            child: _cargando
                ? const SizedBox(width: 20, height: 20, child: CircularProgressIndicator(strokeWidth: 2))
                : const Text('Descargar archivo'),
          ),
        ),
        if (_mensaje.isNotEmpty) Resultado(_mensaje),
      ],
    );
  }
}
