package com.nihongo.app

import android.app.Application
import com.nihongo.app.di.AppContainer

class NihongoApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
