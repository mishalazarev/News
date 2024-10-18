package white.ball.news.data.storage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import white.ball.news.domain.model.Article

const val DATA_BASE_FILE = "data_base_file"

@Database(entities = [Article::class], version = 1)
abstract class NewsRoomDataBase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

}

fun getNewsDataBase(applicationContext: Context): NewsRoomDataBase {
    return Room.databaseBuilder(
        applicationContext,
        NewsRoomDataBase::class.java,
        DATA_BASE_FILE)
        .build()
}

