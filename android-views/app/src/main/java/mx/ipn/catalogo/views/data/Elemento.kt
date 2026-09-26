package mx.ipn.catalogo.views.data

/** Un elemento de la lista compartida entre la Sección 1 y la Sección 4. */
data class Elemento(
    val id: Long,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
)

/** Color mostrado en la cuadrícula de la Sección 4. */
data class ColorMuestra(val nombre: String, val valor: Int)

object DatosCatalogo {
    const val FRUTAS = "Frutas"
    const val VERDURAS = "Verduras"
    const val AGREGADOS = "Agregados por ti"

    fun iniciales(): List<Elemento> = listOf(
        Elemento(1, "Manzana", FRUTAS, "Fruta crujiente rica en fibra; se cultiva en Chihuahua y Coahuila."),
        Elemento(2, "Plátano", FRUTAS, "Fuente de potasio, ideal como colación rápida."),
        Elemento(3, "Mango", FRUTAS, "Fruta tropical dulce; México es uno de sus principales exportadores."),
        Elemento(4, "Fresa", FRUTAS, "Pequeña fruta roja con alto contenido de vitamina C."),
        Elemento(5, "Uva", FRUTAS, "Crece en racimos; se consume fresca o en pasas."),
        Elemento(6, "Piña", FRUTAS, "Fruta tropical ácida y dulce que contiene bromelina."),
        Elemento(7, "Sandía", FRUTAS, "Contiene más de 90 % de agua; muy refrescante."),
        Elemento(8, "Naranja", FRUTAS, "Cítrico jugoso, clásico en el desayuno."),
        Elemento(9, "Papaya", FRUTAS, "Fruta suave de pulpa anaranjada que favorece la digestión."),
        Elemento(10, "Guayaba", FRUTAS, "Aromática y rica en vitamina C; base del ponche navideño."),
        Elemento(11, "Zanahoria", VERDURAS, "Raíz anaranjada rica en betacaroteno."),
        Elemento(12, "Jitomate", VERDURAS, "Base de salsas y guisos de la cocina mexicana."),
        Elemento(13, "Brócoli", VERDURAS, "Verdura verde con mucha fibra y vitamina K."),
        Elemento(14, "Calabaza", VERDURAS, "Se aprovechan su fruto, flor y semillas."),
        Elemento(15, "Chayote", VERDURAS, "Verdura de sabor suave originaria de Mesoamérica."),
        Elemento(16, "Espinaca", VERDURAS, "Hoja verde con hierro y ácido fólico."),
        Elemento(17, "Lechuga", VERDURAS, "Hoja fresca, base de muchas ensaladas."),
        Elemento(18, "Nopal", VERDURAS, "Cactácea emblemática de México, rica en fibra."),
        Elemento(19, "Pepino", VERDURAS, "Refrescante y bajo en calorías."),
        Elemento(20, "Elote", VERDURAS, "Mazorca tierna de maíz; se come asado o hervido."),
    )

    val colores = listOf(
        ColorMuestra("Rojo", 0xFFE53935.toInt()),
        ColorMuestra("Naranja", 0xFFFB8C00.toInt()),
        ColorMuestra("Ámbar", 0xFFFFB300.toInt()),
        ColorMuestra("Lima", 0xFFC0CA33.toInt()),
        ColorMuestra("Verde", 0xFF43A047.toInt()),
        ColorMuestra("Turquesa", 0xFF00897B.toInt()),
        ColorMuestra("Cian", 0xFF00ACC1.toInt()),
        ColorMuestra("Azul", 0xFF1E88E5.toInt()),
        ColorMuestra("Índigo", 0xFF3949AB.toInt()),
        ColorMuestra("Violeta", 0xFF8E24AA.toInt()),
        ColorMuestra("Rosa", 0xFFD81B60.toInt()),
        ColorMuestra("Café", 0xFF6D4C41.toInt()),
    )
}
