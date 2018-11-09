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

import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.Size
import android.util.SizeF
import java.io.Serializable

fun bundleOf(vararg pairs: Pair<String, Any?>) = Bundle(pairs.size).apply {
    for ((key, value) in pairs) {
        when (value) {
            null -> putString(key, null).d { "string" } // Any nullable type will suffice.

            // Scalars
            is Boolean -> putBoolean(key, value).d { "boolean" }
            is Byte -> putByte(key, value).d { "byte" }
            is Char -> putChar(key, value).d { "char" }
            is Double -> putDouble(key, value).d { "double" }
            is Float -> putFloat(key, value).d { "float" }
            is Int -> putInt(key, value).d { "int" }
            is Long -> putLong(key, value).d { "long" }
            is Short -> putShort(key, value).d { "short" }

            // References
            is Bundle -> putBundle(key, value).d { "bundle" }
            is CharSequence -> putCharSequence(key, value).d { "char sequence" }
            is Parcelable -> putParcelable(key, value).d { "parcelable" }

            // Scalar arrays
            is BooleanArray -> putBooleanArray(key, value).d { "boolean array" }
            is ByteArray -> putByteArray(key, value).d { "byte array" }
            is CharArray -> putCharArray(key, value).d { "char array" }
            is DoubleArray -> putDoubleArray(key, value).d { "double array" }
            is FloatArray -> putFloatArray(key, value).d { "float array" }
            is IntArray -> putIntArray(key, value).d { "int array" }
            is LongArray -> putLongArray(key, value).d { "long array" }
            is ShortArray -> putShortArray(key, value).d { "short array" }

            // Reference arrays
            is Array<*> -> {
                val componentType = value::class.java.componentType!!
                @Suppress("UNCHECKED_CAST") // Checked by reflection.
                when {
                    Parcelable::class.java.isAssignableFrom(componentType) -> {
                        putParcelableArray(key, value as Array<Parcelable>)
                            .d { "parcelable array" }
                    }
                    String::class.java.isAssignableFrom(componentType) -> {
                        putStringArray(key, value as Array<String>)
                            .d { "string array" }
                    }
                    CharSequence::class.java.isAssignableFrom(componentType) -> {
                        putCharSequenceArray(key, value as Array<CharSequence>)
                            .d { "char sequence array" }
                    }
                    Serializable::class.java.isAssignableFrom(componentType) -> {
                        putSerializable(key, value)
                            .d { "serializable array" }
                    }
                    else -> {
                        val valueType = componentType.canonicalName
                        throw IllegalArgumentException(
                            "Illegal value array type $valueType for key \"$key\""
                        )
                    }
                }
            }

            // Last resort. Also we must check this after Array<*> as all arrays are serializable.
            is Serializable -> putSerializable(key, value)
                .d { "serializable" }

            else -> {
                if (Build.VERSION.SDK_INT >= 18 && value is Binder) {
                    putBinder(key, value)
                        .d { "binder" }
                } else if (Build.VERSION.SDK_INT >= 21 && value is Size) {
                    putSize(key, value)
                        .d { "size" }
                } else if (Build.VERSION.SDK_INT >= 21 && value is SizeF) {
                    putSizeF(key, value)
                        .d { "size f" }
                } else {
                    val valueType = value.javaClass.canonicalName
                    throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
                }
            }
        }
    }
}

private fun Unit.d(m: () -> String) {
    Log.d("testt", "putted ${m()}")
}