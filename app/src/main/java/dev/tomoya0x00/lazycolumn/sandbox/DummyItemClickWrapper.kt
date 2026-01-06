package dev.tomoya0x00.lazycolumn.sandbox

import androidx.compose.runtime.Stable
import java.lang.ref.WeakReference

/**
 * ClickWrapper pattern for performance optimization.
 * Reuses lambda instances to reduce GC pressure during LazyList scrolling.
 */
@Stable
class DummyItemClickWrapper(
    private val itemId: String,
) {
    private var listenerRef: WeakReference<DummyItemClickListener>? = null
    private var columnId: Int? = null
    private var rowPosition: Int? = null

    @Stable
    fun getOrCreateOnItemClick(
        listener: DummyItemClickListener,
        columnId: Int,
        rowPosition: Int,
    ): () -> Unit {
        updateListenerReferenceIfNeeded(listener)
        this.columnId = columnId
        this.rowPosition = rowPosition
        return onItemClick
    }

    private val onItemClick: () -> Unit = {
        listenerRef?.get()?.onItemClick(
            itemId = itemId,
            columnId = checkNotNull(columnId),
            rowPosition = checkNotNull(rowPosition),
        )
    }

    private fun updateListenerReferenceIfNeeded(listener: DummyItemClickListener) {
        if (listenerRef?.get() == listener) return
        listenerRef = WeakReference(listener)
    }
}

interface DummyItemClickListener {
    fun onItemClick(itemId: String, columnId: Int, rowPosition: Int)
}
