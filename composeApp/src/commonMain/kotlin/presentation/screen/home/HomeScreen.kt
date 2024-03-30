package presentation.screen.home

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import exitApplication
import kms
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import presentation.screen.composable.AppButton
import presentation.screen.composable.AppDialogue
import presentation.screen.composable.AppScaffold
import presentation.screen.composable.AppTextField
import presentation.screen.composable.ErrorDialogue
import presentation.screen.composable.IconWithBackground
import presentation.screen.composable.SetLayoutDirection
import presentation.screen.composable.WarningDialogue
import presentation.screen.composable.animate.FadeAnimation
import presentation.screen.composable.modifier.bounceClick
import presentation.screen.composable.snackbar.StackedSnackbarDuration
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
                        icon = painterResource(DrawableResource("login.xml")),
                        contentDescription = Resources.strings.login,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { listener.onClickLogOn() },
                        iconSize = 65.kms
                    )
                    IconWithBackground(
                        icon = painterResource(DrawableResource("logout.xml")),
                        contentDescription = Resources.strings.logout,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { listener.onClickLogOff() },
                        iconSize = 65.kms
                    )
                    IconWithBackground(
                        icon = painterResource(DrawableResource("admin.xml")),
                        contentDescription = Resources.strings.admin,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { listener.onClickSettings() },
                        iconSize = 65.kms
                    )
                }

                Image(
                    painter = painterResource(DrawableResource("logo.png")),
                    contentDescription = Resources.strings.logo,
                    modifier = Modifier.size(128.kms),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.kms)
                ) {
                    IconWithBackground(
                        icon = painterResource(DrawableResource("dinin.png")),
                        contentDescription = Resources.strings.dinningIn,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { listener.onClickDinIn() },
                        iconSize = 65.kms
                    )
                    IconWithBackground(
                        icon = painterResource(DrawableResource("take_away.png")),
                        contentDescription = Resources.strings.settings,
                        modifier = Modifier.size(80.kms)
                            .bounceClick { /*ToDo*/ },
                        iconSize = 65.kms
                    )
                    IconWithBackground(
                        icon = painterResource(DrawableResource("exit.xml")),
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
                fontSize = 32.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = state.homeDetailsState.systemDate,
                fontSize = 32.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}


@Composable
private fun AttendanceDialogue(
    username: String,
    password: String,
    isLoading: Boolean,
    attendanceType: String,
    homeInteractionListener: HomeInteractionListener,
    modifier: Modifier = Modifier,
) {
    AppDialogue(
        onDismissRequest = homeInteractionListener::onDismissAttendanceDialogue,
        modifier = modifier
    ) {
        Text(
            Resources.strings.enterYourInfo,
            modifier = Modifier.padding(vertical = 8.kms),
            style = MaterialTheme.typography.headlineSmall
        )
        AppTextField(
            text = username,
            onValueChange = homeInteractionListener::onUserNameChanged,
            hint = Resources.strings.userName,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.kms, vertical = 8.kms),
        )
        AppTextField(
            text = password,
            onValueChange = homeInteractionListener::onPasswordChanged,
            hint = Resources.strings.password,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.kms),
            keyboardType = KeyboardType.Password,
        )
        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.kms, vertical = 16.kms),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.kms)
            ) {
                AppButton(
                    Resources.strings.cancel,
                    modifier = Modifier.weight(1f),
                ) { homeInteractionListener.onDismissAttendanceDialogue() }
                AppButton(
                    Resources.strings.ok,
                    modifier = Modifier.weight(1f),
                    isLoading = isLoading,
                ) {
                    if (attendanceType == AttendanceType.LOGIN.name) homeInteractionListener.onClickLogin()
                    else homeInteractionListener.onClickLogout()
                }
            }
        }
    }
}

@Composable
private fun SettingsDialogue(
    username: String,
    password: String,
    isLoading: Boolean,
    homeInteractionListener: HomeInteractionListener,
    modifier: Modifier = Modifier,
) {
    AppDialogue(
        onDismissRequest = homeInteractionListener::onDismissSettingsDialogue,
        modifier = modifier
    ) {
        Text(
            Resources.strings.enterYourInfo,
            modifier = Modifier.padding(vertical = 8.kms),
            style = MaterialTheme.typography.headlineSmall
        )
        AppTextField(
            text = username,
            onValueChange = homeInteractionListener::onUserNameChanged,
            hint = Resources.strings.userName,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.kms, vertical = 8.kms),
        )
        AppTextField(
            text = password,
            onValueChange = homeInteractionListener::onPasswordChanged,
            hint = Resources.strings.password,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.kms),
            keyboardType = KeyboardType.Password,
        )
        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.kms, vertical = 16.kms),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.kms)
            ) {
                AppButton(
                    Resources.strings.cancel,
                    modifier = Modifier.weight(1f),
                ) { homeInteractionListener.onDismissSettingsDialogue() }
                AppButton(
                    Resources.strings.ok,
                    modifier = Modifier.weight(1f),
                    isLoading = isLoading,
                ) {
                    homeInteractionListener.onClickSettingsOk()
                }
            }
        }
    }
}

@Composable
private fun PassCodeDialogue(
    passcode: String,
    isLoading: Boolean,
    homeInteractionListener: HomeInteractionListener,
    modifier: Modifier = Modifier,
) {
    AppDialogue(
        onDismissRequest = homeInteractionListener::onDismissPermissionDialogue,
        modifier = modifier
    ) {
        Text(
            Resources.strings.enterYourPasscode,
            modifier = Modifier.padding(vertical = 8.kms),
            style = MaterialTheme.typography.headlineSmall
        )
        AppTextField(
            text = passcode,
            onValueChange = homeInteractionListener::onPassCodeChanged,
            hint = Resources.strings.passcode,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.kms),
            keyboardType = KeyboardType.NumberPassword,
        )
        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.kms, vertical = 16.kms),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.kms)
            ) {
                AppButton(
                    Resources.strings.cancel,
                    modifier = Modifier.weight(1f),
                ) { homeInteractionListener.onDismissPermissionDialogue() }
                AppButton(
                    Resources.strings.ok,
                    modifier = Modifier.weight(1f),
                    isLoading = isLoading,
                ) { homeInteractionListener.onClickEnterPasscode() }
            }
        }
    }
}