package com.mobile.videocutter.base.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes protected val layoutId: Int) : Fragment(), BaseView {
    protected val TAG = this::class.java.simpleName
    private val baseActivity by lazy {
        requireActivity() as BaseActivity
    }
    protected lateinit var myInflater: LayoutInflater
    private lateinit var callback: OnBackPressedCallback
    protected lateinit var viewRoot: View

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPrepareInitView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::myInflater.isInitialized) {
            myInflater = LayoutInflater.from(requireActivity())
        }

        onInitBinding()
        return viewRoot
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onCleaned()
    }

    override fun onPrepareInitView() {

    }

    override fun onInitView() {

    }

    override fun onCleaned() {
        super.onCleaned()
    }
}
