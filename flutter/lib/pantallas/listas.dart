import 'package:flutter/material.dart';

import '../comunes.dart';
import '../estado.dart';

/// Sección 4: Listas y colecciones.
class ListasPantalla extends StatelessWidget {
  const ListasPantalla({super.key});

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    return PantallaSeccion(
      destino: secciones[3],
      intro: 'Las listas muestran colecciones de datos. La lista de esta sección es compartida con la Sección 1.',
      hijos: [
        const _ListaVertical(),
        DemoCard(
          titulo: 'Estado vacío',
          componente: 'Widget condicional (if lista.isEmpty) con Icon + Text',
          descripcion: 'Cuando no hay elementos se muestra un mensaje con una ilustración en lugar de un '
              'espacio en blanco. Pulsa «Vaciar lista» para verlo arriba.',
          children: [
            OutlinedButton(onPressed: estado.vaciar, child: const Text('Vaciar lista')),
          ],
        ),
        const _Cuadricula(),
        const _ListaConEncabezados(),
        const _Pestanas(),
      ],
    );
  }
}

void _mostrarDetalle(BuildContext context, Elemento e) {
  showDialog<void>(
    context: context,
    builder: (context) => AlertDialog(
      title: Text(e.nombre),
      content: Text('Categoría: ${e.categoria}\n\n${e.descripcion}'),
      actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cerrar'))],
    ),
  );
}

class _ListaVertical extends StatelessWidget {
  const _ListaVertical();

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    final tema = Theme.of(context);
    final lista = estado.elementos;

    Future<void> actualizar() async {
      await Future.delayed(const Duration(milliseconds: 1200));
      estado.recargar();
      if (context.mounted) mostrarToast(context, 'Lista actualizada');
    }

    return DemoCard(
      titulo: 'Lista vertical (con detalle, deslizar y actualizar)',
      componente: 'ListView.builder + Dismissible + RefreshIndicator',
      descripcion: 'Muestra muchos elementos de forma eficiente construyendo solo los visibles. Toca un '
          'elemento para ver su detalle, deslízalo a un lado para eliminarlo o arrastra hacia abajo para '
          'actualizar la lista.',
      children: [
        Row(
          children: [
            Expanded(
              child: Text('${lista.length} elementos',
                  style: tema.textTheme.labelLarge?.copyWith(color: tema.colorScheme.primary)),
            ),
            TextButton.icon(
              onPressed: estado.vaciar,
              icon: const Icon(Icons.delete),
              label: const Text('Vaciar lista'),
            ),
          ],
        ),
        Container(
          height: 360,
          clipBehavior: Clip.antiAlias,
          decoration: BoxDecoration(
            border: Border.all(color: tema.colorScheme.outline),
            borderRadius: BorderRadius.circular(12),
          ),
          child: RefreshIndicator(
            onRefresh: actualizar,
            child: lista.isEmpty
                ? _EstadoVacio(alRestaurar: estado.recargar)
                : ListView.separated(
                    physics: const AlwaysScrollableScrollPhysics(),
                    itemCount: lista.length,
                    separatorBuilder: (_, __) => const Divider(height: 1),
                    itemBuilder: (context, i) {
                      final e = lista[i];
                      return Dismissible(
                        key: ValueKey(e.id),
                        background: _FondoEliminar(alineacion: Alignment.centerLeft),
                        secondaryBackground: _FondoEliminar(alineacion: Alignment.centerRight),
                        onDismissed: (_) {
                          estado.eliminar(e);
                          final mensajero = ScaffoldMessenger.of(context);
                          mensajero.hideCurrentSnackBar();
                          mensajero.showSnackBar(SnackBar(
                            content: Text('Eliminaste «${e.nombre}»'),
                            action: SnackBarAction(label: 'Deshacer', onPressed: () => estado.restaurar(e, i)),
                          ));
                        },
                        child: ListTile(
                          tileColor: tema.colorScheme.surfaceContainerLow,
                          leading: CircleAvatar(
                            backgroundColor: tema.colorScheme.primaryContainer,
                            foregroundColor: tema.colorScheme.onPrimaryContainer,
                            child: Text(e.nombre.characters.first.toUpperCase()),
                          ),
                          title: Text(e.nombre),
                          subtitle: Text(e.categoria),
                          trailing: const Icon(Icons.chevron_right),
                          onTap: () => _mostrarDetalle(context, e),
                        ),
                      );
                    },
                  ),
          ),
        ),
      ],
    );
  }
}

class _FondoEliminar extends StatelessWidget {
  const _FondoEliminar({required this.alineacion});

  final Alignment alineacion;

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    return Container(
      color: esquema.error,
      alignment: alineacion,
      padding: const EdgeInsets.symmetric(horizontal: Espacio.l),
      child: Icon(Icons.delete, color: esquema.onError, semanticLabel: 'Eliminar'),
    );
  }
}

class _EstadoVacio extends StatelessWidget {
  const _EstadoVacio({required this.alRestaurar});

  final VoidCallback alRestaurar;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    // Se usa un ListView para que se pueda seguir arrastrando hacia abajo para actualizar.
    return ListView(
      physics: const AlwaysScrollableScrollPhysics(),
      padding: const EdgeInsets.all(Espacio.l),
      children: [
        const SizedBox(height: Espacio.l),
        Icon(Icons.inbox, size: 96, color: tema.colorScheme.outline, semanticLabel: 'No hay elementos'),
        Text('No hay elementos', textAlign: TextAlign.center, style: tema.textTheme.titleMedium),
        Text(
          'Agrega uno desde la Sección 1 o restaura la lista.',
          textAlign: TextAlign.center,
          style: tema.textTheme.bodyMedium?.copyWith(color: tema.colorScheme.onSurfaceVariant),
        ),
        const SizedBox(height: Espacio.m),
        Center(
          child: FilledButton.icon(
            onPressed: alRestaurar,
            icon: const Icon(Icons.refresh),
            label: const Text('Restaurar lista'),
          ),
        ),
      ],
    );
  }
}

