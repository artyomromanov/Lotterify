package com.example.lotterify.main.model

import com.example.lotterify.network.DrawResult

sealed class LoadingDrawsState {
    object IN_PROGRESS : LoadingDrawsState()
    data class SUCCESS(val results : List<DrawResult>) : LoadingDrawsState()
    data class ERROR(val error : Throwable) : LoadingDrawsState()
}