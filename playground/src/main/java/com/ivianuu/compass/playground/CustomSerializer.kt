package com.ivianuu.compass.playground

import android.os.Bundle
import com.ivianuu.compass.CompassSerializer
import com.ivianuu.compass.Destination
import com.ivianuu.compass.Serializer

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Serializer(MyCustomSerializer::class)
@Destination(DummyFragment::class)
data class CustomSerializerDestination(
    val string: String,
    val title: CharSequence
)

object MyCustomSerializer : CompassSerializer<CustomSerializerDestination> {
    override fun fromBundle(bundle: Bundle): CustomSerializerDestination {
        return CustomSerializerDestination("", "")
    }

    override fun toBundle(destination: CustomSerializerDestination, bundle: Bundle) {

    }
}