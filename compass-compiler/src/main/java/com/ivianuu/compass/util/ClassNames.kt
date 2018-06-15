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

package com.ivianuu.compass.util

import com.squareup.kotlinpoet.ClassName

val CLASS_TYPE_UTIL = ClassName.bestGuess("com.ivianuu.compass.util.TypeUtil")

val CLASS_DETOUR = ClassName.bestGuess("com.ivianuu.compass.CompassDetour")
val CLASS_ACTIVITY_DETOUR = ClassName.bestGuess("com.ivianuu.compass.ActivityDetour")
val CLASS_FRAGMENT_DETOUR = ClassName.bestGuess("com.ivianuu.compass.FragmentDetour")

val CLASS_DETOUR_PROVIDER = ClassName.bestGuess("com.ivianuu.compass.CompassDetourProvider")

val CLASS_ROUTE_FACTORY = ClassName.bestGuess("com.ivianuu.compass.CompassRouteFactory")
val CLASS_ROUTE_FACTORY_PROVIDER = ClassName.bestGuess("com.ivianuu.compass.CompassRouteFactoryProvider")
val CLASS_ACTIVITY_ROUTE_FACTORY = ClassName.bestGuess("com.ivianuu.compass.ActivityRouteFactory")
val CLASS_FRAGMENT_ROUTE_FACTORY = ClassName.bestGuess("com.ivianuu.compass.FragmentRouteFactory")

val CLASS_SERIALIZER = ClassName.bestGuess("com.ivianuu.compass.CompassSerializer")
val CLASS_SERIALIZER_PROVIDER = ClassName.bestGuess("com.ivianuu.compass.CompassSerializerProvider")

val CLASS_BUNDLE = ClassName.bestGuess("android.os.Bundle")