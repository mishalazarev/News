package white.ball.news.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import white.ball.news.domain.model.Article
import white.ball.news.domain.service.SearchService
import white.ball.news.presentation.ui.theme.SnackBarColor

@Composable
fun SearchTextField(
    articles: LiveData<List<Article>>,
    searchTextState: MutableState<TextFieldValue>,
    articlesResponse: MutableState<List<Article>>,
    searchService: SearchService,
) {
    val coroutineScope = rememberCoroutineScope()

    TextField(
        value = searchTextState.value,
        onValueChange = { value -> searchTextState.value = value },
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        textStyle = TextStyle(fontSize = 20.sp),
        leadingIcon = {
            if (searchTextState.value.text.isNotEmpty()) {
                IconButton(onClick = { searchTextState.value = TextFieldValue("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp),
                        tint = Color.Gray
                    )
                }
            }
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        coroutineScope.launch {
                            articlesResponse.value = searchService.getAllArticlesOnRequest(
                                searchTextState.value.text, articles
                            )
                        }
                    }
            )
        },
        placeholder = { Text(
            text = "Search...",
            modifier = Modifier.height(40.dp),
            style = TextStyle(fontSize = 20.sp)
        )},
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = SnackBarColor,
            cursorColor = SnackBarColor,
            backgroundColor = Color.White
        )
    )
}
