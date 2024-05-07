package presentation.screen.composable.forms

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.dish
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.composable.StButton
import com.beepbeep.designSystem.ui.composable.StCheckBox
import com.beepbeep.designSystem.ui.composable.StOutlinedButton
import com.beepbeep.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.painterResource
import presentation.screen.composable.extensions.bottomBorder
import presentation.screen.setting.SettingInteractionListener
import presentation.screen.setting.SettingState
import presentation.screen.setting.toDropDownState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrinterIPForm(
    code: String,
    name: String,
    codeDrawer: String,
    image: Painter,
    printerState: SettingState,
    printerListener: SettingInteractionListener,
    printerTwoState: SettingState,
    printerTwoListener: SettingInteractionListener,
    languageState: SettingState,
    languageListener: SettingInteractionListener,
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
        SettingDropDownChoose(
            label = "Printer",
            options = printerState.restaurants.map { it.toDropDownState() },
            selectedItem = printerState.selectedRestaurant.toDropDownState()
        ) {
            printerListener.onChooseRest(it)
        }
        SettingDropDownChoose(
            label = "Printer 2",
            options = printerTwoState.restaurants.map { it.toDropDownState() },
            selectedItem = printerTwoState.selectedRestaurant.toDropDownState()
        ) {
            printerTwoListener.onChooseRest(it)
        }
        SettingDropDownChoose(
            label = "Language",
            options = languageState.restaurants.map { it.toDropDownState() },
            selectedItem = languageState.selectedRestaurant.toDropDownState()
        ) {
            languageListener.onChooseRest(it)
        }
        SettingTextFieldChoose(
            title = "Code Drawer",
            text = codeDrawer,
            onValueChanged = {},
            hint = "Enter an a code drawer",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.height(96.dp)
        )
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