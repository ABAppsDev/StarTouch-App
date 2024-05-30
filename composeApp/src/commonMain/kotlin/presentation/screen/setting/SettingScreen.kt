package presentation.screen.setting

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.ic_back
import abapps_startouch.composeapp.generated.resources.logo
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.beepbeep.designSystem.ui.composable.modifier.noRippleEffect
import com.beepbeep.designSystem.ui.composable.snackbar.StackedSnackbarDuration
import com.beepbeep.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.painterResource
import presentation.screen.composable.AppScaffold
import presentation.screen.composable.SetLayoutDirection
import presentation.screen.composable.forms.Adjusments
import presentation.screen.composable.forms.CallCenterStation
import presentation.screen.composable.forms.ConvertPoints
import presentation.screen.composable.forms.Discounts
import presentation.screen.composable.forms.ModifiersGroup
import presentation.screen.composable.forms.OutLetAdjusment
import presentation.screen.composable.forms.OutLetTax
import presentation.screen.composable.forms.OutletsForm
import presentation.screen.composable.forms.PrinterForm
import presentation.screen.composable.forms.Promotions
import presentation.screen.composable.forms.Taxes
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

        AppScaffold(
            error = state.errorState,
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
            }
        ) {
            LaunchedEffect(state.isSuccess) {
                if (state.isSuccess)
                    stackedSnackbarHostState.showSuccessSnackbar(
                        title = "Saved Successfully",
                        duration = StackedSnackbarDuration.Short
                    )
            }
            OnRender(state = state, listener = screenModel as SettingInteractionListener)
        }
    }
}

@Composable
private fun OnRender(
    state: SettingState,
    listener: SettingInteractionListener
) {
    SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
        Box(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
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
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleEffect(onClick = listener::onClickBack),
                        tint = Theme.colors.contentPrimary,
                    )
                    Text(
                        text = "Settings",
                        style = Theme.typography.headlineLarge,
                        color = Theme.colors.contentPrimary
                    )
                }

         Adjusments(
                    " ",
                "",
               image = painterResource(Res.drawable.logo),
               "",
               "",
               state, listener
                )
            }
        }
    }
}