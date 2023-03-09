package com.mobile.videocutter.presentation.model

interface IViewListener {
    fun onInitial() {}

    fun onLoading() {}

    fun onFailure() {}

    fun onSuccess()
}
