package mx.ipn.catalogo.views.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.color.MaterialColors
import mx.ipn.catalogo.views.R

/**
 * Tarjeta de documentación reutilizable. Muestra el nombre del elemento,
 * el componente usado y una explicación; los hijos declarados en XML
 * se agregan debajo como la demostración interactiva.
 */
class DemoCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    init {
        orientation = VERTICAL
        val espacio = resources.getDimensionPixelSize(R.dimen.espacio_m)
        setPadding(espacio, espacio, espacio, espacio)
        setBackgroundResource(R.drawable.bg_demo_card)

        val a = context.obtainStyledAttributes(attrs, R.styleable.DemoCard)
        val titulo = a.getString(R.styleable.DemoCard_titulo).orEmpty()
        val descripcion = a.getString(R.styleable.DemoCard_descripcion).orEmpty()
        val componente = a.getString(R.styleable.DemoCard_componente)
        a.recycle()

        addView(TextView(context).apply {
            text = titulo
            setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium)
            setTextColor(MaterialColors.getColor(this@DemoCard, com.google.android.material.R.attr.colorOnSurface))
        })
        if (!componente.isNullOrBlank()) {
            addView(TextView(context).apply {
                text = componente
                setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelMedium)
                setTextColor(MaterialColors.getColor(this@DemoCard, com.google.android.material.R.attr.colorPrimary))
                setPadding(0, espacio / 4, 0, 0)
            })
        }
        addView(TextView(context).apply {
            text = descripcion
            setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium)
            setTextColor(MaterialColors.getColor(this@DemoCard, com.google.android.material.R.attr.colorOnSurfaceVariant))
            setPadding(0, espacio / 4, 0, espacio / 2)
        })
    }
}
