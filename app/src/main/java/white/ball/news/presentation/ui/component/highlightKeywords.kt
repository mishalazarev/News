package white.ball.news.presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import white.ball.news.presentation.ui.theme.SnackBarColor

// Функция для выделения ключевых слов
@Composable
fun highlightKeywords(title: String, searchText: String): AnnotatedString {
    val keywords = searchText.split(" ").filter { it.isNotEmpty() }.map { it.lowercase() }
    val annotatedString = buildAnnotatedString {
        var currentIndex = 0
        val titleLower = title.lowercase()

        while (currentIndex < titleLower.length) {
            var matched = false
            for (keyword in keywords) {
                if (titleLower.startsWith(keyword, currentIndex)) {
                    // Если найдено совпадение, добавляем его как выделенное
                    append(title.substring(currentIndex, currentIndex + keyword.length))
                    addStyle(SpanStyle(color = SnackBarColor), length - keyword.length, length)
                    currentIndex += keyword.length
                    matched = true
                    break
                }
            }
            if (!matched) {
                append(title[currentIndex].toString())
                currentIndex++
            }
        }
    }
    return annotatedString
}