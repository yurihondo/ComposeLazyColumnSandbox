package dev.tomoya0x00.lazycolumn.sandbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.tomoya0x00.lazycolumn.sandbox.ui.SimpleAsyncImage

/**
 * MainContentF - Without optimization and without LazyBox
 *
 * Same as MainContentE, but without:
 * - ModifierCacheHolder (Modifiers are created on every recomposition)
 * - ClickWrapper (Lambda is created on every recomposition)
 * - LazyBox (No deferred content loading)
 */
@Composable
fun MainContentF(
    data: List<DummyData>,
) {
    LazyColumn {
        itemsIndexed(
            items = data,
            key = { _, item -> item.columnId },
        ) { _, item ->
            MainRowF(
                data = item,
            )
        }
    }
}

@Composable
private fun MainRowF(
    data: DummyData,
) {
    Column(
        modifier = Modifier.padding(top = 8.dp),
    ) {
        Text(
            text = data.columnId.toString(),
            modifier = Modifier.padding(top = 8.dp),
        )

        // No LazyBox - directly render LazyRow
        LazyRow {
            itemsIndexed(
                items = data.rowItems,
                key = { _, item -> item.id },
            ) { _, item ->
                MainItemF(
                    text = "${data.columnId}_${item.id}",
                )
            }
        }
    }
}

@Composable
private fun MainItemF(
    text: String,
) {
    // No ModifierCacheHolder - Modifiers are created on every recomposition
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .width(120.dp)
            .aspectRatio(16f / 9)
            .background(
                color = MaterialTheme.colors.surface,
            )
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = {}),
    ) {
        SimpleAsyncImage(
            modifier = Modifier.fillMaxSize(),
            url = dummyImageUrl,
            contentDescription = null,
        )

        Text(
            text = text,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}
