package com.mobile.videocutter.base.common

import ai.ftech.base.common.navigation.FadeAnim
import ai.ftech.base.common.navigation.IScreenAnim
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes protected val layoutId: Int) : Fragment(), BaseView {
    protected val TAG = this::class.java.simpleName
    protected val baseActivity by lazy {
        requireActivity() as BaseActivity
    }
    protected lateinit var myInflater: LayoutInflater
    protected lateinit var viewRoot : View

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
        viewRoot= attachView(inflater, container, savedInstanceState)
        onInitBinding()
        return viewRoot
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        onInitView()
        onObserverViewModel()
    }

    override fun onInitView() {}

    override fun onDestroyView() {
        super.onDestroyView()
        onCleaned()
    }

    open fun attachView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(layoutId, container, false)
    }

    open fun getContainerId(): Int = LAYOUT_INVALID

    fun navigateTo(context: Context, kClass: Class<*>, bundle: Bundle? = null) {
        baseActivity.navigateTo(context, kClass, bundle)
    }

    fun replaceFragment(
        fragment: BaseFragment,
        bundle: Bundle? = null,
        keepToBackStack: Boolean = true,
        screenAnim: IScreenAnim = FadeAnim()
    ) {
        baseActivity.replaceFragment(fragment, bundle, keepToBackStack, screenAnim)
    }

    fun addFragment(
        fragment: BaseFragment,
        bundle: Bundle? = null,
        keepToBackStack: Boolean = true,
        screenAnim: IScreenAnim = FadeAnim()
    ) {
        baseActivity.addFragment(fragment, bundle, keepToBackStack, screenAnim)
    }

    fun addFragmentInsideFragment(
        fragment: BaseFragment,
        bundle: Bundle? = null,
        keepToBackStack: Boolean = true,
        screenAnim: IScreenAnim = FadeAnim()
    ) {
        includeFragment(
            fragment,
            bundle,
            getContainerId(),
            false,
            keepToBackStack,
            screenAnim
        )
    }

    fun backFragment() {
        baseActivity.backFragment()
    }

    fun clearStackFragment() {
        baseActivity.clearStackFragment()
    }

    fun getCurrentFragment(): Fragment? {
        return baseActivity.getCurrentFragment()
    }

    private fun includeFragment(
        fragment: Fragment,
        bundle: Bundle?,
        containerId: Int,
        isReplace: Boolean,
        keepToBackStack: Boolean,
        screenAnim: IScreenAnim
    ) {
        if (getContainerId() == LAYOUT_INVALID) {
            throw IllegalArgumentException("Cần phải gán container id để replace fragment")
        }
        try {
            val tag = fragment::class.java.simpleName
            bundle?.let {
                fragment.arguments = it
            }
            childFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    screenAnim.enter(),
                    screenAnim.exit(),
                    screenAnim.popEnter(),
                    screenAnim.popExit()
                )
                if (isReplace) {
                    replace(containerId, fragment, tag)
                } else {
                    add(containerId, fragment, tag)
                }
                if (keepToBackStack) {
                    addToBackStack(tag)
                }
                commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
