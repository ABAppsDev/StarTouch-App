package presentation.screen.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownTextField(
    options: List<DropDownState>,
    modifier: Modifier = Modifier.fillMaxWidth(),
    selectedItem: DropDownState,
    enabled: Boolean = true,
    onClick: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(selectedItem) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        AppTextField(
            text = selectedOptionText.name,
            enabled = enabled,
            onValueChange = {},
            readOnly = true,
            modifier = modifier.menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onClick(selectionOption.id)
                    },
                    text = {
                        Text(text = selectionOption.name)
                    },
                )
            }
        }
    }
}

@Immutable
data class DropDownState(
    val id: Int = 0,
    val name: String = ""
)