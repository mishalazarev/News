package white.ball.news.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.RoomRepository

class DetailScreenViewModel(
    roomRepository: RoomRepository,
    articlesAPI: LiveData<List<Article>>,
    articlesInBookmarks: LiveData<List<Article>>,
) : MainViewModel(
    roomRepository,
    articlesAPI,
    articlesInBookmarks
) {



}