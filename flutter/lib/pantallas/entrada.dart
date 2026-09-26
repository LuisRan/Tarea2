import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../comunes.dart';
import '../estado.dart';

const _estados = [
  'Aguascalientes', 'Baja California', 'Baja California Sur', 'Campeche', 'Chiapas', 'Chihuahua',
  'Ciudad de México', 'Coahuila', 'Colima', 'Durango', 'Estado de México', 'Guanajuato', 'Guerrero',
  'Hidalgo', 'Jalisco', 'Michoacán', 'Morelos', 'Nayarit', 'Nuevo León', 'Oaxaca', 'Puebla',
  'Querétaro', 'Quintana Roo', 'San Luis Potosí', 'Sinaloa', 'Sonora', 'Tabasco', 'Tamaulipas',
  'Tlaxcala', 'Veracruz', 'Yucatán', 'Zacatecas',
];

/// Sección 1: Entrada de texto.
class EntradaPantalla extends StatelessWidget {
  const EntradaPantalla({super.key});

  @override
  Widget build(BuildContext context) {
    return PantallaSeccion(
      destino: secciones[0],
      intro: 'Los campos de texto permiten al usuario capturar información. '
          'Prueba cada uno y observa la respuesta debajo.',
      hijos: const [
        _CampoSimple(),
        _CampoValidado(),
        _CampoContrasena(),
        _CamposTeclado(),
        _CampoMultilinea(),
        _CampoSugerencias(),
        _BarraBusqueda(),
        _AgregarALista(),
      ],
    );
  }
}

class _CampoSimple extends StatefulWidget {
  const _CampoSimple();
  @override
  State<_CampoSimple> createState() => _CampoSimpleState();
}

class _CampoSimpleState extends State<_CampoSimple> {
  String _nombre = '';

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Campo de texto simple',
      componente: 'TextField (InputDecoration.labelText)',
      descripcion: 'Captura texto libre de una sola línea. La etiqueta flotante indica qué dato se '
          'espera y sube cuando el campo recibe el foco.',
      children: [
        TextField(
          decoration: const InputDecoration(labelText: 'Tu nombre', border: OutlineInputBorder()),
          textCapitalization: TextCapitalization.words,
          onChanged: (v) => setState(() => _nombre = v.trim()),
        ),
        Resultado(_nombre.isEmpty ? 'Escribe tu nombre para saludarte.' : '¡Hola, $_nombre!'),
      ],
    );
  }
}

class _CampoValidado extends StatefulWidget {
  const _CampoValidado();
  @override
  State<_CampoValidado> createState() => _CampoValidadoState();
}

class _CampoValidadoState extends State<_CampoValidado> {
  String _cp = '';

  @override
  Widget build(BuildContext context) {
    final error = _cp.isNotEmpty && _cp.length != 5;
    return DemoCard(
      titulo: 'Campo con validación',
      componente: 'TextField (errorText, helperText, counter)',
      descripcion: 'Comprueba el dato mientras se escribe y muestra un mensaje de error visible cuando '
          'no cumple la regla. Aquí se exige un código postal de 5 dígitos.',
      children: [
        TextField(
          keyboardType: TextInputType.number,
          maxLength: 5,
          inputFormatters: [FilteringTextInputFormatter.digitsOnly],
          onChanged: (v) => setState(() => _cp = v),
          decoration: InputDecoration(
            labelText: 'Código postal',
            border: const OutlineInputBorder(),
            helperText: _cp.length == 5 ? 'Código postal válido ✔' : 'Debe tener 5 dígitos',
            errorText: error ? 'El código postal debe tener exactamente 5 dígitos' : null,
          ),
        ),
      ],
    );
  }
}

class _CampoContrasena extends StatefulWidget {
  const _CampoContrasena();
  @override
  State<_CampoContrasena> createState() => _CampoContrasenaState();
}

class _CampoContrasenaState extends State<_CampoContrasena> {
  String _clave = '';
  bool _visible = false;

