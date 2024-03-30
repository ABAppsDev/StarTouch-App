package presentation.screen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerListItem(
    modifier: Modifier = Modifier,
    itemsCount: Int = 10,
    columnsCount: Int = 3,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    verticalPadding: Dp = 16.dp,
    horizontalPadding: Dp = 24.dp,
    content: @Composable (Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsCount),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding),
        verticalArrangement = Arrangement.spacedBy(verticalPadding),
        contentPadding = contentPadding
    ) {
        items(itemsCount) {
            content(it)
        }
    }
}