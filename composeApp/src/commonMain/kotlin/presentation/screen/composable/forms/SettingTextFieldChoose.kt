package presentation.screen.composable.forms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.composable.StTextField

@Composable
fun SettingTextFieldChoose(
    title: String,
    text: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Number,
) {
    Row(
        modifier.fillMaxWidth().padding(horizontal = 8.dp).padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StTextField(
            label = title,
            text = text,
            onValueChange = onValueChanged,
            modifier = Modifier.padding(start = 8.dp).padding(bottom = 8.dp),
            keyboardType = keyboardType,
            hint = hint,
        )
    }
}