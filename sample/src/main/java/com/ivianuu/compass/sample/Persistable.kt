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

package com.ivianuu.compass.sample

/**
fun Bundle.putDestination(destination: Any) {
val clazz = destination::class

props
.map { it as KProperty1<Any, Any?> }
.forEach { putProperty(it, it.get(destination)) }
}

fun Bundle.putProperty(property: KProperty1<out Any, Any?>, value: Any?) {
Log.d("testt", "${property.returnType}")
}

fun <T> Bundle.getAuto(key: String) = get(key) as T

/**
fun KClass<*>?.isPersistable(): Boolean {
return when(this) {
null -> true

Boolean::class -> true
Byte::class -> true
Char::class -> true
Double::class -> true
Float::class -> true
Int::class -> true
Long::class -> true
Short::class -> true

Bundle::class -> true
CharSequence::class -> true
Parcelable::class -> true

BooleanArray::class -> true
ByteArray::class -> true
CharArray::class -> true
DoubleArray::class -> true
FloatArray::class -> true
IntArray::class -> true
LongArray::class -> true
ShortArray::class -> true
Array<Any>::class -> {
val componentType = this::class.java.componentType!!
@Suppress("UNCHECKED_CAST") // Checked by reflection.
when {
Parcelable::class.java.isAssignableFrom(componentType) -> true
String::class.java.isAssignableFrom(componentType) -> true
CharSequence::class.java.isAssignableFrom(componentType) -> true
Serializable::class.java.isAssignableFrom(componentType) -> true
else -> {
val valueType = componentType.canonicalName
throw IllegalArgumentException(
"Illegal value array type $valueType for key \"$key\"")
}
}
}
else -> false
}
}

// Last resort. Also we must check this after Array<*> as all arrays are serializable.
is Serializable -> putSerializable(key, value)

else -> {
if (Build.VERSION.SDK_INT >= 18 && value is Binder) {
putBinder(key, value)
} else if (Build.VERSION.SDK_INT >= 21 && value is Size) {
putSize(key, value)
} else if (Build.VERSION.SDK_INT >= 21 && value is SizeF) {
putSizeF(key, value)
} else {
val valueType = value.javaClass.canonicalName
throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
}
}
 */