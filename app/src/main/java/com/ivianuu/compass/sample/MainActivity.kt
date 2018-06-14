package com.ivianuu.compass.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ivianuu.compass.CompassFragmentAppNavigator
import com.ivianuu.traveler.Traveler

class MainActivity : AppCompatActivity() {

    private val traveler = Traveler.create()
    val router get() = traveler.router

    private val navigator by lazy(LazyThreadSafetyMode.NONE) {
        CompassFragmentAppNavigator(this, supportFragmentManager, android.R.id.content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        router.navigateTo(
            WebsiteDestination("https://github.com/square/kotlinpoet/issues/310"))

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
}
