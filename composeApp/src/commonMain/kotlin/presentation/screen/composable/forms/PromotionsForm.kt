package presentation.screen.composable.forms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import presentation.screen.composable.extensions.bottomBorder
import presentation.screen.setting.SettingInteractionListener
import presentation.screen.setting.SettingState
import presentation.screen.setting.toDropDownState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Promotions(
    code: String,
    name:String,
    image: Painter,
    name2:String,
    description:String,
    state: SettingState,
    listener: SettingInteractionListener

){

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
            title = "Status",
            text = name2,
            onValueChanged = {},
            hint = "",
            keyboardType = KeyboardType.Text,
        )

        SettingTextFieldChoose(
            title = "Type",
            text = description,
            onValueChanged = {},
            hint = "",
            keyboardType = KeyboardType.Text,
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingDropDownChoose(
                label = "Discount ",
                options = state.restaurants.map { it.toDropDownState() },
                selectedItem = state.selectedRestaurant.toDropDownState(),
                modifier = Modifier.fillMaxWidth(),
                onClick = {}
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingDropDownChoose(
                label = "Check Number ",
                options = state.restaurants.map { it.toDropDownState() },
                selectedItem = state.selectedRestaurant.toDropDownState(),
                modifier = Modifier.fillMaxWidth(),
                onClick = {}
            )

        }
        SettingTextFieldChoose(
            title = "From",
            text = name,
            onValueChanged = {},
            hint = "",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.fillMaxWidth()
        )
        Column(modifier = Modifier.padding(16.dp)){

            Row(
                modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                StCheckBox(
                    label = "By Time",
                    isChecked = true,
                    onCheck = {},
                )
            }


            Row (
                horizontalArrangement = Arrangement.SpaceAround){
                SettingTextFieldChoose(
                    title = "Start Time",
                    text = name,
                    onValueChanged = {},
                    hint = "",
                    keyboardType = KeyboardType.Text,
                    modifier =  Modifier.weight(1f)
                )
                SettingTextFieldChoose(
                    title = "End Time",
                    text = name,
                    onValueChanged = {},
                    hint = "",
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.weight(1f)
                )
            }
            SettingTextFieldChoose(
                title = "Selected Days",
                text = name,
                onValueChanged = {},
                hint = "",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )
            SettingTextFieldChoose(
                title = "Check Type",
                text = name,
                onValueChanged = {},
                hint = "",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )
            SettingTextFieldChoose(
                title = "Notes",
                text = name,
                onValueChanged = {},
                hint = "",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )


        }


        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StOutlinedButton(
                title = "Cancel",
                onClick = {},
                modifier = Modifier.weight(1f),
            )
            StButton(
                title = "Ok",
                onClick = {},
                modifier = Modifier.weight(1f),
                isLoading = false
            )
        }

    }}