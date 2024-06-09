package presentation.screen.takeaway

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import presentation.screen.takeaway.order.OrderTakeAwayScreen

class TakeAwayScreen : Screen {
    @Composable
    override fun Content() {
        LocalNavigator.currentOrThrow.push(OrderTakeAwayScreen(1L, 1, emptyList(), false))
    }
}