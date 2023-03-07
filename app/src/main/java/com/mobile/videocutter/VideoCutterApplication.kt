package com.mobile.videocutter

import com.mobile.videocutter.base.common.BaseApplication
import com.mobile.videocutter.base.extension.setApplication

class VideoCutterApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        setApplication(this)
    }
}
