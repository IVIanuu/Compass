package com.ivianuu.compass.sample

import android.content.Context
import android.content.Intent
import com.ivianuu.compass.ActivityRouteFactory
import com.ivianuu.compass.Destination
import com.ivianuu.compass.DoNotSerialize
import com.ivianuu.compass.RouteFactory

/**
 * Key for a simple intent
 */
@DoNotSerialize
@RouteFactory(IntentDestination.RouteFactory::class)
@Destination
class IntentDestination(val intent: Intent) {
    class RouteFactory : ActivityRouteFactory<IntentDestination> {
        override fun createActivityIntent(
            context: Context,
            destination: IntentDestination
        ): Intent = Intent(destination.intent)
    }
}