package com.example.lotterify.network

import com.example.lotterify.error.DrawsError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class RepositoryEuroMillionsImpl : RepositoryEuroMillions {

    override suspend fun fetchNumbers() : List<EuroMillionsDrawResult> {

        val drawsList : MutableList<EuroMillionsDrawResult> = ArrayList()

        withContext(Dispatchers.IO) {

            try {

                val doc = Jsoup.connect("https://www.national-lottery.co.uk/results/euromillions/draw-history").get()

                val dates = doc.select("li[class=\"table_cell table_cell_1 table_cell_first\"]")
                val numbers = doc.select("li[class=\"table_cell table_cell_3\"]")
                val luckyStarz = doc.select("li[class=\"table_cell table_cell_4\"]")

                dates.forEachIndexed  { index, value ->
                    if(index != 0) drawsList.add(EuroMillionsDrawResult(value.text(), numbers[index].text()))
                }

            } catch (e: Throwable) {
                throw DrawsError("Error : ${e.message}", e.cause)
            }
        }
        return drawsList
    }
}



