package presentation.screen.setting

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import presentation.screen.composable.AppButton
import presentation.screen.composable.AppCheckBox
import presentation.screen.composable.AppScaffold
import presentation.screen.composable.AppTextField
import presentation.screen.composable.DropDownState
import presentation.screen.composable.DropDownTextField
import presentation.screen.composable.SetLayoutDirection
import presentation.screen.composable.animate.SlideAnimation
import presentation.screen.composable.snackbar.StackedSnackbarDuration
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

        val stackedSnackbarHostState = rememberStackedSnackbarHostState()
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

@Composable
private fun OnRender(
    state: SettingState, listener: SettingInteractionListener
) {
    SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = Color(0xFF11142D))
                .verticalScroll(rememberScrollState()),
        ) {
            SlideAnimation(
                visible = state.restaurants.isNotEmpty(),
                enter = slideInHorizontally { -it } + fadeIn(tween(600)),
                exit = slideOutHorizontally { it } + fadeOut(tween(600)),
            ) {
                SettingDropDownChoose(
                    title = "Restaurant : ",
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
                    title = "Outlet : ",
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
                    title = "Dining Room : ",
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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AppCheckBox(
                    checked = state.isCallCenter,
                    title = "Call Center",
                    onCheckedChange = listener::onSelectedCallCenter,
                )
                AppCheckBox(
                    checked = state.isQuickSaleLoopBack,
                    title = "Quick sale loop back",
                    onCheckedChange = listener::onQuickLoopBackSelected,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppButton(
                    title = "Close",
                    onClick = listener::onClickClose,
                    modifier = Modifier.weight(1f),
                )
                AppButton(
                    title = "Save",
                    onClick = listener::onClickSave,
                    modifier = Modifier.weight(1f),
                    isLoading = state.isLoading
                )
            }
        }
    }
}

@Composable
private fun SettingDropDownChoose(
    title: String,
    options: List<DropDownState>,
    selectedItem: DropDownState,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) {
    Row(
        modifier.fillMaxWidth().padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            title,
            Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        DropDownTextField(
            modifier = Modifier.padding(start = 8.dp).padding(vertical = 16.dp),
            options = options,
            selectedItem = selectedItem
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
        Text(
            title,
            Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        AppTextField(
            text = text,
            onValueChange = onValueChanged,
            hint = hint,
            keyboardType = keyboardType,
            modifier = Modifier.padding(start = 8.dp).padding(bottom = 16.dp),
        )
    }
}