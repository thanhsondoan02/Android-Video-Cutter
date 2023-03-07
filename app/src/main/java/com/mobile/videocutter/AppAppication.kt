package com.mobile.videocutter

import android.app.Application
import com.mobile.videocutter.base.extension.setApplication

class AppAppication: Application() {
    override fun onCreate() {
        super.onCreate()
        setApplication(this)
    }
}
