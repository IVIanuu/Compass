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

import androidx.fragment.app.Fragment
import com.ivianuu.compass.Destination

@Destination(PrimitiveFragment::class)
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

class PrimitiveFragment : Fragment()

@Destination(NullablePrimitiveFragment::class)
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

class NullablePrimitiveFragment : Fragment()

@Destination(PrimitiveArrayFragment::class)
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

class PrimitiveArrayFragment : Fragment() {
    init {
        PrimitiveArrayDestination__Serializer
    }
}

@Destination(NullablePrimitiveArrayFragment::class)
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

class NullablePrimitiveArrayFragment : Fragment() {
    init {
        NullablePrimitiveArrayDestination__Serializer
    }
}