package dev.tomoya0x00.lazycolumn.sandbox

import androidx.compose.runtime.Immutable

/**
 * Created by tomoya0x00 on 2022/03/12.
 */
@Immutable
data class DummyData(
    val columnId: Int,
    val rowItems: List<DummyRowItemData>,
)

@Immutable
data class DummyRowItemData(
    val id: String,
    val clickWrapper: DummyItemClickWrapper,
)

const val dummyImageUrl = "https://placehold.jp/24/cc9999/993333/160x90.png"
