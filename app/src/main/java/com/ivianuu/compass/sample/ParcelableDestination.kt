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

import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.SparseArray
import com.ivianuu.compass.Destination
import com.ivianuu.compass.FragmentDetour
import kotlinx.android.parcel.Parcelize

/**
 * @author Manuel Wrage (IVIanuu)
 */

//@Detour(ParcelableDetour::class)
@Destination(ParcelableFragment::class)
data class ParcelableDestination(val somethingParcelable: SomethingParcelable)

class ParcelableFragment : Fragment()

@Destination(ParcelableListFragment::class)
data class ParcelableListDestination(val listt: List<SomethingParcelable>)

@Destination(ParcelableArrayFragment::class)
data class ParcelableArrayDestination(val arrayy: Array<SomethingParcelable>)

@Destination(ParcelableSparseArrayFragment::class)
data class ParcelabelSparseArrayDestination(val arrayy: SparseArray<SomethingParcelable>)

class ParcelableArrayFragment : Fragment()

class ParcelableListFragment : Fragment()

class ParcelableSparseArrayFragment : Fragment()

class ParcelableDetour : FragmentDetour<ParcelableDestination> {
    override fun setupTransaction(
        destination: ParcelableDestination,
        currentFragment: Fragment?,
        nextFragment: Fragment,
        transaction: FragmentTransaction
    ) {
    }
}

@Parcelize
data class SomethingParcelable(val hehe: String) : Parcelable