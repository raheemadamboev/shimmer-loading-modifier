package xyz.teamgravity.shimmerloadingmodifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import xyz.teamgravity.shimmerloadingmodifier.ui.theme.ShimmerLoadingModifierTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShimmerLoadingModifierTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var loading by remember { mutableStateOf(true) }

                    LaunchedEffect(key1 = true) {
                        delay(3_000L)
                        loading = false
                    }

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(20) {
                            ShimmerItem(loading = loading) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null,
                                        modifier = Modifier.size(100.dp)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(text = stringResource(id = R.string.dummy_text))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.shimmerEffect(): Modifier {
    return composed {
        var size by remember { mutableStateOf(IntSize.Zero) }

        val transition = rememberInfiniteTransition()
        val startOffsetX by transition.animateFloat(
            initialValue = -2 * size.width.toFloat(),
            targetValue = 2 * size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1_000
                )
            )
        )

        return@composed background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFB8B5B5),
                    Color(0xDD8F8B8B),
                    Color(0xFFB8B5B5)
                ),
                start = Offset(
                    x = startOffsetX,
                    y = 0F
                ),
                end = Offset(
                    x = startOffsetX + size.width.toFloat(),
                    y = size.height.toFloat()
                )
            )
        ).onGloballyPositioned {
            size = it.size
        }
    }
}

@Composable
fun ShimmerItem(
    loading: Boolean,
    content: @Composable () -> Unit,
) {
    if (loading) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1F)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7F)
                        .height(20.dp)
                        .shimmerEffect()
                )
            }
        }
    } else {
        content()
    }
}