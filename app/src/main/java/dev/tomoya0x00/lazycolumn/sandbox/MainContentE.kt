package dev.tomoya0x00.lazycolumn.sandbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.tomoya0x00.lazycolumn.sandbox.ui.SimpleAsyncImage

/**
 * MainContentE - Without optimization (ModifierCacheHolder removed)
 *
 * Same visual appearance as MainContentD, but without:
 * - ModifierCacheHolder (Modifiers are created on every recomposition)
 * - LazyBox (no delayed content loading)
 *
 * Used for performance comparison with MainContentD.
 */
@Composable
fun MainContentE(
    data: List<DummyData>
) {
    LazyColumn {
        items(
            items = data,
            key = { it.columnId },
        ) {
            MainRowE(
                data = it,
            )
        }
    }
}

@Composable
private fun MainRowE(
    data: DummyData,
) {
    Column(
        modifier = Modifier.padding(top = 8.dp),
    ) {
        Text(
            text = data.columnId.toString(),
            modifier = Modifier.padding(start = 8.dp),
        )

        LazyRow {
            items(
                items = data.rowIds,
                key = { it },
            ) { rowId ->
                MainItemE(
                    text = "${data.columnId}_$rowId",
                )
            }
        }
    }
}

@Composable
private fun MainItemE(
    text: String,
) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .width(120.dp)
            .aspectRatio(16f / 9)
            .background(
                color = MaterialTheme.colors.surface,
            )
            .clip(MaterialTheme.shapes.medium),
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
