package com.mobile.videocutter.base.common

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.InflateException
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(@LayoutRes protected val layoutId: Int) : AppCompatActivity(), BaseView {
    companion object {
        const val FRAGMENT_NAME = "FRAGMENT_NAME"
        const val FRAGMENT_BUNDLE = "FRAGMENT_BUNDLE"
    }

    protected val TAG = this::class.java.simpleName
    private var permissionListener: PermissionListener? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val listDenied = mutableListOf<String>()
            val listNeverAskAgain = mutableListOf<String>()
            it.forEach { (k, v) ->
                if (!v) {
                    if (shouldShowRequestPermissionRationale(k)) {
                        listDenied.add(k)
                    } else {
                        listNeverAskAgain.add(k)
                    }
                }
            }
            if (listDenied.isEmpty() && listNeverAskAgain.isEmpty()) {
                permissionListener?.onAllow()
            } else {
                permissionListener?.onDenied(listNeverAskAgain)
            }
        }
    }

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

    override fun onInitView() {}

    open fun attachView() {
        setContentView(layoutId)
    }

    fun doRequestPermission(permissions: Array<String>, listener: PermissionListener) {
        permissionListener = listener
        launcher.launch(permissions)
    }

    interface PermissionListener {
        fun onAllow()
        fun onDenied(neverAskAgainPermissionList: List<String>)
    }
}
