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

import android.os.Parcelable
import android.util.SparseArray
import com.ivianuu.compass.Destination

@Destination(DummyFragment::class)
data class ParcelableDestination(val somethingParcelable: SomethingParcelable)

@Destination(DummyFragment::class)
data class ParcelableListDestination(val list: List<SomethingParcelable>)

@Destination(DummyFragment::class)
data class ParcelableArrayListDestination(val arrayList: ArrayList<SomethingParcelable>)

@Destination(DummyFragment::class)
data class ParcelableArrayDestination(val array: Array<SomethingParcelable>)

@Destination(DummyFragment::class)
data class ParcelableSparseArrayDestination(val array: SparseArray<SomethingParcelable>)

@Parcelize
data class SomethingParcelable(val hehe: String) : Parcelable