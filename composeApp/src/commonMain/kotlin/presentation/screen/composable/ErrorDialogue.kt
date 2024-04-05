package presentation.screen.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kms
import com.beepbeep.designSystem.ui.composable.snackbar.internal.SnackbarColor
import com.beepbeep.designSystem.ui.composable.snackbar.internal.icons.SnackbarIcon
import com.beepbeep.designSystem.ui.composable.snackbar.internal.icons.snackbaricon.IcError
import resource.Resources

@Composable
fun ErrorDialogue(
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    onClickConfirmButton: () -> Unit,
) {
    AppDialogue(
        onDismissRequest = onDismissRequest,
        modifier = modifier.fillMaxWidth(LocalDensity.current.density / 2f),
        containerColor = Color.White
    ) {
        Icon(
            imageVector = SnackbarIcon.IcError,
            contentDescription = Resources.strings.error,
            tint = SnackbarColor.Error,
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
            modifier = Modifier.padding(bottom = 16.dp, start = 8.dp, end = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        AppButton(
            title = Resources.strings.ok,
            onClick = onClickConfirmButton,
            modifier = Modifier.height(52.dp)
                .padding(bottom = 8.dp)
                .fillMaxWidth(LocalDensity.current.density / 2f),
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = Color.White,
        )
    }
}
