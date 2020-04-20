package com.example.lotterify.network

interface RepositoryEuroMillions {
    suspend fun fetchNumbers() : List<EuroMillionsDrawResult>
}