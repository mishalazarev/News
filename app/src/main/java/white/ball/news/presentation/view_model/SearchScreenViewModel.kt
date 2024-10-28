package white.ball.news.presentation.view_model

import androidx.lifecycle.LiveData
import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.RoomRepository

class SearchScreenViewModel (
    roomRepository: RoomRepository,
    articlesAPI: LiveData<List<Article>>,
    articlesInBookmarks: LiveData<List<Article>>,
) : MainViewModel(
    roomRepository,
    articlesAPI,
    articlesInBookmarks
) {



}