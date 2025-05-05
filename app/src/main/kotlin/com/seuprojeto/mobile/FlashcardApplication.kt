package com.seuprojeto.mobile

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

/**
 * Application class for initialization tasks.
 */
class FlashcardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize ThreeTenABP for Java 8 date/time API backport
        AndroidThreeTen.init(this)
    }
} 