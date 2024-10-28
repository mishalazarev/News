package white.ball.news.data.storage.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import white.ball.news.domain.model.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Insert
    fun putNewArticle(newArticleDBO: Article)

    @Delete
    fun deleteArticle(deletedArticleDBO: Article)
}