package com.ivianuu.compass

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/**
 * Returns a new [Intent] associated with [this] or throws
 */
fun <D : Any> D.intent(context: Context): Intent {
    val routeFactory = activityRouteFactory()
    val intent = routeFactory.createActivityIntent(context, this)
    serializerOrNull()?.toBundle(this)?.let { intent.putExtras(it) }
    return intent
}

/**
 * Returns a new [Intent] associated with [this] or throws
 */
fun <D : Any> D.intentOrNull(context: Context) = try {
    intent(context)
} catch (e: Exception) {
    null
}

/**
 * Returns a new [Fragment] associated with [this]
 */
fun <D : Any> D.fragment(): Fragment {
    val routeFactory = fragmentRouteFactory()
    val fragment = routeFactory.createFragment(this)
    serializerOrNull()?.toBundle(this)?.let { fragment.arguments = it }
    return fragment
}

/**
 * Returns a new [Fragment] associated with [this] or null
 */
fun <D : Any> D.fragmentOrNull() = try {
    fragment()
} catch (e: Exception) {
    null
}

/**
 * Returns a new [F] associated this [this]
 */
inline fun <D : Any, reified F : Fragment> D.fragment(fragmentClass: KClass<F> = F::class) =
    fragmentClass.java.cast(fragment())!!

/**
 * Returns a new [F] associated with [this]
 */
inline fun <D : Any, reified F : Fragment> D.fragmentOrNull(fragmentClass: KClass<F> = F::class) =
    try {
        fragment(fragmentClass)
    } catch (e: Exception) {
        null
    }