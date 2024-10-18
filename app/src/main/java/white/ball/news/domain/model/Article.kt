package white.ball.news.domain.model

import androidx.compose.runtime.MutableState
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("articles")
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("author") val author: String,
    @ColumnInfo("source_name") val sourceName: String,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("publish_at") val publishedAt: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("url") val url: String,
    @ColumnInfo("url_to_image") val urlToImage: String?,
    @ColumnInfo("is_in_your_bookmark") var isInYourBookmark: Boolean
)