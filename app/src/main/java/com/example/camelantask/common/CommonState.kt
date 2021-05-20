package com.example.camelantask.common

sealed class CommonState<out T> {
    object ShowLoading : CommonState<Nothing>()
    object LoadingFinished : CommonState<Nothing>()
    object NoInterNet : CommonState<Nothing>()
    data class Success<out R>(val data: R) : CommonState<R>()
    data class Error(val exception: Throwable) : CommonState<Nothing>()
    object EmptyStates: CommonState<Nothing>()

}