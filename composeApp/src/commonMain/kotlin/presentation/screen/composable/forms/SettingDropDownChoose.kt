package presentation.screen.composable.forms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.screen.composable.DropDownState
import presentation.screen.composable.DropDownTextField

@Composable
fun SettingDropDownChoose(
    label: String,
    options: List<DropDownState>,
    selectedItem: DropDownState,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) {
    Row(
        modifier.fillMaxWidth().padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DropDownTextField(
            modifier = Modifier.padding(start = 8.dp).padding(vertical = 8.dp),
            options = options,
            selectedItem = selectedItem,
            label = label
        ) {
            onClick(it)
        }
    }
}