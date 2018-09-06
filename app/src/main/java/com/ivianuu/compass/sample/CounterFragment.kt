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

import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ivianuu.compass.Destination
import com.ivianuu.compass.Detour
import com.ivianuu.compass.FragmentDetour
import kotlinx.android.synthetic.main.fragment_counter.*

@Detour(CounterFragmentDetour::class)
@Destination(CounterFragment::class)
data class CounterDestination(val count: Int, val color: Int)

fun CounterDestination.increment() =
    copy(count = count + 1)

object CounterFragmentDetour : FragmentDetour<CounterDestination> {
    override fun setupTransaction(
        destination: CounterDestination,
        currentFragment: Fragment?,
        nextFragment: Fragment,
        transaction: FragmentTransaction
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            currentFragment?.exitTransition = Slide(Gravity.START)
            currentFragment?.reenterTransition = Slide(Gravity.START)
            nextFragment.enterTransition = Slide(Gravity.END)
            nextFragment.returnTransition = Slide(Gravity.END)
        }
    }
}

/**
 * @author Manuel Wrage (IVIanuu)
 */
class CounterFragment : Fragment() {

    private val router
        get() = (requireActivity() as MainActivity).router

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_counter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val destination = counterDestination()

        title.text = "Count: ${destination.count}"
        view.setBackgroundColor(destination.color)

        up.setOnClickListener { router.navigateTo(destination.increment()) }
        down.setOnClickListener { router.exit() }
    }
}