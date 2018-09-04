@file:Suppress("UNCHECKED_CAST")

package com.ivianuu.compass

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

inline fun <T : Any> T.requireDetour() =
    Compass.requireDetour(this::class)

inline fun <T : Any> T.getDetour() =
    Compass.getDetour(this::class)

inline fun <T : Any> T.requireActivityDetour() = requireDetour() as ActivityDetour<T>

inline fun <T : Any> T.getActivityDetour() =
    getDetour() as? ActivityDetour<T>

inline fun <T : Any> T.requireFragmentDetour() = requireDetour() as FragmentDetour<T>

inline fun <T : Any> T.getFragmentDetour() =
    getDetour() as? FragmentDetour<T>

inline fun <T : Any> T.requireRouteFactory() =
    Compass.requireRouteFactory(this::class)

inline fun <T : Any> T.getRouteFactory() =
    Compass.getRouteFactory(this::class)

inline fun <T : Any> T.requireActivityRouteFactory() =
    getActivityRouteFactory()
        ?: throw IllegalArgumentException("no activity route factory found for ${this::class}")

inline fun <T : Any> T.getActivityRouteFactory() =
    getRouteFactory() as? ActivityRouteFactory<T>

inline fun <T : Any> T.requireFragmentRouteFactory() =
    getFragmentRouteFactory()
        ?: throw IllegalArgumentException("no fragment route factory found for ${this::class}")

inline fun <T : Any> T.getFragmentRouteFactory() =
    getRouteFactory() as? FragmentRouteFactory<T>

inline fun <T : Any> T.requireSerializer() =
    Compass.requireSerializer(this::class)

inline fun <T : Any> T.getSerializer() =
    Compass.getSerializer(this::class)

inline fun <T : Any> T.requireIntent(context: Context) =
    getIntent(context) ?: throw IllegalArgumentException("no intent found for $this")

fun <T : Any> T.getIntent(context: Context): Intent? {
    val routeFactory =
        Compass.getRouteFactory(this::class)
                as? ActivityRouteFactory<T>
            ?: return null

    val intent = routeFactory.createActivityIntent(context, this)

    val serializer = Compass.getSerializer(this::class)
    if (serializer != null) {
        intent.putExtras(serializer.toBundle(this))
    }

    return intent
}

inline fun <T : Any> T.requireFragment() =
    getFragment() ?: throw IllegalStateException("no fragment found for $this")

fun <T : Any> T.getFragment(): Fragment? {
    val routeFactory =
        Compass.getRouteFactory(this::class)
                as? FragmentRouteFactory<T>
            ?: return null

    val fragment = routeFactory.createFragment(this)

    val serializer = Compass.getSerializer(this::class)
    if (serializer != null) {
        fragment.arguments = serializer.toBundle(this)
    }

    return fragment
}