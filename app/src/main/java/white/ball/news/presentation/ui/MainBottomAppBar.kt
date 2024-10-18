package white.ball.news.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import white.ball.news.domain.model.ItemBottomBar
import white.ball.news.domain.util.TextUtil
import white.ball.news.presentation.nav_controller.MainNavController
import white.ball.news.presentation.ui.theme.MainColor
import white.ball.news.presentation.ui.theme.SnackBarColor
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import white.ball.news.data.api.ApiService
import white.ball.news.data.api.TAG
import white.ball.news.data.storage.service.RoomService
import white.ball.news.domain.model.Article

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainBottomAppBar(
    articles: MutableState<List<Article>>,
    navController: NavHostController,
    isLoading: MutableState<Boolean>,
    snackbarHostState: SnackbarHostState,
    roomService: RoomService,
    apiService: ApiService,
    context: Context,
) {
    val textUtil = TextUtil()
    val itemsBottomBarNavigation = arrayOf(
        ItemBottomBar.MainScreen,
        ItemBottomBar.BookmarksScreen,
        ItemBottomBar.SearchScreen
    )
    val loadArticles: @Composable () -> Unit = {
        ShimmerItems(
            contentAfterLoading = {
                MainNavController(
                    context,
                    navController,
                    snackbarHostState,
                    articles,
                    apiService,
                    roomService,
                )
            },
            isLoading
        )
    }

    MainNavController(
        context,
        navController,
        snackbarHostState,
        articles,
        apiService,
        roomService,
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier.height(120.dp),
                title = {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "News",
                            style = TextStyle(
                                fontStyle = FontStyle.Italic,
                                fontFamily = FontFamily.Cursive,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MainColor
                        )
                        Text(
                            text = textUtil.getTodayDayForTopBar(),
                            style = TextStyle(
                                fontFamily = FontFamily.Cursive,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MainColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SnackBarColor,
                    titleContentColor = MainColor
                )
            )
        },
        bottomBar = {
            androidx.compose.material.BottomNavigation(
                backgroundColor = SnackBarColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                itemsBottomBarNavigation.forEach { currentItemBottomBar ->
                    BottomNavigationItem(
                        selected = currentItemBottomBar.route == currentRoute,
                        onClick = {
                            navController.navigate(currentItemBottomBar.route) {
                                if (currentItemBottomBar.route == ItemBottomBar.MainScreen.route) {
                                    popUpTo(ItemBottomBar.MainScreen.route)
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    if (currentItemBottomBar.route == currentRoute) {
                                        currentItemBottomBar.iconImageClicked
                                    } else {
                                        currentItemBottomBar.iconImageDefault
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        selectedContentColor = MainColor,
                        unselectedContentColor = Color.Red
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (articles.value.isEmpty()) {
                    InternetNotWorking(
                        getArticlesFromApi = {
                            apiService.getArticles(
                                articlesListener = articles,
                                context = context,
                                todayDayForAPI = textUtil.getTodayDayForAPI()
                            )
                        },
                        context
                    )
                } else {
                    loadArticles()
                }
            }
        }
    )
}