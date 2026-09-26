import 'package:flutter/material.dart';

/// Un elemento de la lista compartida entre la Sección 1 y la Sección 4.
class Elemento {
  const Elemento(this.id, this.nombre, this.categoria, this.descripcion);

  final int id;
  final String nombre;
  final String categoria;
  final String descripcion;
}

/// Color mostrado en la cuadrícula de la Sección 4.
class ColorMuestra {
  const ColorMuestra(this.nombre, this.valor);

  final String nombre;
  final Color valor;
}

const frutas = 'Frutas';
const verduras = 'Verduras';
const agregadosPorTi = 'Agregados por ti';

const List<Elemento> elementosIniciales = [
  Elemento(1, 'Manzana', frutas, 'Fruta crujiente rica en fibra; se cultiva en Chihuahua y Coahuila.'),
  Elemento(2, 'Plátano', frutas, 'Fuente de potasio, ideal como colación rápida.'),
  Elemento(3, 'Mango', frutas, 'Fruta tropical dulce; México es uno de sus principales exportadores.'),
  Elemento(4, 'Fresa', frutas, 'Pequeña fruta roja con alto contenido de vitamina C.'),
  Elemento(5, 'Uva', frutas, 'Crece en racimos; se consume fresca o en pasas.'),
  Elemento(6, 'Piña', frutas, 'Fruta tropical ácida y dulce que contiene bromelina.'),
  Elemento(7, 'Sandía', frutas, 'Contiene más de 90 % de agua; muy refrescante.'),
  Elemento(8, 'Naranja', frutas, 'Cítrico jugoso, clásico en el desayuno.'),
  Elemento(9, 'Papaya', frutas, 'Fruta suave de pulpa anaranjada que favorece la digestión.'),
  Elemento(10, 'Guayaba', frutas, 'Aromática y rica en vitamina C; base del ponche navideño.'),
  Elemento(11, 'Zanahoria', verduras, 'Raíz anaranjada rica en betacaroteno.'),
  Elemento(12, 'Jitomate', verduras, 'Base de salsas y guisos de la cocina mexicana.'),
  Elemento(13, 'Brócoli', verduras, 'Verdura verde con mucha fibra y vitamina K.'),
  Elemento(14, 'Calabaza', verduras, 'Se aprovechan su fruto, flor y semillas.'),
  Elemento(15, 'Chayote', verduras, 'Verdura de sabor suave originaria de Mesoamérica.'),
  Elemento(16, 'Espinaca', verduras, 'Hoja verde con hierro y ácido fólico.'),
  Elemento(17, 'Lechuga', verduras, 'Hoja fresca, base de muchas ensaladas.'),
  Elemento(18, 'Nopal', verduras, 'Cactácea emblemática de México, rica en fibra.'),
  Elemento(19, 'Pepino', verduras, 'Refrescante y bajo en calorías.'),
  Elemento(20, 'Elote', verduras, 'Mazorca tierna de maíz; se come asado o hervido.'),
];

const List<ColorMuestra> coloresMuestra = [
  ColorMuestra('Rojo', Color(0xFFE53935)),
  ColorMuestra('Naranja', Color(0xFFFB8C00)),
  ColorMuestra('Ámbar', Color(0xFFFFB300)),
  ColorMuestra('Lima', Color(0xFFC0CA33)),
  ColorMuestra('Verde', Color(0xFF43A047)),
  ColorMuestra('Turquesa', Color(0xFF00897B)),
  ColorMuestra('Cian', Color(0xFF00ACC1)),
  ColorMuestra('Azul', Color(0xFF1E88E5)),
  ColorMuestra('Índigo', Color(0xFF3949AB)),
  ColorMuestra('Violeta', Color(0xFF8E24AA)),
  ColorMuestra('Rosa', Color(0xFFD81B60)),
  ColorMuestra('Café', Color(0xFF6D4C41)),
];

/// Estado compartido por todas las pantallas.
/// - [elementos] conecta la Sección 1 (captura) con la Sección 4 (listas).
/// - [tamanoTexto] conecta la Sección 3 (deslizador) con la Sección 5 (textos).
class CatalogoEstado extends ChangeNotifier {
  final List<Elemento> _agregados = [];
  final List<Elemento> _elementos = List.of(elementosIniciales);
  int _siguienteId = 1000;
  double _tamanoTexto = 18;

  List<Elemento> get elementos => List.unmodifiable(_elementos);
  double get tamanoTexto => _tamanoTexto;

  void agregar(String nombre) {
    final nuevo = Elemento(
      _siguienteId++,
      nombre.trim(),
      agregadosPorTi,
      'Elemento capturado en la Sección 1 (Entrada de texto).',
    );
    _agregados.add(nuevo);
    _elementos.insert(0, nuevo);
    notifyListeners();
  }

  void eliminar(Elemento elemento) {
    _elementos.removeWhere((e) => e.id == elemento.id);
    notifyListeners();
  }

  void restaurar(Elemento elemento, int posicion) {
    if (_elementos.any((e) => e.id == elemento.id)) return;
    _elementos.insert(posicion.clamp(0, _elementos.length).toInt(), elemento);
    notifyListeners();
  }

  void vaciar() {
    _elementos.clear();
    notifyListeners();
  }

  /// Recarga la lista original más lo agregado desde la Sección 1.
  void recargar() {
    _elementos
      ..clear()
      ..addAll(_agregados.reversed)
      ..addAll(elementosIniciales);
    notifyListeners();
  }

  void cambiarTamanoTexto(double sp) {
    if (sp == _tamanoTexto) return;
    _tamanoTexto = sp;
    notifyListeners();
  }
}

/// Hace disponible el estado compartido en todo el árbol de widgets.
class EstadoScope extends InheritedNotifier<CatalogoEstado> {
  const EstadoScope({super.key, required CatalogoEstado estado, required super.child})
      : super(notifier: estado);

  static CatalogoEstado of(BuildContext context) =>
      context.dependOnInheritedWidgetOfExactType<EstadoScope>()!.notifier!;
}
