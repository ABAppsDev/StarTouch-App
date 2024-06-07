package presentation.screen.takeaway.hold

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.invoice
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.beepbeep.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.painterResource
import presentation.screen.composable.SetLayoutDirection
import presentation.screen.composable.extensions.bottomBorder
import presentation.screen.composable.modifier.bounceClick
import presentation.screen.dinin.AssignCheckState
import resource.Resources

class HoldOrderScreen : Screen {
    @Composable
    override fun Content() {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(emptyList<AssignCheckState>()) { check ->
                CheckItem(check) {
                    //  dinInInteractionListener.onClickCheck(check.id, check.name.toInt())
                }
            }
        }
    }
}

@Composable
private fun CheckItem(
    check: AssignCheckState,
    onClick: () -> Unit,
) {
    SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
        Row(
            modifier = Modifier.fillMaxWidth().bounceClick { onClick() }.padding(8.dp)
                .bottomBorder(1.dp, Theme.colors.divider),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(48.dp).padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(Theme.radius.large))
                    .background(Theme.colors.surface)
            ) {
                Icon(
                    painterResource(Res.drawable.invoice),
                    contentDescription = null,
                    Modifier.align(Alignment.Center).size(24.dp),
                    tint = Color.Unspecified
                )
            }
            Column(
                modifier = Modifier.weight(1f).padding(bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    Resources.strings.checkNumber + " : ${check.name}",
                    style = Theme.typography.title,
                    color = Theme.colors.contentPrimary
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    check.date,
                    style = Theme.typography.title,
                    color = Theme.colors.contentPrimary
                )
                Text(
                    check.status,
                    style = Theme.typography.title,
                    color = Theme.colors.contentPrimary
                )
            }
        }
    }
}