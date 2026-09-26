import 'package:flutter/material.dart';

import '../comunes.dart';
import '../estado.dart';

/// Sección 3: Elementos de selección.
class SeleccionPantalla extends StatelessWidget {
  const SeleccionPantalla({super.key});

  @override
  Widget build(BuildContext context) {
    return PantallaSeccion(
      destino: secciones[2],
      intro: 'Los elementos de selección permiten elegir entre opciones sin escribir.',
      hijos: const [
        _Casillas(),
        _Opciones(),
        _Interruptor(),
        _Deslizadores(),
        _Desplegable(),
        _FechaYHora(),
        _Chips(),
      ],
    );
  }
}

class _Casillas extends StatefulWidget {
  const _Casillas();
  @override
  State<_Casillas> createState() => _CasillasState();
}

class _CasillasState extends State<_Casillas> {
  static const _nombres = ['Queso', 'Jamón', 'Piña'];
  final _marcados = [false, true, false];

  /// true = todas, false = ninguna, null = indeterminado.
  bool? get _padre {
    final n = _marcados.where((m) => m).length;
    if (n == 0) return false;
    if (n == _marcados.length) return true;
    return null;
  }

  @override
  Widget build(BuildContext context) {
    final elegidos = [for (var i = 0; i < _nombres.length; i++) if (_marcados[i]) _nombres[i]];
    return DemoCard(
      titulo: 'Casillas de verificación',
      componente: 'Checkbox / CheckboxListTile (tristate: true)',
      descripcion: 'Permiten marcar varias opciones independientes. La casilla principal muestra un '
          'estado indeterminado cuando solo algunas opciones están marcadas.',
      children: [
        CheckboxListTile(
          contentPadding: EdgeInsets.zero,
          controlAffinity: ListTileControlAffinity.leading,
          tristate: true,
          value: _padre,
          title: const Text('Todos los ingredientes'),
          onChanged: (_) => setState(() {
            final nuevo = _padre != true;
            for (var i = 0; i < _marcados.length; i++) {
              _marcados[i] = nuevo;
            }
          }),
        ),
        for (var i = 0; i < _nombres.length; i++)
          Padding(
            padding: const EdgeInsets.only(left: 32),
            child: CheckboxListTile(
              contentPadding: EdgeInsets.zero,
              dense: true,
              controlAffinity: ListTileControlAffinity.leading,
              value: _marcados[i],
              title: Text(_nombres[i]),
              onChanged: (v) => setState(() => _marcados[i] = v ?? false),
            ),
          ),
        Resultado('Seleccionados: ${elegidos.isEmpty ? 'ninguno' : elegidos.join(', ')}'),
      ],
    );
  }
}

class _Opciones extends StatefulWidget {
  const _Opciones();
  @override
  State<_Opciones> createState() => _OpcionesState();
}

class _OpcionesState extends State<_Opciones> {
  static const _opciones = ['Envío estándar', 'Envío exprés', 'Recoger en tienda'];
  String _elegida = _opciones.first;

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Botones de opción',
      componente: 'RadioGroup + RadioListTile',
      descripcion: 'Grupo de opciones mutuamente excluyentes: al elegir una se desmarca la anterior. '
          'Útil cuando solo puede haber una respuesta.',
      children: [
        RadioGroup<String>(
          groupValue: _elegida,
          onChanged: (v) => setState(() => _elegida = v ?? _elegida),
          child: Column(
            children: [
              for (final o in _opciones)
                RadioListTile<String>(contentPadding: EdgeInsets.zero, value: o, title: Text(o)),
            ],
          ),
        ),
        Resultado('Opción elegida: $_elegida'),
      ],
    );
  }
}

class _Interruptor extends StatefulWidget {
  const _Interruptor();
  @override
  State<_Interruptor> createState() => _InterruptorState();
}

class _InterruptorState extends State<_Interruptor> {
  bool _activo = false;

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Interruptor (switch)',
      componente: 'Switch',
      descripcion: 'Activa o desactiva una opción de forma inmediata, como una preferencia de configuración.',
      children: [
        Row(
          children: [
            const Expanded(child: Text('Recibir notificaciones')),
            Switch(value: _activo, onChanged: (v) => setState(() => _activo = v)),
          ],
        ),
        Resultado(_activo ? 'Notificaciones activadas 🔔' : 'Notificaciones desactivadas'),
      ],
    );
  }
}

class _Deslizadores extends StatefulWidget {
  const _Deslizadores();
  @override
  State<_Deslizadores> createState() => _DeslizadoresState();
}