class _Cuadricula extends StatefulWidget {
  const _Cuadricula();
  @override
  State<_Cuadricula> createState() => _CuadriculaState();
}

class _CuadriculaState extends State<_Cuadricula> {
  ColorMuestra? _elegido;

  @override
  Widget build(BuildContext context) {
    return DemoCard(
      titulo: 'Cuadrícula',
      componente: 'GridView.count(crossAxisCount: 4)',
      descripcion: 'Organiza los elementos en filas y columnas; es ideal para contenido visual como '
          'colores, fotos o productos.',
      children: [
        GridView.count(
          crossAxisCount: 4,
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          mainAxisSpacing: Espacio.s,
          crossAxisSpacing: Espacio.s,
          childAspectRatio: 0.85,
          children: [
            for (final c in coloresMuestra)
              InkWell(
                borderRadius: BorderRadius.circular(12),
                onTap: () => setState(() => _elegido = c),
                child: Column(
                  children: [
                    Expanded(
                      child: Container(
                        decoration: BoxDecoration(color: c.valor, borderRadius: BorderRadius.circular(12)),
                      ),
                    ),
                    const SizedBox(height: Espacio.xs),
                    Text(c.nombre, style: Theme.of(context).textTheme.labelSmall),
                  ],
                ),
              ),
          ],
        ),
        Resultado(
          _elegido == null ? 'Toca un color.' : 'Elegiste el color: ${_elegido!.nombre}',
          color: _elegido?.valor,
        ),
      ],
    );
  }
}

/// Filas de la lista con encabezados: dos tipos de elemento distintos.
sealed class _Fila {}

class _FilaEncabezado extends _Fila {
  _FilaEncabezado(this.texto);
  final String texto;
}

class _FilaItem extends _Fila {
  _FilaItem(this.elemento);
  final Elemento elemento;
}

class _ListaConEncabezados extends StatelessWidget {
  const _ListaConEncabezados();

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    final grupos = <String, List<Elemento>>{};
    for (final e in EstadoScope.of(context).elementos) {
      grupos.putIfAbsent(e.categoria, () => []).add(e);
    }
    final filas = <_Fila>[
      for (final g in grupos.entries) ...[
        _FilaEncabezado('${g.key} (${g.value.length})'),
        for (final e in g.value) _FilaItem(e),
      ],
    ];
    return DemoCard(
      titulo: 'Lista con encabezados de sección',
      componente: 'ListView.builder con 2 tipos de fila (sealed class)',
      descripcion: 'Combina dos tipos de elemento: encabezados que agrupan y filas de contenido. Aquí se '
          'agrupa la lista compartida por categoría.',
      children: [
        Container(
          height: 300,
          clipBehavior: Clip.antiAlias,
          decoration: BoxDecoration(
            border: Border.all(color: tema.colorScheme.outline),
            borderRadius: BorderRadius.circular(12),
          ),
          child: ListView.builder(
            itemCount: filas.length,
            itemBuilder: (context, i) => switch (filas[i]) {
              _FilaEncabezado(:final texto) => Container(
                  color: tema.colorScheme.secondaryContainer,
                  padding: const EdgeInsets.symmetric(horizontal: Espacio.m, vertical: Espacio.s),
                  child: Text(texto,
                      style: tema.textTheme.labelLarge?.copyWith(color: tema.colorScheme.onSecondaryContainer)),
                ),
              _FilaItem(:final elemento) => InkWell(
                  onTap: () => _mostrarDetalle(context, elemento),
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: Espacio.m, vertical: 12),
                    child: Text(elemento.nombre, style: tema.textTheme.bodyLarge),
                  ),
                ),
            },
          ),
        ),
      ],
    );
  }
}

class _Pestanas extends StatefulWidget {
  const _Pestanas();
  @override
  State<_Pestanas> createState() => _PestanasState();
}

class _PestanasState extends State<_Pestanas> with SingleTickerProviderStateMixin {
  static const _paginas = [
    ('Resumen', 'Esta es la primera página. Desliza hacia la izquierda para ver la siguiente.', Icons.home),
    ('Detalles', 'Segunda página: las pestañas se sincronizan con el deslizamiento.', Icons.info),
    ('Ajustes', 'Tercera página: toca «Resumen» para volver al inicio.', Icons.settings),
  ];
  late final TabController _control = TabController(length: _paginas.length, vsync: this)
    ..addListener(() => setState(() {}));

  @override
  void dispose() {
    _control.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return DemoCard(
      titulo: 'Pestañas con contenido deslizable',
      componente: 'TabBar + TabBarView (TabController)',
      descripcion: 'Dividen contenido relacionado en páginas. Puedes tocar una pestaña o deslizar '
          'horizontalmente para cambiar de página.',
      children: [
        TabBar(
          controller: _control,
          tabs: [for (final p in _paginas) Tab(text: p.$1, icon: Icon(p.$3))],
        ),
        SizedBox(
          height: 180,
          child: TabBarView(
            controller: _control,
            children: [
              for (final p in _paginas)
                Padding(
                  padding: const EdgeInsets.all(Espacio.m),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(p.$3, size: 48, color: tema.colorScheme.primary),
                      Text(p.$1, style: tema.textTheme.titleLarge),
                      Text(p.$2, textAlign: TextAlign.center, style: tema.textTheme.bodyMedium),
                    ],
                  ),
                ),
            ],
          ),
        ),
        Resultado('Página ${_control.index + 1} de ${_paginas.length}'),
      ],
    );
  }
}
