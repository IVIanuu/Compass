package com.ivianuu.compass.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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

        MainDestination__RouteFactory

        if (savedInstanceState == null) {
            router.newRootScreen(
                CounterDestination(1, ColorGenerator.generate()))
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

    private inline fun d(m: () -> String) {
        Log.d("Main", m())
    }
}
