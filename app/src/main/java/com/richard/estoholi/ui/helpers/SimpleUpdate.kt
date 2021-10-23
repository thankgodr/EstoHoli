package com.richard.estoholi.ui.helpers

interface SimpleUpdate<T, E> {
    fun start()
    fun sucsess(res : T)
    fun error(err: E)
    fun complete()
}