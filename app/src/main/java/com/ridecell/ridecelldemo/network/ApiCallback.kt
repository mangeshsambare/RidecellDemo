package com.ridecell.ridecelldemo.network

import io.reactivex.rxjava3.disposables.Disposable

interface ApiCallback<T> {

    /*request sent to server from app*/
    fun requestSent()

    /*subscribe request for maintaining request status whether it is working or not */
    fun subscribeRequest(disposable: Disposable)

    /*successful executed*/
    fun success(t: T)

    /*error*/
    fun error(error: NetworkError)
}