package white.ball.news.domain.model

import white.ball.news.R

sealed class ItemBottomBar(
    val iconImageDefault: Int,
    val iconImageClicked: Int,
    val route: String
) {
    data object MainScreen : ItemBottomBar(R.drawable.icon_home_default, R.drawable.icon_home_clicked, "main_screen")
    data object BookmarksScreen: ItemBottomBar(R.drawable.icon_bookmark_default, R.drawable.icon_bookmark_clicked, "bookmarks_screen")
    data object SearchScreen: ItemBottomBar(R.drawable.icon_search_default, R.drawable.icon_search_clicked, "search_screen")
}
