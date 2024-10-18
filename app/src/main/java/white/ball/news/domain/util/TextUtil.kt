package white.ball.news.domain.util

import android.util.Log
import white.ball.news.data.api.TAG
import java.text.SimpleDateFormat
import java.util.Date

class TextUtil {

    private val monthsOnEnglish = arrayOf("January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December")

    private fun convertMonthOnEnglish(monthIndex: Int): String = monthsOnEnglish[monthIndex]

    fun getTodayDayForTopBar(): String {
        val dateAndTimeFormat = SimpleDateFormat("dd MM")
        val localDateNumberFormat = dateAndTimeFormat.format(Date())
        val separateDateNumber = localDateNumberFormat.split(" ")
        val correctDateFormat = "${separateDateNumber[0]} ${convertMonthOnEnglish(separateDateNumber[1].toInt() - 1)}"
        return correctDateFormat
    }

    fun getTodayDayForAPI():String {
        val dateAndTimeFormat = SimpleDateFormat("YYYY dd MM")
        val localDateNumberFormat = dateAndTimeFormat.format(Date())
        val separateDateNumber = localDateNumberFormat.split(" ")
        Log.d(TAG, "getTodayDayForAPI: ${separateDateNumber[0]} ${separateDateNumber[1]} ${separateDateNumber[2]}")
        val dayToday = if(separateDateNumber[1].toInt() == 1) {
            26
        } else {
            separateDateNumber[1].toInt() - 2
        }

        val correctDate = "${separateDateNumber[0]}-${separateDateNumber[2]}-$dayToday"
        return correctDate
//        2024-09-07
    }

}