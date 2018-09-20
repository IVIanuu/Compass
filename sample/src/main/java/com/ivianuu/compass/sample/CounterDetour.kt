package com.ivianuu.compass.sample

import android.os.Build
import android.transition.Slide
import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ivianuu.compass.fragment.FragmentDetour

object CounterFragmentDetour : FragmentDetour<CounterDestination> {
    override fun setupTransaction(
        destination: CounterDestination,
        data: Any?,
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