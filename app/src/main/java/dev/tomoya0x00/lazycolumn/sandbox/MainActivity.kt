package dev.tomoya0x00.lazycolumn.sandbox

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
                    var useOptimization by remember { mutableStateOf(true) }

                    Column {
                        // Toggle UI
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = useOptimization,
                                onClick = { useOptimization = true }
                            )
                            Text(
                                text = "With Optimization (D)",
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            RadioButton(
                                selected = !useOptimization,
                                onClick = { useOptimization = false }
                            )
                            Text(text = "Without Optimization (E)")
                        }

                        // Update JankStats state
                        metricsStateHolder.state?.putState(
                            "Optimization",
                            if (useOptimization) "Enabled" else "Disabled"
                        )

                        // Content
                        val itemClickListener = remember {
                            object : DummyItemClickListener {
                                override fun onItemClick(itemId: String, columnId: Int, rowPosition: Int) {
                                    // No-op for performance testing
                                }
                            }
                        }

                        if (useOptimization) {
                            MainContentD(data = dummy, itemClickListener = itemClickListener)
                        } else {
                            MainContentE(data = dummy)
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
