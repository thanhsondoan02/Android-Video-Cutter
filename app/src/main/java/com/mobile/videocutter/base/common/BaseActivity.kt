package com.mobile.videocutter.base.common

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.InflateException
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(@LayoutRes protected val layoutId: Int) : AppCompatActivity(), BaseView {

    protected val TAG = this::class.java.simpleName

    private val safeActionManager = SafeActionManager()

    //region lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        onPrepareInitView()
        super.onCreate(savedInstanceState)
        try {
            attachView()
            onInitBinding()
            onInitView()
            onObserverViewModel()
        } catch (e: InflateException) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        safeActionManager.setSafeActionState(true)
        safeActionManager.doWaitingAction()
    }

    override fun onPause() {
        safeActionManager.setSafeActionState(false)
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onInitView() {

    }

    open fun attachView() {
        setContentView(layoutId)
    }

}
