package presentation.screen.composable.forms

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.logo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.composable.StButton
import com.beepbeep.designSystem.ui.composable.StCheckBox
import com.beepbeep.designSystem.ui.composable.StOutlinedButton
import com.beepbeep.designSystem.ui.composable.animate.SlideAnimation
import com.beepbeep.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.painterResource
import presentation.screen.composable.extensions.bottomBorder
import presentation.screen.setting.SettingDropDownChoose
import presentation.screen.setting.SettingInteractionListener
import presentation.screen.setting.SettingState
import presentation.screen.setting.SettingTextFieldChoose
import presentation.screen.setting.toDropDownState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsForm(
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
                painter = painterResource(Res.drawable.logo),
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
        SlideAnimation(
            visible = state.restaurants.isNotEmpty(),
            enter = slideInHorizontally { -it } + fadeIn(tween(600)),
            exit = slideOutHorizontally { it } + fadeOut(tween(600)),
        ) {
            SettingDropDownChoose(
                label = "Restaurant",
                options = state.restaurants.map { it.toDropDownState() },
                selectedItem = state.selectedRestaurant.toDropDownState()
            ) {
                listener.onChooseRest(it)
            }
        }
        SlideAnimation(
            visible = state.outlets.isNotEmpty(),
            enter = slideInHorizontally { -it } + fadeIn(tween(600)),
            exit = slideOutHorizontally { it } + fadeOut(tween(600)),
        ) {
            SettingDropDownChoose(
                label = "Outlet",
                options = state.outlets.map { it.toDropDownState() },
                selectedItem = state.selectedOutlet.toDropDownState()
            ) {
                listener.onChooseOutlet(it)
            }
        }
        SlideAnimation(
            visible = state.rooms.isNotEmpty(),
            enter = slideInHorizontally { -it } + fadeIn(tween(600)),
            exit = slideOutHorizontally { it } + fadeOut(tween(600)),
        ) {
            SettingDropDownChoose(
                label = "Dining Room",
                options = state.rooms.map { it.toDropDownState() },
                selectedItem = state.selectedMainRoom.toDropDownState()
            ) {
                listener.onChooseDinInRoom(it)
            }
        }
        SettingTextFieldChoose(
            title = "Work Station :",
            text = state.workStationId,
            hint = "Work Station ID",
            keyboardType = KeyboardType.Number,
            onValueChanged = listener::onWorkStationIdChanged
        )
        SettingTextFieldChoose(
            title = "Api Url :",
            text = state.apiUrl,
            onValueChanged = listener::onApiUrlChanged,
            hint = "IP Address",
            keyboardType = KeyboardType.Text,
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StCheckBox(
                label = "Call Center",
                isChecked = state.isCallCenter,
                onCheck = listener::onSelectedCallCenter,
            )
            StCheckBox(
                label = "Quick sale loop back",
                isChecked = state.isQuickSaleLoopBack,
                onCheck = listener::onQuickLoopBackSelected,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StOutlinedButton(
                title = "Close",
                onClick = listener::onClickClose,
                modifier = Modifier.weight(1f),
            )
            StButton(
                title = "Save",
                onClick = listener::onClickSave,
                modifier = Modifier.weight(1f),
                isLoading = state.isLoading
            )
        }
    }
}