  String get _seguridad {
    if (_clave.isEmpty) return 'Seguridad: escribe una contraseña';
    var puntos = 0;
    if (_clave.length >= 8) puntos++;
    if (RegExp(r'\d').hasMatch(_clave)) puntos++;
    if (RegExp(r'[A-ZÁÉÍÓÚÑ]').hasMatch(_clave)) puntos++;
    if (RegExp(r'[^A-Za-z0-9áéíóúñÁÉÍÓÚÑ]').hasMatch(_clave)) puntos++;
    if (puntos <= 1) return 'Seguridad: débil';
    if (puntos <= 3) return 'Seguridad: media';
    return 'Seguridad: fuerte';
  }

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Campo de contraseña',
      componente: 'TextField (obscureText) + IconButton',
      descripcion: 'Oculta los caracteres para proteger datos sensibles. El ícono del ojo permite '
          'mostrar u ocultar el contenido.',
      children: [
        TextField(
          obscureText: !_visible,
          onChanged: (v) => setState(() => _clave = v),
          decoration: InputDecoration(
            labelText: 'Contraseña',
            border: const OutlineInputBorder(),
            suffixIcon: IconButton(
              tooltip: _visible ? 'Ocultar contraseña' : 'Mostrar contraseña',
              icon: Icon(_visible ? Icons.visibility_off : Icons.visibility),
              onPressed: () => setState(() => _visible = !_visible),
            ),
          ),
        ),
        Resultado(_seguridad),
      ],
    );
  }
}

class _CamposTeclado extends StatefulWidget {
  const _CamposTeclado();
  @override
  State<_CamposTeclado> createState() => _CamposTecladoState();
}

class _CamposTecladoState extends State<_CamposTeclado> {
  static final _formatoCorreo = RegExp(r'^[\w.+-]+@[\w-]+(\.[\w-]+)+$');
  String _correo = '';

  @override
  Widget build(BuildContext context) {
    final invalido = _correo.isNotEmpty && !_formatoCorreo.hasMatch(_correo);
    return DemoCard(
      titulo: 'Tipos de teclado',
      componente: 'TextField (keyboardType: TextInputType.…)',
      descripcion: 'keyboardType indica qué teclado mostrar: numérico, de correo (con @) o telefónico. '
          'Así se facilita la captura y se reducen errores.',
      children: [
        TextField(
          keyboardType: TextInputType.number,
          inputFormatters: [FilteringTextInputFormatter.digitsOnly, LengthLimitingTextInputFormatter(3)],
          decoration: const InputDecoration(labelText: 'Edad (teclado numérico)', border: OutlineInputBorder()),
        ),
        TextField(
          keyboardType: TextInputType.emailAddress,
          onChanged: (v) => setState(() => _correo = v),
          decoration: InputDecoration(
            labelText: 'Correo electrónico',
            border: const OutlineInputBorder(),
            errorText: invalido ? 'Formato de correo no válido' : null,
          ),
        ),
        const TextField(
          keyboardType: TextInputType.phone,
          decoration: InputDecoration(labelText: 'Teléfono', border: OutlineInputBorder()),
        ),
      ],
    );
  }
}

class _CampoMultilinea extends StatefulWidget {
  const _CampoMultilinea();
  @override
  State<_CampoMultilinea> createState() => _CampoMultilineaState();
}

class _CampoMultilineaState extends State<_CampoMultilinea> {
  String _notas = '';

  @override
  Widget build(BuildContext context) {
    final lineas = _notas.isEmpty ? 0 : '\n'.allMatches(_notas).length + 1;
    final palabras = _notas.split(RegExp(r'\s+')).where((p) => p.isNotEmpty).length;
    return DemoCard(
      titulo: 'Campo multilínea',
      componente: 'TextField (minLines / maxLines, keyboardType.multiline)',
      descripcion: 'Admite varias líneas para textos largos como comentarios o notas. Crece conforme se '
          'escribe y muestra un contador de caracteres.',
      children: [
        TextField(
          minLines: 3,
          maxLines: 6,
          maxLength: 200,
          keyboardType: TextInputType.multiline,
          textCapitalization: TextCapitalization.sentences,
          onChanged: (v) => setState(() => _notas = v),
          decoration: const InputDecoration(
            labelText: 'Comentarios',
            alignLabelWithHint: true,
            border: OutlineInputBorder(),
          ),
        ),
        Resultado('Líneas: $lineas · Palabras: $palabras'),
      ],
    );
  }
}

class _CampoSugerencias extends StatefulWidget {
  const _CampoSugerencias();
  @override
  State<_CampoSugerencias> createState() => _CampoSugerenciasState();
}

