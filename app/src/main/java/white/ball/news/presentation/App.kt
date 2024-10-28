package white.ball.news.presentation

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import white.ball.news.data.api.ApiService
import white.ball.news.data.api.TAG
import white.ball.news.data.storage.service.RoomService
import white.ball.news.domain.model.Article
import white.ball.news.domain.util.TextUtil

class App : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val apiService = ApiService()
    private val textUtil = TextUtil()

    val articlesAPI = MutableLiveData(emptyList<Article>())

    val roomService = RoomService()

    lateinit var articlesInBookmarks: LiveData<List<Article>>

    override fun onCreate() {
        super.onCreate()

        // Инициализация базы данных
        roomService.loadDataBase(applicationContext)

        // Запуск получения статей
        fetchArticles(
            articlesAPI,
            applicationContext,
            )

        applicationScope.launch {
            articlesInBookmarks = roomService.getArticlesInBookmarks()
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel()
    }

    // Функция для получения статей
    private fun fetchArticles(
        articles: MutableLiveData<List<Article>>,
        application: Context) {
        apiService.getArticles(
            articles,
            application,
            textUtil.getTodayDayForAPI()
        )
    }
}