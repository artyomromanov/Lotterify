package com.example.lotterify.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.jsoup.Jsoup


class MainViewModel(context: Context) : ViewModel() {

    private val handler = android.os.Handler(context.mainLooper)

    private val resultData = MutableLiveData<List<Pair<String, String>>>()
    fun getResultsData() = resultData as LiveData<List<Pair<String,String>>>

    fun fetchNumbers() {

            Thread(Runnable {

                val datesStringBuilder = StringBuilder()

                val drawsList = mutableListOf<Pair<String, String>>()
                try {
                    val doc = Jsoup.connect("https://www.national-lottery.co.uk/results/euromillions/draw-history").get()

                    val dates = doc.select("li[class=\"table_cell table_cell_1 table_cell_first\"]")
                    val numbers = doc.select("li[class=\"table_cell table_cell_3\"]")
                    val luckyStarz = doc.select("li[class=\"table_cell table_cell_4\"]")
                    for (i in 1..dates.size) {
                        datesStringBuilder.append("\n").append(dates[i].text())
                        drawsList.add(dates[i].text() to numbers[i].text())
                    }

                } catch (e: Exception) {
                    datesStringBuilder.append("Error : ").append(e.message).append("\n")
                }

                handler.post {
                    resultData.value = drawsList
                }
            }).start()
        }
}

//li[class="table_cell table_cell_3"],li[class="table_cell table_cell_1 table_cell_first"]
//span[hello="Cleveland"][goodbye="Columbus"]
//
//span.table_cell_block
//	div a