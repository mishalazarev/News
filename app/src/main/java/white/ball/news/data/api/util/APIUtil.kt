package white.ball.news.data.api.util


class APIUtil {

    fun convertPublishedAt(publishedAt: String): String {
        val dateArray = publishedAt
            .split("T")[0]
            .split("-")

        return "${dateArray[2]}.${dateArray[1]}.${dateArray[0]}"
    }

}