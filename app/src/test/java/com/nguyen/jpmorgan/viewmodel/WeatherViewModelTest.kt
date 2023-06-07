package com.nguyen.jpmorgan.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nguyen.jpmorgan.model.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class WeatherViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineScope =  MainCoroutineScopeRule()

    // error: java.util.concurrent.TimeoutException: LiveData value was never set.
    @Test
    fun getRecord() = runTest {
        val viewModel = WeatherViewModel(ApplicationProvider.getApplicationContext(), FakeRepository())
        viewModel.fetchWeather("London")
        val record = viewModel.record.getOrAwaitValue()
        assertThat(record, not(nullValue()))
        assertThat(record.city.name, equalTo("London"))
        assertThat(record.list.size, equalTo(7))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {}
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }
        this.observeForever(observer)

        try {
            afterObserve.invoke()

            // Don't wait indefinitely if the LiveData is not set.
            if (!latch.await(time, timeUnit)) {
                throw TimeoutException("LiveData value was never set.")
            }

        } finally {
            this.removeObserver(observer)
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}