package com.ivianuu.compass.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivianuu.compass.CompassFragmentAppNavigator
import com.ivianuu.compass.Destination
import com.ivianuu.traveler.Traveler

@Destination(MainActivity::class)
data class MainDestination(val something: String)

class MainActivity : AppCompatActivity() {

    private val traveler = Traveler.create()
    val router get() = traveler.router

    private val navigator by lazy(LazyThreadSafetyMode.NONE) {
        CompassFragmentAppNavigator(this, supportFragmentManager, android.R.id.content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            router.newRootScreen(Dummy.CounterDestination(1, ColorGenerator.generate()))
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        traveler.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        traveler.navigatorHolder.removeNavigator()
        super.onPause()
    }
}
