package presentation.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import presentation.screen.order.OrderScreen
import presentation.screen.splash.SplashScreen
import resource.StarTouchTheme
import util.getScreenModel

@Composable
fun App() {
    MainApp.Content()
}

object MainApp : Screen {
    @Composable
    override fun Content() {
        val appScreenModel = getScreenModel<AppScreenModel>()
        val userLanguage by appScreenModel.state.collectAsState()

        StarTouchTheme(languageCode = userLanguage) {
            Box(
                Modifier.fillMaxSize()
                    .paint(
                        painter = painterResource(DrawableResource("bg.jpg")),
                        contentScale = ContentScale.Crop
                    )
            ) {
                Navigator(SplashScreen) {
                    SlideTransition(it)
                }
            }
        }
    }
}