package white.ball.news.data.api

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import white.ball.news.data.api.util.APIUtil
import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.RoomRepository

const val API_KEY = "df94cae1412f40679dd7e87b6a1dee01"
const val TAG = "log_tag"

class ApiService {

    private val apiUtil = APIUtil()

    fun getArticles(
        articlesListener: MutableLiveData<List<Article>>,
        context: Context,
        todayDayForAPI: String,
    ) {
        val baseUrl = "https://newsapi.org/v2/everything?q=apple&from=" +
                todayDayForAPI +
                "&language=en&sortBy=publishedAt?&apiKey=" +
                API_KEY
        val queue = Volley.newRequestQueue(context)

        val request = object : StringRequest(
            Method.GET,
            baseUrl,
            { response ->
                articlesListener.value = convertMainJSONObjectToAllArticles(response)
            },
            { error: VolleyError ->
                Log.e(TAG, "Volley error: $error")
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = mutableMapOf<String, String>()
                headers["Content-Type"] = "application/json"
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        queue.add(request)
    }

    private fun convertMainJSONObjectToAllArticles(response: String): MutableList<Article> {

        val articles = mutableListOf<Article>()
        val mainJSONObject = JSONObject(response)

        val articlesJSONArray = mainJSONObject.getJSONArray("articles")

        for (index in 0 until articlesJSONArray.length()) {
            val author = (articlesJSONArray[index] as JSONObject).getString("author")
            val sourceName = (articlesJSONArray[index] as JSONObject).getJSONObject("source").getString("name")
            val content = (articlesJSONArray[index] as JSONObject).getString("content")
            val description = (articlesJSONArray[index] as JSONObject).getString("description")
            val publishedAt = apiUtil.convertPublishedAt((articlesJSONArray[index] as JSONObject).getString("publishedAt"))
            val title = (articlesJSONArray[index] as JSONObject).getString("title")
            val url = (articlesJSONArray[index] as JSONObject).getString("url")
            val urlToImage = (articlesJSONArray[index] as JSONObject).getString("urlToImage")
            Log.d(TAG, "convertMainJSONObjectToAllArticles: $urlToImage")

            if (urlToImage != "null" && author != "null") {

                articles.add(
                    Article(
                        author = author,
                        sourceName = sourceName,
                        content = content,
                        description = description,
                        publishedAt = publishedAt,
                        title = title,
                        url = url,
                        urlToImage = urlToImage,
                        isInYourBookmark = false
                    )
                )
            }
        }

        return articles
    }
}