class _DeslizadoresState extends State<_Deslizadores> {
  RangeValues _rango = const RangeValues(200, 800);

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    return DemoCard(
      titulo: 'Deslizador de valor único y de rango',
      componente: 'Slider / RangeSlider',
      descripcion: 'El deslizador elige un valor dentro de un intervalo; el de rango elige un mínimo y '
          'un máximo. El primero define el tamaño de texto que se usa en la Sección 5.',
      children: [
        Slider(
          value: estado.tamanoTexto,
          min: 12,
          max: 32,
          divisions: 10,
          label: '${estado.tamanoTexto.round()} sp',
          onChanged: estado.cambiarTamanoTexto,
        ),
        Resultado('Tamaño de texto para la Sección 5: ${estado.tamanoTexto.round()} sp'),
        RangeSlider(
          values: _rango,
          min: 0,
          max: 1000,
          divisions: 20,
          labels: RangeLabels('\$${_rango.start.round()}', '\$${_rango.end.round()}'),
          onChanged: (v) => setState(() => _rango = v),
        ),
        Resultado('Precio entre \$${_rango.start.round()} y \$${_rango.end.round()}'),
      ],
    );
  }
}

class _Desplegable extends StatefulWidget {
  const _Desplegable();
  @override
  State<_Desplegable> createState() => _DesplegableState();
}

class _DesplegableState extends State<_Desplegable> {
  static const _paises = ['México', 'Argentina', 'Chile', 'Colombia', 'España', 'Perú', 'Uruguay'];
  String? _pais;

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Lista desplegable',
      componente: 'DropdownMenu<String>',
      descripcion: 'Muestra una lista de opciones al tocarla y conserva la elegida. Ocupa poco espacio '
          'cuando hay muchas opciones.',
      children: [
        DropdownMenu<String>(
          label: const Text('País'),
          expandedInsets: EdgeInsets.zero,
          requestFocusOnTap: false,
          dropdownMenuEntries: [for (final p in _paises) DropdownMenuEntry(value: p, label: p)],
          onSelected: (p) => setState(() => _pais = p),
        ),
        Resultado('País elegido: ${_pais ?? '—'}'),
      ],
    );
  }
}

class _FechaYHora extends StatefulWidget {
  const _FechaYHora();
  @override
  State<_FechaYHora> createState() => _FechaYHoraState();
}

class _FechaYHoraState extends State<_FechaYHora> {
  DateTime? _fecha;
  TimeOfDay? _hora;

  Future<void> _elegirFecha() async {
    final hoy = DateTime.now();
    final fecha = await showDatePicker(
      context: context,
      initialDate: _fecha ?? hoy,
      firstDate: DateTime(hoy.year - 5),
      lastDate: DateTime(hoy.year + 5),
      helpText: 'Selecciona una fecha',
    );
    if (fecha != null) setState(() => _fecha = fecha);
  }

  Future<void> _elegirHora() async {
    final hora = await showTimePicker(
      context: context,
      initialTime: _hora ?? const TimeOfDay(hour: 12, minute: 0),
      helpText: 'Selecciona una hora',
      builder: (context, hijo) => MediaQuery(
        data: MediaQuery.of(context).copyWith(alwaysUse24HourFormat: true),
        child: hijo!,
      ),
    );
    if (hora != null) setState(() => _hora = hora);
  }

  @override
  Widget build(BuildContext context) {
    final loc = MaterialLocalizations.of(context);
    return DemoCard(
      titulo: 'Selector de fecha y de hora',
      componente: 'showDatePicker / showTimePicker',
      descripcion: 'Abren diálogos con un calendario o un reloj para elegir fechas y horas válidas sin '
          'escribirlas a mano.',
      children: [
        Wrap(
          spacing: Espacio.s,
          runSpacing: Espacio.s,
          children: [
            OutlinedButton.icon(
              onPressed: _elegirFecha,
              icon: const Icon(Icons.calendar_month),
              label: const Text('Elegir fecha'),
            ),
            OutlinedButton.icon(
              onPressed: _elegirHora,
              icon: const Icon(Icons.schedule),
              label: const Text('Elegir hora'),
            ),
          ],
        ),
        Resultado('Fecha: ${_fecha == null ? 'sin elegir' : loc.formatFullDate(_fecha!)}'),
        Resultado('Hora: ${_hora == null ? 'sin elegir' : loc.formatTimeOfDay(_hora!, alwaysUse24HourFormat: true)}'),
      ],
    );
  }
}

class _Chips extends StatefulWidget {
  const _Chips();
  @override
  State<_Chips> createState() => _ChipsState();
}

class _ChipsState extends State<_Chips> {
  static const _filtros = ['Vegano', 'Sin gluten', 'Orgánico', 'Local', 'De temporada'];
  final _activos = <String>{};

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Chips de filtro',
      componente: 'FilterChip + Wrap',
      descripcion: 'Etiquetas compactas que se activan o desactivan para filtrar contenido. Se pueden '
          'combinar varias a la vez.',
      children: [
        Wrap(
          spacing: Espacio.s,
          runSpacing: Espacio.s,
          children: [
            for (final f in _filtros)
              FilterChip(
                label: Text(f),
                selected: _activos.contains(f),
                onSelected: (s) => setState(() => s ? _activos.add(f) : _activos.remove(f)),
              ),
          ],
        ),
        Resultado('Filtros activos: ${_activos.isEmpty ? 'ninguno' : _activos.join(', ')}'),
      ],
    );
  }
}
