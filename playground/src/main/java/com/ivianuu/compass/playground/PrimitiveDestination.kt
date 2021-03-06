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

package com.ivianuu.compass.playground

import com.ivianuu.compass.Destination

@Destination(DummyFragment::class)
data class PrimitiveDestination(
    val bool: Boolean,
    val bytee: Byte,
    val charr: Char,
    val doublee: Double,
    val floatt: Float,
    val integerr: Int,
    val longg: Long,
    val shortt: Short
)

@Destination(DummyFragment::class)
data class NullablePrimitiveDestination(
    val bool: Boolean?,
    val bytee: Byte?,
    val charr: Char?,
    val doublee: Double?,
    val floatt: Float?,
    val integerr: Int?,
    val lonng: Long?,
    val shortt: Short?
)

@Destination(DummyFragment::class)
data class PrimitiveArrayDestination(
    val booleann: BooleanArray,
    val bytee: ByteArray,
    val charr: CharArray,
    val doublee: DoubleArray,
    val floatt: FloatArray,
    val integerr: IntArray,
    val longg: LongArray,
    val shortt: ShortArray
)

@Destination(DummyFragment::class)
data class NullablePrimitiveArrayDestination(
    val booleann: BooleanArray?,
    val bytee: ByteArray?,
    val charr: CharArray?,
    val doublee: DoubleArray?,
    val floatt: FloatArray?,
    val integerr: IntArray?,
    val longg: LongArray?,
    val shortt: ShortArray?
)

@Destination(DummyFragment::class)
data class PrimitiveListDestination(
    val bool: List<Boolean>,
    val bytee: List<Byte>,
    val charr: List<Char>,
    val doublee: List<Double>,
    val floatt: List<Float>,
    val integerr: List<Int>,
    val longg: List<Long>,
    val shortt: List<Short>,
    val string: List<String>,
    val charSequence: List<String>
)

@Destination(DummyFragment::class)
data class NullablePrimitiveListDestination(
    val bool: List<Boolean>?,
    val bytee: List<Byte>?,
    val charr: List<Char>?,
    val doublee: List<Double>?,
    val floatt: List<Float>?,
    val integerr: List<Int>?,
    val longg: List<Long>?,
    val shortt: List<Short>?,
    val string: List<String>?,
    val charSequence: List<String>?
)