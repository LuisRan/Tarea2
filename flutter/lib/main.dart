import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

import 'comunes.dart';
import 'estado.dart';
import 'pantallas/botones.dart';
import 'pantallas/contenedores.dart';
import 'pantallas/entrada.dart';
import 'pantallas/informacion.dart';
import 'pantallas/inicio.dart';
import 'pantallas/listas.dart';
import 'pantallas/seleccion.dart';

void main() {
  runApp(const CatalogoApp());
}

/// Color semilla compartido con las versiones de Android (#006A60).
const semilla = Color(0xFF006A60);

class CatalogoApp extends StatefulWidget {
  const CatalogoApp({super.key});

  @override
  State<CatalogoApp> createState() => _CatalogoAppState();
}

class _CatalogoAppState extends State<CatalogoApp> {
  final _estado = CatalogoEstado();

  @override
  void dispose() {
    _estado.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return EstadoScope(
      estado: _estado,
      child: MaterialApp(
        title: 'Catálogo UI · Flutter',
        debugShowCheckedModeBanner: false,
        // Tema claro y oscuro: se elige automáticamente según el sistema.
        themeMode: ThemeMode.system,
        theme: ThemeData(colorScheme: ColorScheme.fromSeed(seedColor: semilla)),
        darkTheme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: semilla, brightness: Brightness.dark),
        ),
        // Textos del sistema (calendario, reloj, botones) en español.
        locale: const Locale('es', 'MX'),
        supportedLocales: const [Locale('es', 'MX'), Locale('es')],
        localizationsDelegates: GlobalMaterialLocalizations.delegates,
        initialRoute: inicio.ruta,
        routes: {
          inicio.ruta: (_) => const InicioPantalla(),
          secciones[0].ruta: (_) => const EntradaPantalla(),
          secciones[1].ruta: (_) => const BotonesPantalla(),
          secciones[2].ruta: (_) => const SeleccionPantalla(),
          secciones[3].ruta: (_) => const ListasPantalla(),
          secciones[4].ruta: (_) => const InformacionPantalla(),
          secciones[5].ruta: (_) => const ContenedoresPantalla(),
        },
      ),
    );
  }
}
