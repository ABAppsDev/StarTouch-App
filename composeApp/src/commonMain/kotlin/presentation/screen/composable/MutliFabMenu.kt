package presentation.screen.composable

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.baseline_more_vert_24
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beepbeep.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun FilterFabMenuButton(
    item: MutliFabMenuItem,
    onClick: (MutliFabMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {

    FloatingActionButton(
        modifier = modifier.size(32.dp),
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
        onClick = {
            onClick(item)
        },
        backgroundColor = Theme.colors.primary
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(item.icon),
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun FilterFabMenuLabel(
    label: String,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = Color.Black.copy(alpha = 0.8f)
    ) {
        Text(
            text = label, color = Color.White,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp),
            fontSize = 14.sp,
            maxLines = 1
        )
    }

}

@Composable
fun FilterFabMenuItem(
    menuItem: MutliFabMenuItem,
    onMenuItemClick: (MutliFabMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //label
        FilterFabMenuLabel(label = menuItem.label)

        //fab
        FilterFabMenuButton(item = menuItem, onClick = onMenuItemClick)

    }

}

@Composable
fun FilterFabMenu(
    visible: Boolean,
    items: List<MutliFabMenuItem>,
    onMenuItemClick: (MutliFabMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {

    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        )
    }

    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Bottom,
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        ) + fadeOut(
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        )
    }


    AnimatedVisibility(visible = visible, enter = enterTransition, exit = exitTransition) {
        Column(
            modifier = modifier.fillMaxWidth().padding(end = 12.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items.forEach { menuItem ->
                FilterFabMenuItem(
                    menuItem = menuItem,
                    onMenuItemClick = onMenuItemClick
                )
            }
        }
    }
}

@Composable
fun FilterFab(
    state: MutliFabState,
    rotation: Float,
    onClick: (MutliFabState) -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = modifier
            .rotate(rotation),
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
        onClick = {
            onClick(
                if (state == MutliFabState.EXPANDED) {
                    MutliFabState.COLLAPSED
                } else {
                    MutliFabState.EXPANDED
                }
            )
        },
        backgroundColor = Theme.colors.primary,
        shape = CircleShape
    ) {
        Icon(
            painter = painterResource(Res.drawable.baseline_more_vert_24),
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun MutliFabView(
    items: List<MutliFabMenuItem>,
    modifier: Modifier = Modifier,
    onMenuItemClick: (MutliFabMenuItem) -> Unit
) {

    var mutliFabState by rememberSaveable {
        mutableStateOf(MutliFabState.COLLAPSED)
    }

    val transitionState = remember {
        MutableTransitionState(mutliFabState).apply {
            targetState = MutliFabState.COLLAPSED
        }
    }

    val transition = updateTransition(targetState = transitionState, label = "transition")

    val iconRotationDegree by transition.animateFloat({
        tween(durationMillis = 150, easing = FastOutSlowInEasing)
    }, label = "rotation") {
        if (mutliFabState == MutliFabState.EXPANDED) 230f else 0f
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom)
    ) {
        FilterFabMenu(
            items = items,
            visible = mutliFabState == MutliFabState.EXPANDED,
            onMenuItemClick = {
                onMenuItemClick.invoke(it)
                mutliFabState = MutliFabState.COLLAPSED
            })
        FilterFab(
            state = mutliFabState,
            rotation = iconRotationDegree, onClick = { state ->
                mutliFabState = state
            })
    }
}

data class MutliFabMenuItem(
    val icon: DrawableResource,
    val label: String
)

enum class MutliFabState {
    COLLAPSED,
    EXPANDED
}
