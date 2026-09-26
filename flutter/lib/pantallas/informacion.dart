import 'dart:async';

import 'package:flutter/material.dart';

import '../comunes.dart';
import '../estado.dart';

/// Sección 5: Información y retroalimentación.
class InformacionPantalla extends StatelessWidget {
  const InformacionPantalla({super.key});

  @override
  Widget build(BuildContext context) {
    return PantallaSeccion(
      destino: secciones[4],
      intro: 'Elementos que comunican información y dan retroalimentación al usuario.',
      hijos: const [
        _Textos(),
        _Imagenes(),
        _Progreso(),
        _Mensajes(),
        _Dialogo(),
        _HojaInferior(),
        _TarjetaSeparadorBadge(),
      ],
    );
  }
}

class _Textos extends StatefulWidget {
  const _Textos();
  @override
  State<_Textos> createState() => _TextosState();
}

class _TextosState extends State<_Textos> {
  bool _mayusculas = false;

  String _t(String s) => _mayusculas ? s.toUpperCase() : s;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    final estilos = tema.textTheme;
    final colores = tema.colorScheme;
    final tamano = EstadoScope.of(context).tamanoTexto;
    return DemoCard(
      titulo: 'Textos con distintos estilos',
      componente: 'Text (TextTheme, TextStyle) / Text.rich',
      descripcion: 'La tipografía crea jerarquía: títulos grandes, subtítulos, cuerpo y etiquetas, además '
          'de énfasis con negritas, cursivas y color.',
      children: [
        Text(_t('Título grande'), style: estilos.displaySmall),
        Text(_t('Encabezado de sección'), style: estilos.headlineSmall),
        Text(_t('Subtítulo mediano'), style: estilos.titleMedium),
        Text(_t('Texto de cuerpo para párrafos largos y descripciones.'), style: estilos.bodyMedium),
        Text('ETIQUETA PEQUEÑA', style: estilos.labelSmall?.copyWith(color: colores.onSurfaceVariant)),
        const Text('Texto en negritas', style: TextStyle(fontWeight: FontWeight.bold)),
        const Text('Texto en cursiva', style: TextStyle(fontStyle: FontStyle.italic)),
        Text('Texto con el color principal',
            style: TextStyle(color: colores.primary, fontWeight: FontWeight.bold)),
        Text.rich(
          TextSpan(
            children: [
              const TextSpan(text: 'Un mismo texto puede combinar '),
              const TextSpan(text: 'negritas', style: TextStyle(fontWeight: FontWeight.bold)),
              const TextSpan(text: ', '),
              const TextSpan(text: 'cursivas', style: TextStyle(fontStyle: FontStyle.italic)),
              const TextSpan(text: ', '),
              const TextSpan(text: 'subrayado', style: TextStyle(decoration: TextDecoration.underline)),
              const TextSpan(text: ' y '),
              TextSpan(text: 'color', style: TextStyle(color: colores.primary)),
              const TextSpan(text: '.'),
            ],
          ),
        ),
        const Divider(),
        Text(
          'Este texto usa el tamaño elegido en la Sección 3 (${tamano.round()} sp).',
          style: TextStyle(fontSize: tamano, color: colores.tertiary),
        ),
        SwitchListTile(
          contentPadding: EdgeInsets.zero,
          title: const Text('Mostrar en mayúsculas'),
          value: _mayusculas,
          onChanged: (v) => setState(() => _mayusculas = v),
        ),
      ],
    );
  }
}

class _Imagenes extends StatefulWidget {
  const _Imagenes();
  @override
  State<_Imagenes> createState() => _ImagenesState();
}

class _ImagenesState extends State<_Imagenes> {
  static const _modos = {
    'cover': BoxFit.cover,
    'contain': BoxFit.contain,
    'none': BoxFit.none,
    'fill': BoxFit.fill,
  };
  String _modo = 'cover';
  int _semilla = 1015;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    final ajuste = _modos[_modo]!;
    Widget marco(Widget imagen, String pie) => Expanded(
          child: Column(
            children: [
              Container(
                height: 130,
                width: double.infinity,
                clipBehavior: Clip.hardEdge,
                decoration: BoxDecoration(color: tema.colorScheme.surfaceContainerHighest),
                child: imagen,
              ),
              Text(pie, style: tema.textTheme.labelMedium),
            ],
          ),
        );
    return DemoCard(
      titulo: 'Imagen local y desde URL',
      componente: 'Image.asset / Image.network + BoxFit',
      descripcion: 'Muestra una imagen incluida en la app y otra descargada de internet. El modo de '
          'escalado define cómo se ajusta la imagen a su contenedor.',
      children: [
        Wrap(
          spacing: Espacio.s,
          runSpacing: Espacio.s,
          children: [
            for (final m in _modos.keys)
              ChoiceChip(label: Text(m), selected: _modo == m, onSelected: (_) => setState(() => _modo = m)),
          ],
        ),
        Row(
          children: [
            marco(
              Image.asset('assets/paisaje.png', fit: ajuste, semanticLabel: 'Paisaje de montañas incluido en la app'),
              'Local (asset)',
            ),
            const SizedBox(width: Espacio.s),
            marco(
              Image.network(
                'https://picsum.photos/seed/$_semilla/600/400',
                key: ValueKey(_semilla),
                fit: ajuste,
                semanticLabel: 'Fotografía descargada de internet',
                loadingBuilder: (context, hijo, progreso) =>
                    progreso == null ? hijo : const Center(child: CircularProgressIndicator()),
                errorBuilder: (context, error, pila) => const Center(child: Icon(Icons.broken_image, size: 40)),
              ),
              'Desde URL',
            ),
          ],
        ),
        FilledButton.tonalIcon(
          onPressed: () => setState(() => _semilla++),
          icon: const Icon(Icons.refresh),
          label: const Text('Cargar otra imagen'),
        ),
      ],
    );
  }
}

