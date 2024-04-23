package presentation.screen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.composable.snackbar.internal.SnackbarColor
import com.beepbeep.designSystem.ui.composable.snackbar.internal.icons.SnackbarIcon
import com.beepbeep.designSystem.ui.composable.snackbar.internal.icons.snackbaricon.IcWarning
import kms
import resource.Resources

@Composable
fun WarningItemDialogue(
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    onClickConfirmButton: () -> Unit,
    onClickRejectButton: () -> Unit,
    onClickDismissButton: () -> Unit,
) {
    AppDialogue(
        onDismissRequest = onDismissRequest,
        modifier = modifier.fillMaxWidth(LocalDensity.current.density / 2f),
        containerColor = Color.White
    ) {
        Icon(
            imageVector = SnackbarIcon.IcWarning,
            contentDescription = Resources.strings.warning,
            tint = SnackbarColor.Warning,
            modifier = Modifier.size(64.kms).padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            title,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AppButton(
                    title = Resources.strings.cancel,
                    onClick = onClickDismissButton,
                    modifier = Modifier.height(52.dp)
                        .padding(bottom = 8.dp, end = 8.dp)
                        .weight(LocalDensity.current.density / 3f),
                    containerColor = SnackbarColor.Error,
                    contentColor = Color.White,
                )
                AppButton(
                    title = Resources.strings.ok,//yes
                    onClick = onClickConfirmButton,
                    modifier = Modifier.height(52.dp)
                        .padding(bottom = 8.dp)
                        .weight(LocalDensity.current.density / 3f),
                    containerColor = SnackbarColor.Success,
                    contentColor = Color.White,
                )
                AppButton(
                    title = Resources.strings.ok,//no
                    onClick = onClickRejectButton,
                    modifier = Modifier.height(52.dp)
                        .padding(bottom = 8.dp)
                        .weight(LocalDensity.current.density / 3f),
                    containerColor = SnackbarColor.Success,
                    contentColor = Color.White,
                )
            }
        }
    }
}