package com.ivianuu.compass.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ivianuu.compass.Destination
import com.ivianuu.compass.fragment.CompassFragmentNavigator
import com.ivianuu.traveler.Traveler
import com.ivianuu.traveler.lifecycle.setNavigator
import com.ivianuu.traveler.setRoot

@Destination(MainActivity::class)
data class MainDestination(val something: String)

class DummyClass

class MainActivity : AppCompatActivity() {

    private val traveler = Traveler()
    private val navigatorHolder get() = traveler.navigatorHolder
    val router get() = traveler.router

    private val navigator by lazy(LazyThreadSafetyMode.NONE) {
        CompassFragmentNavigator(android.R.id.content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val reflectSerializer = ReflectSerializer(MainDestination::class)

        val bundle = bundleOf(
            "com.ivianuu.compass.sample.MainDestination_something" to "my value",
            "test" to arrayListOf("haha"),
            "test_2" to arrayListOf(DummyClass())
        )

        try {
            Log.d("testt", "hehe -> ${reflectSerializer.fromBundle(bundle)}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (savedInstanceState == null) {
            router.setRoot(CounterDestination(1, ColorGenerator.generate()))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val bundle = bundleOf(
            "com.ivianuu.compass.sample.MainDestination_something" to "my value",
            "test" to arrayListOf("haha"),
            "test_2" to arrayListOf(DummyClass())
        )

        outState.putAll(bundle)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(this, navigator)
    }

}