class _Progreso extends StatefulWidget {
  const _Progreso();
  @override
  State<_Progreso> createState() => _ProgresoState();
}

class _ProgresoState extends State<_Progreso> {
  int _progreso = 40;
  bool _indeterminados = true;
  Timer? _temporizador;

  void _simular() {
    _temporizador?.cancel();
    setState(() => _progreso = 0);
    _temporizador = Timer.periodic(const Duration(milliseconds: 150), (t) {
      setState(() => _progreso += 5);
      if (_progreso >= 100) t.cancel();
    });
  }

  @override
  void dispose() {
    _temporizador?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final valor = _progreso / 100;
    return DemoCard(
      titulo: 'Indicadores de progreso',
      componente: 'LinearProgressIndicator / CircularProgressIndicator',
      descripcion: 'El modo determinado muestra cuánto falta para terminar; el indeterminado indica que hay '
          'trabajo en curso de duración desconocida.',
      children: [
        Row(
          children: [
            Expanded(child: LinearProgressIndicator(value: valor)),
            const SizedBox(width: Espacio.m),
            CircularProgressIndicator(value: valor),
          ],
        ),
        Resultado('Progreso: $_progreso %'),
        Wrap(
          spacing: Espacio.s,
          runSpacing: Espacio.s,
          children: [
            OutlinedButton(
              onPressed: () => setState(() => _progreso = (_progreso - 10).clamp(0, 100).toInt()),
              child: const Text('−10'),
            ),
            OutlinedButton(
              onPressed: () => setState(() => _progreso = (_progreso + 10).clamp(0, 100).toInt()),
              child: const Text('+10'),
            ),
            FilledButton(onPressed: _simular, child: const Text('Simular')),
          ],
        ),
        SwitchListTile(
          contentPadding: EdgeInsets.zero,
          title: const Text('Mostrar indicadores indeterminados'),
          value: _indeterminados,
          onChanged: (v) => setState(() => _indeterminados = v),
        ),
        if (_indeterminados)
          const Row(
            children: [
              Expanded(child: LinearProgressIndicator()),
              SizedBox(width: Espacio.m),
              CircularProgressIndicator(),
            ],
          ),
      ],
    );
  }
}

class _Mensajes extends StatefulWidget {
  const _Mensajes();
  @override
  State<_Mensajes> createState() => _MensajesState();
}

class _MensajesState extends State<_Mensajes> {
  String _mensaje = '';

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Toast y Snackbar',
      componente: 'Toast propio (OverlayEntry) / SnackBar + SnackBarAction',
      descripcion: 'El toast es un aviso breve que desaparece solo. El snackbar aparece en la parte inferior '
          'y puede incluir una acción, como deshacer.',
      children: [
        Wrap(
          spacing: Espacio.s,
          runSpacing: Espacio.s,
          children: [
            FilledButton.tonal(
              onPressed: () => mostrarToast(context, 'Esto es un toast'),
              child: const Text('Mostrar toast'),
            ),
            FilledButton.tonal(
              onPressed: () {
                setState(() => _mensaje = 'Se archivó el mensaje');
                ScaffoldMessenger.of(context)
                  ..hideCurrentSnackBar()
                  ..showSnackBar(SnackBar(
                    content: const Text('Se archivó el mensaje'),
                    duration: const Duration(seconds: 5),
                    action: SnackBarAction(
                      label: 'Deshacer',
                      onPressed: () {
                        if (mounted) setState(() => _mensaje = 'Pulsaste «Deshacer»: el mensaje se restauró');
                      },
                    ),
                  ));
              },
              child: const Text('Mostrar snackbar'),
            ),
          ],
        ),
        if (_mensaje.isNotEmpty) Resultado(_mensaje),
      ],
    );
  }
}

