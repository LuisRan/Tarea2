import 'package:flutter_test/flutter_test.dart';

import 'package:catalogo_ui/main.dart';

void main() {
  testWidgets('La pantalla principal muestra las seis secciones', (tester) async {
    await tester.pumpWidget(const CatalogoApp());
    await tester.pumpAndSettle();
    expect(find.text('Catálogo de elementos de interfaz'), findsOneWidget);
    expect(find.text('1. Entrada de texto'), findsOneWidget);
  });
}
