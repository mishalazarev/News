package white.ball.news.data.storage.service

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import white.ball.news.data.storage.room.ArticleDao
import white.ball.news.data.storage.room.NewsRoomDataBase
import white.ball.news.data.storage.room.getNewsDataBase
import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.RoomRepository

class RoomService : RoomRepository {

    private lateinit var dataBase: NewsRoomDataBase

    private lateinit var articleDao: ArticleDao

    override fun loadDataBase(context: Context) {
        dataBase = getNewsDataBase(context)
        articleDao = dataBase.getArticleDao()
    }

    override fun putNewArticle(newArticle: Article) {
        articleDao.putNewArticle(newArticle)
    }


    override suspend fun getArticlesInBookmarks(): List<Article> {
        return withContext(Dispatchers.IO) {
            articleDao.getAllArticles()
        }
    }

    override suspend fun deleteArticle(deletedArticle: Article) {
        withContext(Dispatchers.IO) {
            articleDao.deleteArticle(deletedArticle)
        }
    }

}