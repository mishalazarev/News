package white.ball.news.presentation.view_model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.RoomRepository

class BookmarksViewModel(private val roomRepository: RoomRepository) : ViewModel() {
    // Хранит статьи в закладках
    private val _articlesInBookmarks = mutableStateListOf<Article>()
    val articlesInBookmarks: List<Article> get() = _articlesInBookmarks

    init {
        viewModelScope.launch {
            loadBookmarks()
        }
    }

    suspend fun removeBookmark(article: Article) {
        roomRepository.removeBookmark(article)
        loadBookmarks()
    }

    suspend fun addBookmark(article: Article){
        roomRepository.addBookmark(article)
        loadBookmarks()
    }

    private suspend fun loadBookmarks() {
        _articlesInBookmarks.clear()
        _articlesInBookmarks.addAll(roomRepository.getArticlesInBookmarks())
    }
}