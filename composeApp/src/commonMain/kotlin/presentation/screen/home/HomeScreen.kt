package presentation.screen.home

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.admin
import abapps_startouch.composeapp.generated.resources.dinin
import abapps_startouch.composeapp.generated.resources.exit
import abapps_startouch.composeapp.generated.resources.login
import abapps_startouch.composeapp.generated.resources.logo
import abapps_startouch.composeapp.generated.resources.logout
import abapps_startouch.composeapp.generated.resources.take_away
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.beepbeep.designSystem.ui.composable.StButton
import com.beepbeep.designSystem.ui.composable.StDialogue
import com.beepbeep.designSystem.ui.composable.StOutlinedButton
import com.beepbeep.designSystem.ui.composable.StTextField
import com.beepbeep.designSystem.ui.composable.animate.FadeAnimation
import com.beepbeep.designSystem.ui.composable.snackbar.StackedSnackbarDuration
import com.beepbeep.designSystem.ui.theme.Theme
import exitApplication
import kms
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import presentation.screen.composable.AppScaffold
import presentation.screen.composable.ErrorDialogue
import presentation.screen.composable.IconWithBackground
import presentation.screen.composable.SetLayoutDirection
import presentation.screen.composable.WarningDialogue
import presentation.screen.composable.modifier.bounceClick
import presentation.screen.composable.snackbar.rememberStackedSnackbarHostState
import presentation.screen.dinin.DinInScreen
import presentation.screen.home.util.AttendanceType
import presentation.screen.setting.SettingScreen
import presentation.util.EventHandler
import resource.Resources
import util.getScreenModel

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val homeScreenModel = getScreenModel<HomeScreenModel>()
        val state by homeScreenModel.state.collectAsState()
        val retry = Resources.strings.retry
        val pullRefreshState =
            rememberPullRefreshState(state.isRefresh, { homeScreenModel.retry() })

        FadeAnimation(state.warningDialogueIsVisible) {
            WarningDialogue(
                Resources.strings.warning,
                Resources.strings.doYouWantToCloseApp,
                onDismissRequest = homeScreenModel::onDismissWarningDialogue,
                onClickConfirmButton = { homeScreenModel.exitApp(state.permissionDialogueState.passcode) },
                onClickDismissButton = homeScreenModel::onDismissWarningDialogue
            )
        }
        if (state.exitApp)
            exitApplication()
        EventHandler(homeScreenModel.effect) { effect, navigator ->
            when (effect) {
                is HomeUiEffect.NavigateToDinInScreen -> navigator.push(DinInScreen())
                HomeUiEffect.NavigateToSetting -> navigator.push(SettingScreen())
            }
        }
        FadeAnimation(visible = state.attendanceDialogueState.isVisible) {
            AttendanceDialogue(
                username = state.attendanceDialogueState.username,
                password = state.attendanceDialogueState.password,
                isLoading = state.attendanceDialogueState.isLoading,
                attendanceType = state.attendanceDialogueState.attendanceType,
                homeInteractionListener = homeScreenModel as HomeInteractionListener
            )
        }
        FadeAnimation(visible = state.settingsDialogueState.isVisible) {
            SettingsDialogue(
                username = state.settingsDialogueState.username,
                password = state.settingsDialogueState.password,
                isLoading = state.settingsDialogueState.isLoading,
                homeInteractionListener = homeScreenModel as HomeInteractionListener
            )
        }
        FadeAnimation(visible = state.permissionDialogueState.isVisible) {
            PassCodeDialogue(
                passcode = state.permissionDialogueState.passcode,
                isLoading = state.permissionDialogueState.isLoading,
                homeInteractionListener = homeScreenModel as HomeInteractionListener,
            )
        }
        val stackedSnackbarHostState = rememberStackedSnackbarHostState(maxStack = 1)

        AppScaffold(
            isLoading = state.isLoading,
            stackedSnackbarHostState = stackedSnackbarHostState,
            error = state.errorHomeDetailsState,
            onError = {
                LaunchedEffect(state.errorHomeDetailsState) {
                    if (state.errorHomeDetailsState != null && state.errorMessage.isNotEmpty())
                        stackedSnackbarHostState.showErrorSnackbar(
                            title = "Something happened, try again!",
                            description = state.errorMessage,
                            actionTitle = retry,
                        ) {
                            homeScreenModel.retry()
                        }
                }
            }
        ) {
            LaunchedEffect(state.errorState) {
                if (state.errorState != null && state.errorMessage.isNotEmpty())
                    homeScreenModel.showErrorDialogue()
            }
            LaunchedEffect(state.attendanceDialogueState.message) {
                delay(500)
                if (state.attendanceDialogueState.message.isNotEmpty())
                    stackedSnackbarHostState.showSuccessSnackbar(
                        title = state.attendanceDialogueState.message,
                        duration = StackedSnackbarDuration.Short
                    )
            }
            FadeAnimation(state.errorDialogueIsVisible) {
                ErrorDialogue(
                    title = Resources.strings.error,
                    text = state.errorMessage,
                    onDismissRequest = homeScreenModel::onDismissErrorDialogue,
                    onClickConfirmButton = homeScreenModel::onDismissErrorDialogue,
                )
            }
            OnRender(
                state = state,
                listener = homeScreenModel as HomeInteractionListener,
                pullRefreshState = pullRefreshState
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun OnRender(
    state: HomeState,
    listener: HomeInteractionListener,
    pullRefreshState: PullRefreshState
) {
    Box(
        Modifier.fillMaxSize().pullRefresh(pullRefreshState).verticalScroll(rememberScrollState())
    ) {
        PullRefreshIndicator(
            state.isRefresh,
            pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color(0xFF8D7B4B)
        )
        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            Row(
                modifier = Modifier.fillMaxSize().padding(16.kms),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.kms)
                ) {
                    IconWithBackground(
                        icon = painterResource(Res.drawable.login),
                        contentDescription = Resources.strings.login,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { listener.onClickLogOn() },
                        iconSize = 65.kms,
                    )
                    IconWithBackground(
                        icon = painterResource(Res.drawable.logout),
                        contentDescription = Resources.strings.logout,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { listener.onClickLogOff() },
                        iconSize = 65.kms,
                    )
                    IconWithBackground(
                        icon = painterResource(Res.drawable.admin),
                        contentDescription = Resources.strings.admin,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { listener.onClickSettings() },
                        iconSize = 65.kms,
                    )
                }

                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = Resources.strings.logo,
                    modifier = Modifier.size(128.kms),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.kms)
                ) {
                    IconWithBackground(
                        icon = painterResource(Res.drawable.dinin),
                        contentDescription = Resources.strings.dinningIn,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { listener.onClickDinIn() },
                        iconSize = 65.kms,
                    )
                    IconWithBackground(
                        icon = painterResource(Res.drawable.take_away),
                        contentDescription = Resources.strings.settings,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { /*ToDo*/ },
                        iconSize = 65.kms,
                    )
                    IconWithBackground(
                        icon = painterResource(Res.drawable.exit),
                        contentDescription = Resources.strings.exit,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { listener.onClickExitApp() },
                        iconSize = 65.kms
                    )
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.kms)
        ) {
            Text(
                text = "${Resources.strings.outlet}: ${state.homeDetailsState.outletName}",
                style = Theme.typography.headlineLarge,
                color = Theme.colors.contentPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = state.homeDetailsState.systemDate,
                style = Theme.typography.headlineLarge,
                color = Theme.colors.contentPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AttendanceDialogue(
    username: String,
    password: String,
    isLoading: Boolean,
    attendanceType: String,
    homeInteractionListener: HomeInteractionListener,
    modifier: Modifier = Modifier,
) {
    StDialogue(
        onDismissRequest = homeInteractionListener::onDismissAttendanceDialogue,
        modifier = modifier
    ) {
        Text(
            text = Resources.strings.enterYourInfo,
            style = Theme.typography.headline,
            color = Theme.colors.contentPrimary,
        )
        StTextField(
            modifier = Modifier.padding(top = 40.dp),
            label = Resources.strings.userName,
            text = username,
            hint = Resources.strings.userName,
            onValueChange = homeInteractionListener::onUserNameChanged,
            imeAction = ImeAction.Next,
        )
        StTextField(
            modifier = Modifier.padding(top = 24.dp),
            label = Resources.strings.password,
            text = password,
            hint = Resources.strings.password,
            onValueChange = homeInteractionListener::onPasswordChanged,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Go,
            keyboardActions = KeyboardActions(onGo = {
                if (attendanceType == AttendanceType.LOGIN.name) homeInteractionListener.onClickLogin()
                else homeInteractionListener.onClickLogout()
            })
        )
        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StOutlinedButton(
                    title = Resources.strings.cancel,
                    onClick = {
                        homeInteractionListener.onDismissAttendanceDialogue()
                    },
                    modifier = Modifier.weight(1f)
                )
                StButton(
                    title = Resources.strings.ok,
                    onClick = {
                        if (attendanceType == AttendanceType.LOGIN.name) homeInteractionListener.onClickLogin()
                        else homeInteractionListener.onClickLogout()
                    },
                    modifier = Modifier.weight(1f),
                    isLoading = isLoading,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDialogue(
    username: String,
    password: String,
    isLoading: Boolean,
    homeInteractionListener: HomeInteractionListener,
    modifier: Modifier = Modifier,
) {
    StDialogue(
        onDismissRequest = homeInteractionListener::onDismissSettingsDialogue,
        modifier = modifier
    ) {
        Text(
            text = Resources.strings.enterYourInfo,
            style = Theme.typography.headline,
            color = Theme.colors.contentPrimary,
        )
        StTextField(
            modifier = Modifier.padding(top = 40.dp),
            label = Resources.strings.userName,
            text = username,
            hint = Resources.strings.userName,
            onValueChange = homeInteractionListener::onUserNameChanged,
        )
        StTextField(
            modifier = Modifier.padding(top = 24.dp),
            label = Resources.strings.password,
            text = password,
            hint = Resources.strings.password,
            onValueChange = homeInteractionListener::onPasswordChanged,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Go,
            keyboardActions = KeyboardActions(onGo = {
                homeInteractionListener.onClickSettingsOk()
            })
        )
        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StOutlinedButton(
                    title = Resources.strings.cancel,
                    onClick = {
                        homeInteractionListener.onDismissSettingsDialogue()
                    },
                    modifier = Modifier.weight(1f)
                )
                StButton(
                    title = Resources.strings.ok,
                    onClick = {
                        homeInteractionListener.onClickSettingsOk()
                    },
                    modifier = Modifier.weight(1f),
                    isLoading = isLoading,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PassCodeDialogue(
    passcode: String,
    isLoading: Boolean,
    homeInteractionListener: HomeInteractionListener,
    modifier: Modifier = Modifier,
) {
    StDialogue(
        onDismissRequest = homeInteractionListener::onDismissPermissionDialogue,
        modifier = modifier
    ) {
        Text(
            text = Resources.strings.enterYourPasscode,
            style = Theme.typography.headline,
            color = Theme.colors.contentPrimary,
        )
        StTextField(
            modifier = Modifier.padding(top = 24.dp),
            label = Resources.strings.passcode,
            text = passcode,
            hint = Resources.strings.passcode,
            onValueChange = homeInteractionListener::onPassCodeChanged,
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Go,
            keyboardActions = KeyboardActions(onGo = {
                homeInteractionListener.onClickEnterPasscode()
            })
        )
        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StOutlinedButton(
                    title = Resources.strings.cancel,
                    onClick = {
                        homeInteractionListener.onDismissPermissionDialogue()
                    },
                    modifier = Modifier.weight(1f)
                )
                StButton(
                    title = Resources.strings.ok,
                    onClick = {
                        homeInteractionListener.onClickEnterPasscode()
                    },
                    modifier = Modifier.weight(1f),
                    isLoading = isLoading,
                )
            }
        }
    }
}