package com.nguyen.jpmorgan.model

object ServiceLocator {
    // this repository property can be set by either instantiating a RemoteRepository via
    // provideRepository(), or by being directly assigned to FakeRepository in WeatherViewModelTest
    var repository: Repository? = null

    fun provideRepository(): Repository {
        if (repository == null)
            repository = RemoteRepository()
        return repository!!
    }
}