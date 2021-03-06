@file:Suppress("unused")

package com.javadude.moviesnav

import android.app.Application
import com.facebook.stetho.Stetho

class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}