package presentation.screen.setting

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.ic_back
import abapps_startouch.composeapp.generated.resources.logo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.beepbeep.designSystem.ui.composable.StButton
import com.beepbeep.designSystem.ui.composable.StCheckBox
import com.beepbeep.designSystem.ui.composable.StOutlinedButton
import com.beepbeep.designSystem.ui.composable.StTextField
import com.beepbeep.designSystem.ui.composable.animate.SlideAnimation
import com.beepbeep.designSystem.ui.composable.modifier.noRippleEffect
import com.beepbeep.designSystem.ui.composable.snackbar.StackedSnackbarDuration
import com.beepbeep.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.painterResource
import presentation.screen.composable.AppScaffold
import presentation.screen.composable.DropDownState
import presentation.screen.composable.DropDownTextField
import presentation.screen.composable.SetLayoutDirection
import presentation.screen.composable.extensions.bottomBorder
import presentation.screen.composable.snackbar.rememberStackedSnackbarHostState
import presentation.util.EventHandler

class SettingScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SettingScreenModel>()
        val state by screenModel.state.collectAsState()

        EventHandler(screenModel.effect) { effect, navigator ->
            when (effect) {
                is SettingUiEffect.NavigateBackToHome -> navigator.pop()
            }
        }

        val stackedSnackbarHostState = rememberStackedSnackbarHostState(1)
        AppScaffold(error = state.errorState,
            titleError = state.errorMessage,
            stackedSnackbarHostState = stackedSnackbarHostState,
            onError = {
                LaunchedEffect(state.errorState) {
                    if (state.errorState != null && state.errorMessage.isNotEmpty())
                        stackedSnackbarHostState.showErrorSnackbar(
                            title = "Something happened, try again!",
                            description = state.errorMessage,
                            actionTitle = "Retry",
                        ) {
                            screenModel.retry()
                        }
                }
            }) {
            LaunchedEffect(state.isSuccess) {
                if (state.isSuccess) stackedSnackbarHostState.showSuccessSnackbar(
                    title = "Saved Successfully", duration = StackedSnackbarDuration.Short
                )
            }
            OnRender(state = state, listener = screenModel as SettingInteractionListener)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnRender(
    state: SettingState,
    listener: SettingInteractionListener
) {
    SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier.fillMaxWidth().fillMaxHeight(0.30f)
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = Theme.radius.extraLarge,
                            bottomStart = Theme.radius.extraLarge
                        )
                    )
                    .background(Color(0xFFF53D47))
            )
            Column {
                Spacer(Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                            .noRippleEffect(onClick = listener::onClickBack),
                        tint = Theme.colors.contentPrimary,
                    )
                    Text(
                        text = "Settings",
                        style = Theme.typography.headlineLarge,
                        color = Theme.colors.contentPrimary
                    )
                }
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
        }
    }
}

@Composable
private fun SettingDropDownChoose(
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

@Composable
private fun SettingTextFieldChoose(
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