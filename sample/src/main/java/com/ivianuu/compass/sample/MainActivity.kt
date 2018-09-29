package com.ivianuu.compass.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivianuu.compass.Destination
import com.ivianuu.compass.fragment.CompassFragmentNavigator
import com.ivianuu.traveler.Traveler
import com.ivianuu.traveler.lifecycle.setNavigator
import com.ivianuu.traveler.setRoot

@Destination(MainActivity::class)
data class MainDestination(val something: String)

class MainActivity : AppCompatActivity() {

    private val traveler = Traveler()
    private val navigatorHolder get() = traveler.navigatorHolder
    val router get() = traveler.router

    private val navigator by lazy(LazyThreadSafetyMode.NONE) {
        CompassFragmentNavigator(android.R.id.content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            router.setRoot(CounterDestination(1, ColorGenerator.generate()))
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(this, navigator)
    }

}