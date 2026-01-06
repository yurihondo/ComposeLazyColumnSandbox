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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.tomoya0x00.lazycolumn.sandbox.ui.ModifierCacheHolder
import dev.tomoya0x00.lazycolumn.sandbox.ui.SimpleAsyncImage

/**
 * MainContentD - With optimization (ModifierCacheHolder + ClickWrapper)
 */
@Composable
fun MainContentD(
    data: List<DummyData>,
    itemClickListener: DummyItemClickListener,
) {
    val modifierCacheHolder = remember {
        ModifierCacheHolder()
    }

    LazyColumn {
        items(
            items = data,
            key = { it.columnId },
        ) {
            MainRowD(
                modifierCacheHolder = modifierCacheHolder,
                data = it,
                itemClickListener = itemClickListener,
            )
        }
    }
}

@Composable
private fun MainRowD(
    modifierCacheHolder: ModifierCacheHolder,
    data: DummyData,
    itemClickListener: DummyItemClickListener,
) {
    Column(
        modifier = modifierCacheHolder.getOrCreate(tag = "MainRowRoot") {
            Modifier.padding(top = 8.dp)
        },
    ) {
        Text(
            text = data.columnId.toString(),
            modifier = modifierCacheHolder.getOrCreate(tag = "MainRowText") {
                Modifier.padding(top = 8.dp)
            },
        )

        LazyBox(
            delayMilliSec = 10,
            placeHolder = {
                Spacer(
                    modifier = modifierCacheHolder.getOrCreate(tag = "MainRowPlaceHolder") {
                        Modifier.size(8.dp * 2 + 120.dp * 9f / 16)
                    },
                )
            },
        ) {
            LazyRow {
                itemsIndexed(
                    items = data.rowItems,
                    key = { _, item -> item.id },
                ) { index, item ->
                    MainItemD(
                        modifierCacheHolder = modifierCacheHolder,
                        text = "${data.columnId}_${item.id}",
                        onClick = item.clickWrapper.getOrCreateOnItemClick(
                            listener = itemClickListener,
                            columnId = data.columnId,
                            rowPosition = index,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun MainItemD(
    modifierCacheHolder: ModifierCacheHolder,
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifierCacheHolder.getOrCreate(tag = "MainItemRoot") {
            Modifier
                .padding(start = 8.dp)
                .width(120.dp)
                .aspectRatio(16f / 9)
                .background(
                    color = MaterialTheme.colors.surface,
                )
                .clip(MaterialTheme.shapes.medium)
        }.clickable(onClick = onClick),
    ) {
        SimpleAsyncImage(
            modifier = modifierCacheHolder.getOrCreate(tag = "MainItemImage") {
                Modifier.fillMaxSize()
            },
            url = dummyImageUrl,
            contentDescription = null,
        )

        Text(
            text = text,
            modifier = modifierCacheHolder.getOrCreate(tag = "MainItemTex") {
                Modifier.align(Alignment.TopCenter)
            },
        )
    }
}