class _Dialogo extends StatefulWidget {
  const _Dialogo();
  @override
  State<_Dialogo> createState() => _DialogoState();
}

class _DialogoState extends State<_Dialogo> {
  String _mensaje = '';

  Future<void> _confirmar() async {
    final ok = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        icon: const Icon(Icons.delete),
        title: const Text('¿Eliminar archivo?'),
        content: const Text('El archivo «reporte.pdf» se eliminará de forma permanente.'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Cancelar')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('Eliminar')),
        ],
      ),
    );
    if (ok == null || !mounted) return;
    setState(() => _mensaje = ok ? 'Confirmaste: archivo eliminado' : 'Cancelaste la eliminación');
  }

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Diálogo de confirmación',
      componente: 'showDialog + AlertDialog',
      descripcion: 'Interrumpe al usuario para confirmar una acción importante o irreversible antes de '
          'ejecutarla.',
      children: [
        FilledButton.icon(onPressed: _confirmar, icon: const Icon(Icons.delete), label: const Text('Eliminar archivo')),
        if (_mensaje.isNotEmpty) Resultado(_mensaje),
      ],
    );
  }
}

class _HojaInferior extends StatefulWidget {
  const _HojaInferior();
  @override
  State<_HojaInferior> createState() => _HojaInferiorState();
}

class _HojaInferiorState extends State<_HojaInferior> {
  String _mensaje = '';

  Future<void> _abrir() async {
    const opciones = [('Compartir', Icons.share), ('Editar', Icons.edit), ('Eliminar', Icons.delete)];
    final elegida = await showModalBottomSheet<String>(
      context: context,
      showDragHandle: true,
      builder: (context) => SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: Espacio.l, vertical: Espacio.s),
              child: Text('¿Qué deseas hacer?', style: Theme.of(context).textTheme.titleLarge),
            ),
            for (final o in opciones)
              ListTile(
                leading: Icon(o.$2),
                title: Text(o.$1),
                onTap: () => Navigator.pop(context, o.$1),
              ),
            const SizedBox(height: Espacio.m),
          ],
        ),
      ),
    );
    if (elegida != null && mounted) setState(() => _mensaje = 'Elegiste en la hoja: $elegida');
  }

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Hoja inferior (bottom sheet)',
      componente: 'showModalBottomSheet',
      descripcion: 'Panel que sube desde la parte inferior con opciones o contenido adicional, sin salir de '
          'la pantalla actual.',
      children: [
        FilledButton(onPressed: _abrir, child: const Text('Abrir hoja inferior')),
        if (_mensaje.isNotEmpty) Resultado(_mensaje),
      ],
    );
  }
}

class _TarjetaSeparadorBadge extends StatefulWidget {
  const _TarjetaSeparadorBadge();
  @override
  State<_TarjetaSeparadorBadge> createState() => _TarjetaSeparadorBadgeState();
}

class _TarjetaSeparadorBadgeState extends State<_TarjetaSeparadorBadge> {
  bool _marcada = false;
  int _notificaciones = 0;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return DemoCard(
      titulo: 'Tarjeta, separador y badge',
      componente: 'Card / Divider / Badge',
      descripcion: 'La tarjeta agrupa información relacionada, el separador divide contenido y el badge '
          'muestra un contador sobre un ícono.',
      children: [
        Card(
          margin: EdgeInsets.zero,
          elevation: 2,
          color: _marcada ? tema.colorScheme.secondaryContainer : null,
          clipBehavior: Clip.antiAlias,
          child: InkWell(
            onTap: () => setState(() => _marcada = !_marcada),
            child: Padding(
              padding: const EdgeInsets.all(Espacio.m),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Tarjeta seleccionable', style: tema.textTheme.titleMedium),
                  const Divider(height: Espacio.m),
                  Text(
                    _marcada ? 'Tarjeta marcada ✔' : 'Toca la tarjeta para marcarla o desmarcarla.',
                    style: tema.textTheme.bodyMedium,
                  ),
                ],
              ),
            ),
          ),
        ),
        const Divider(height: Espacio.l),
        Wrap(
          spacing: Espacio.m,
          runSpacing: Espacio.s,
          crossAxisAlignment: WrapCrossAlignment.center,
          children: [
            Badge(
              isLabelVisible: _notificaciones > 0,
              label: Text(_notificaciones > 99 ? '99+' : '$_notificaciones'),
              child: const Icon(Icons.notifications, size: 32, semanticLabel: 'Notificaciones'),
            ),
            FilledButton.tonal(
              onPressed: () => setState(() => _notificaciones++),
              child: const Text('Nueva notificación'),
            ),
            TextButton(onPressed: () => setState(() => _notificaciones = 0), child: const Text('Limpiar')),
          ],
        ),
        Resultado('Notificaciones pendientes: $_notificaciones'),
      ],
    );
  }
}
