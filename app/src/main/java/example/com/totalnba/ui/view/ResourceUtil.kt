package example.com.totalnba.ui.view

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes

object ResourceUtil {

    @JvmStatic
    fun getResIdForAttribute(context: Context, @AttrRes attribute: Int): Int {
        val typedValue = TypedValue()

        val theme = context.theme
        theme.resolveAttribute(attribute, typedValue, true)

        return typedValue.resourceId
    }
}

val Int.f: Float
    get() = toFloat()

fun Resources.dp(toPx: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    toPx,
    displayMetrics
)

fun Resources.dp(toPx: Int): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    toPx.f,
    displayMetrics
)

fun Context.dp(toPx: Int) = resources.dp(toPx)

fun Context.dp(toPx: Float) = resources.dp(toPx)

fun View.dp(toPx: Int) = context.dp(toPx)

fun View.dp(toPx: Float) = context.dp(toPx)