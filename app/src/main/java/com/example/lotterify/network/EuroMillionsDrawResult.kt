package com.example.lotterify.network

import com.example.lotterify.network.DrawResult

data class EuroMillionsDrawResult(override val date : String, override val numbers : String) : DrawResult(date, numbers)