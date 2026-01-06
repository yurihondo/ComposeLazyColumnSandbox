package dev.tomoya0x00.lazycolumn.sandbox

import LazyBox
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
 * MainContentE - Without optimization (no ModifierCacheHolder, no ClickWrapper)
 *
 * Same visual appearance as MainContentD, but without:
 * - ModifierCacheHolder (Modifiers are created on every recomposition)
 * - ClickWrapper (Lambda is created on every recomposition)
 *
 * LazyBox is included as it's not considered a special optimization.
 */
@Composable
fun MainContentE(
    data: List<DummyData>,
    itemClickListener: DummyItemClickListener,
) {
    LazyColumn {
        itemsIndexed(
            items = data,
            key = { _, item -> item.columnId },
        ) { _, item ->
            MainRowE(
                data = item,
                itemClickListener = itemClickListener,
            )
        }
    }
}

@Composable
private fun MainRowE(
    data: DummyData,
    itemClickListener: DummyItemClickListener,
) {
    Column(
        modifier = Modifier.padding(top = 8.dp),
    ) {
        Text(
            text = data.columnId.toString(),
            modifier = Modifier.padding(top = 8.dp),
        )

        LazyBox(
            delayMilliSec = 10,
            placeHolder = {
                Spacer(
                    modifier = Modifier.size(8.dp * 2 + 120.dp * 9f / 16),
                )
            },
        ) {
            LazyRow {
                itemsIndexed(
                    items = data.rowItems,
                    key = { _, item -> item.id },
                ) { index, item ->
                    MainItemE(
                        text = "${data.columnId}_${item.id}",
                        // No ClickWrapper - lambda is created on every recomposition
                        onClick = {
                            itemClickListener.onItemClick(
                                itemId = item.id,
                                columnId = data.columnId,
                                rowPosition = index,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MainItemE(
    text: String,
    onClick: () -> Unit,
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
            .clickable(onClick = onClick),
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
