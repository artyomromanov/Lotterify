package com.example.lotterify.network

interface DrawsRepository {
    suspend fun fetchNumbers() : List<DrawResult>
}