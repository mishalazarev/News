package white.ball.news.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.RoomRepository
import kotlin.random.Random

abstract class MainViewModel(
    private val roomRepository: RoomRepository,
    articlesAPI: LiveData<List<Article>>,
    articlesInBookmarks: LiveData<List<Article>>,
) : ViewModel() {

    private val _mArticlesAPI = MutableLiveData<List<Article>>()
    val mArticlesAPI: LiveData<List<Article>> = _mArticlesAPI

    private val _mArticleInBookmarks = MutableLiveData<List<Article>>()
    val mArticleInBookmarks: LiveData<List<Article>> = _mArticleInBookmarks

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _mArticlesAPI.value = articlesAPI.value
        _mArticleInBookmarks.value = articlesInBookmarks.value
    }

    suspend fun removeBookmark(article: Article) {
        roomRepository.removeBookmark(article)
    }

    suspend fun addBookmark(article: Article){
        roomRepository.addBookmark(article)
    }

    suspend fun updateArticlesWithBookmarks() {
        val updatedArticles = _mArticlesAPI.value?.map { article ->
            val bookmarkArticle = _mArticleInBookmarks.value?.find { it.title == article.title }
            bookmarkArticle ?: article
        }
        _mArticlesAPI.value = updatedArticles!!
    }

    fun setArticlesAPI(newArticles: List<Article>) {
        if (newArticles.isNotEmpty()) {
            _mArticlesAPI.value = newArticles
        }
    }

    suspend fun loadingArticlesAPI() {
        delay(Random.nextLong(1000,5000))
        _isLoading.value = true
    }

}