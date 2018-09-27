/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.compass.compiler.util

import com.squareup.kotlinpoet.ClassName

val CLASS_BUNDLE_HOLDER = ClassName("com.ivianuu.compass", "BundleHolder")

val CLASS_DETOUR = ClassName("com.ivianuu.compass", "CompassDetour")
val CLASS_ACTIVITY_DETOUR = ClassName("com.ivianuu.compass.android", "ActivityDetour")
val CLASS_DIRECTOR_CONTROLLER_DETOUR = ClassName("com.ivianuu.compass.director", "ControllerDetour")
val CLASS_FRAGMENT_DETOUR = ClassName("com.ivianuu.compass.fragment", "FragmentDetour")

val CLASS_DETOUR_PROVIDER = ClassName("com.ivianuu.compass", "CompassDetourProvider")

val CLASS_ROUTE_FACTORY = ClassName("com.ivianuu.compass", "CompassRouteFactory")
val CLASS_ROUTE_FACTORY_PROVIDER = ClassName("com.ivianuu.compass", "CompassRouteFactoryProvider")
val CLASS_ACTIVITY_ROUTE_FACTORY = ClassName("com.ivianuu.compass.android", "ActivityRouteFactory")
val CLASS_FRAGMENT_ROUTE_FACTORY = ClassName("com.ivianuu.compass.fragment", "FragmentRouteFactory")
val CLASS_DIRECTOR_CONTROLLER_ROUTE_FACTORY =
    ClassName("com.ivianuu.compass.director", "ControllerRouteFactory")

val CLASS_SERIALIZER = ClassName("com.ivianuu.compass", "CompassSerializer")
val CLASS_SERIALIZER_PROVIDER = ClassName("com.ivianuu.compass", "CompassSerializerProvider")

val CLASS_BUNDLE = ClassName("android.os", "Bundle")
val CLASS_INTENT = ClassName("android.content", "Intent")

val CLASS_ACTIVITY = ClassName("android.app", "Activity")
val CLASS_FRAGMENT = ClassName("androidx.fragment.app", "Fragment")
val CLASS_DIRECTOR_CONTROLLER = ClassName("com.ivianuu.director", "Controller")

val CLASS_CONTEXT = ClassName("android.content", "Context")