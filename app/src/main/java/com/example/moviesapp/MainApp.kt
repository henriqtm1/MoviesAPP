
package com.example.moviesapp

import android.app.Application
import com.example.moviesapp.di.RetrofitModule
import com.example.moviesapp.di.apiModule
import com.example.moviesapp.di.repositoryModule
import com.example.moviesapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp : Application() {
    companion object {
        lateinit var INSTANCE: MainApp
            private set
    }
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApp)
            modules(listOf(
                viewModelModule,
                apiModule,
                repositoryModule,
                RetrofitModule
            ))

        }
        INSTANCE = this
    }
}