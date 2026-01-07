package dev.tomoya0x00.lazycolumn.sandbox

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.metrics.performance.JankStats
import androidx.metrics.performance.PerformanceMetricsState
import dev.tomoya0x00.lazycolumn.sandbox.ui.theme.ComposeLazyColumnSandboxTheme

class MainActivity : ComponentActivity() {
    private lateinit var jankStats: JankStats

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup JankStats
        val metricsStateHolder = PerformanceMetricsState.getHolderForHierarchy(window.decorView)
        jankStats = JankStats.createAndTrack(window) { frameData ->
            if (frameData.isJank) {
                Log.w("JankStats", "Jank detected! Duration: ${frameData.frameDurationUiNanos / 1_000_000}ms, States: ${frameData.states}")
            }
        }

        setContent {
            ComposeLazyColumnSandboxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var selectedContent by remember { mutableStateOf("D") }

                    Column {
                        // Compose Version Title
                        Text(
                            text = "Compose: ${BuildConfig.COMPOSE_VERSION}",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        // Toggle UI
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            CompactOption(
                                selected = selectedContent == "D",
                                onClick = { selectedContent = "D" },
                                label = "Full",
                                desc = "Cache+Wrap+Lazy",
                                modifier = Modifier.weight(1f),
                            )
                            CompactOption(
                                selected = selectedContent == "E",
                                onClick = { selectedContent = "E" },
                                label = "LazyBox",
                                desc = "LazyBox only",
                                modifier = Modifier.weight(1f),
                            )
                            CompactOption(
                                selected = selectedContent == "F",
                                onClick = { selectedContent = "F" },
                                label = "Plain",
                                desc = "No optimization",
                                modifier = Modifier.weight(1f),
                            )
                        }

                        // Update JankStats state
                        metricsStateHolder.state?.putState(
                            "Content",
                            selectedContent
                        )

                        // Content
                        val itemClickListener = remember {
                            object : DummyItemClickListener {
                                override fun onItemClick(itemId: String, columnId: Int, rowPosition: Int) {
                                    // No-op for performance testing
                                }
                            }
                        }

                        when (selectedContent) {
                            "D" -> MainContentD(data = dummy, itemClickListener = itemClickListener)
                            "E" -> MainContentE(data = dummy)
                            "F" -> MainContentF(data = dummy)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        jankStats.isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        jankStats.isTrackingEnabled = false
    }
}

val dummy = (0..100).map { cnt ->
    DummyData(
        columnId = cnt,
        rowItems = (0..10).map { rowId ->
            DummyRowItemData(
                id = "$rowId",
                clickWrapper = DummyItemClickWrapper(itemId = "${cnt}_$rowId"),
            )
        }
    )
}

@Composable
private fun CompactOption(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    desc: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .selectable(selected = selected, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Column(modifier = Modifier.padding(start = 2.dp)) {
            Text(text = label, style = MaterialTheme.typography.caption)
            Text(
                text = desc,
                style = MaterialTheme.typography.overline,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
