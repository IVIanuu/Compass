package com.ivianuu.compass

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

fun <D : Any> D.intent(context: Context): Intent {
    val routeFactory = activityRouteFactory()
    val intent = routeFactory.createActivityIntent(context, this)
    serializerOrNull()?.let { intent.putExtras(it.toBundle(this)) }
    return intent
}

fun <D : Any> D.intentOrNull(context: Context) = try {
    intent(context)
} catch (e: Exception) {
    null
}

fun <D : Any> D.fragment(): Fragment {
    val routeFactory = fragmentRouteFactory()
    val fragment = routeFactory.createFragment(this)
    fragment.arguments = serializerOrNull()?.toBundle(this)
    return fragment
}

fun <D : Any> D.fragmentOrNull() = try {
    fragment()
} catch (e: Exception) {
    null
}

inline fun <D : Any, reified F : Fragment> D.fragment(fragmentClass: KClass<F> = F::class) =
    fragmentClass.java.cast(fragment())!!

inline fun <D : Any, reified F : Fragment> D.fragmentOrNull(fragmentClass: KClass<F> = F::class) =
    try {
        fragment(fragmentClass)
    } catch (e: Exception) {
        null
    }