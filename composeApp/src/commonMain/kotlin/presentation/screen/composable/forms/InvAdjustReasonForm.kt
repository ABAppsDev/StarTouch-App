package presentation.screen.composable.forms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.composable.StButton
import com.beepbeep.designSystem.ui.composable.StCheckBox
import com.beepbeep.designSystem.ui.composable.StOutlinedButton
import com.beepbeep.designSystem.ui.theme.Theme
import presentation.screen.composable.extensions.bottomBorder
import presentation.screen.setting.SettingInteractionListener
import presentation.screen.setting.SettingState
import presentation.screen.setting.toDropDownState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvAdjustReasonForm(
    code: String,
    name: String,
    name2: String,
    image: Painter,
    state: SettingState,
    listener: SettingInteractionListener
) {

    Card(
        Modifier.fillMaxWidth().fillMaxHeight()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        shape = RoundedCornerShape(Theme.radius.large),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().bottomBorder(1.dp, Theme.colors.divider)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = image,
                contentDescription = "",
                modifier = Modifier.size(40.dp),
            )
            Text(
                text = "Admin",
                style = Theme.typography.titleLarge,
                color = Theme.colors.contentPrimary
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingTextFieldChoose(
                title = "Code",
                text = code,
                hint = "Enter Category Code",
                keyboardType = KeyboardType.Text,
                onValueChanged = {},
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            StCheckBox(
                label = "Active",
                isChecked = true,
                onCheck = {},
            )
        }
        SettingTextFieldChoose(
            title = "Name",
            text = name,
            onValueChanged = {},
            hint = "Enter Category Name",
            keyboardType = KeyboardType.Text,
        )
        SettingTextFieldChoose(
            title = "Name 2",
            text = name2,
            onValueChanged = {},
            hint = "Enter a name 2",
            keyboardType = KeyboardType.Text,
        )
        SettingDropDownChoose(
            label = "Item Type",
            options = state.restaurants.map { it.toDropDownState() },
            selectedItem = state.selectedRestaurant.toDropDownState()
        ) {
            listener.onChooseRest(it)
        }

        // Start Add RadioButton
        Row(
            modifier = Modifier.padding(all = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Debit",
                style = Theme.typography.titleLarge,
                color = Theme.colors.contentPrimary
            )
            RadioButton(
                selected = true,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.Red,
                    unselectedColor = Color.White
                ),
                enabled = true
            )
            Text(
                text = "Credit",
                style = Theme.typography.titleLarge,
                color = Theme.colors.contentPrimary
            )
            RadioButton(
                selected = false,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.Red,
                    unselectedColor = Color.White
                ),
                enabled = true
            )
        }
        // End Add RadioButton

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StOutlinedButton(
                title = "Close",
                onClick = {},
                modifier = Modifier.weight(1f),
            )
            StButton(
                title = "Save",
                onClick = {},
                modifier = Modifier.weight(1f),
                isLoading = false
            )
        }
    }
}