class _CampoSugerenciasState extends State<_CampoSugerencias> {
  String? _elegido;

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Campo con sugerencias automáticas',
      componente: 'Autocomplete<String>',
      descripcion: 'Propone opciones mientras el usuario escribe, reduciendo errores de captura. '
          'Escribe las primeras letras de un estado de la República.',
      children: [
        Autocomplete<String>(
          optionsBuilder: (valor) {
            final texto = valor.text.trim().toLowerCase();
            if (texto.isEmpty) return const Iterable<String>.empty();
            return _estados.where((e) => e.toLowerCase().contains(texto));
          },
          onSelected: (e) => setState(() => _elegido = e),
          fieldViewBuilder: (context, controlador, foco, alEnviar) => TextField(
            controller: controlador,
            focusNode: foco,
            onSubmitted: (_) => alEnviar(),
            decoration: const InputDecoration(
              labelText: 'Estado de la República',
              prefixIcon: Icon(Icons.place),
              border: OutlineInputBorder(),
            ),
          ),
        ),
        Resultado(_elegido == null ? 'Aún no eliges un estado.' : 'Seleccionaste: $_elegido'),
      ],
    );
  }
}

class _BarraBusqueda extends StatefulWidget {
  const _BarraBusqueda();
  @override
  State<_BarraBusqueda> createState() => _BarraBusquedaState();
}

class _BarraBusquedaState extends State<_BarraBusqueda> {
  final _controlador = TextEditingController();

  @override
  void dispose() {
    _controlador.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    final consulta = _controlador.text.trim();
    final resultados = consulta.isEmpty
        ? <Elemento>[]
        : estado.elementos.where((e) => e.nombre.toLowerCase().contains(consulta.toLowerCase())).toList();
    return DemoCard(
      titulo: 'Barra de búsqueda',
      componente: 'SearchBar',
      descripcion: 'Campo especializado para filtrar contenido. Aquí busca dentro de los elementos de la '
          'lista de la Sección 4.',
      children: [
        SearchBar(
          controller: _controlador,
          hintText: 'Buscar frutas o verduras…',
          leading: const Icon(Icons.search),
          elevation: const WidgetStatePropertyAll(1.0),
          onChanged: (_) => setState(() {}),
          trailing: [
            if (consulta.isNotEmpty)
              IconButton(
                tooltip: 'Borrar búsqueda',
                icon: const Icon(Icons.clear),
                onPressed: () => setState(_controlador.clear),
              ),
          ],
        ),
        if (consulta.isNotEmpty)
          Resultado(resultados.isEmpty
              ? 'Sin resultados para «$consulta»'
              : '${resultados.length} resultado(s): ${resultados.map((e) => e.nombre).join(', ')}'),
      ],
    );
  }
}

class _AgregarALista extends StatefulWidget {
  const _AgregarALista();
  @override
  State<_AgregarALista> createState() => _AgregarAListaState();
}

class _AgregarAListaState extends State<_AgregarALista> {
  final _controlador = TextEditingController();
  bool _error = false;

  @override
  void dispose() {
    _controlador.dispose();
    super.dispose();
  }

  void _agregar() {
    final nombre = _controlador.text.trim();
    if (nombre.isEmpty) {
      setState(() => _error = true);
      return;
    }
    EstadoScope.of(context).agregar(nombre);
    _controlador.clear();
    setState(() => _error = false);
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('«$nombre» se agregó a la Sección 4')),
    );
  }

  @override
  Widget build(BuildContext context) {
    final total = EstadoScope.of(context).elementos.length;
    return DemoCard(
      titulo: 'Conexión con la Sección 4',
      componente: 'TextField + FilledButton + ChangeNotifier compartido',
      descripcion: 'El texto capturado aquí se agrega a la lista compartida que se muestra en la '
          'Sección 4 (Listas y colecciones).',
      children: [
        TextField(
          controller: _controlador,
          textCapitalization: TextCapitalization.sentences,
          textInputAction: TextInputAction.done,
          onSubmitted: (_) => _agregar(),
          onChanged: (_) {
            if (_error) setState(() => _error = false);
          },
          decoration: InputDecoration(
            labelText: 'Nuevo elemento',
            border: const OutlineInputBorder(),
            errorText: _error ? 'Escribe un nombre antes de agregar' : null,
          ),
        ),
        FilledButton.icon(
          onPressed: _agregar,
          icon: const Icon(Icons.add),
          label: const Text('Agregar a la lista'),
        ),
        Resultado('La lista compartida tiene $total elementos'),
      ],
    );
  }
}
