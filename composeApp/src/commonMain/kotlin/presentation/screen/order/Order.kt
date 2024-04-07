package presentation.screen.order

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun OrdersScreen() {
    val scope = rememberCoroutineScope()
    Scaffold(
        containerColor = Color(0xFF1F1D2B), //todo delete this background color
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Orders #34562",
                        color = Color.White
                    ) //todo remember change text color
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF1F1D2B)) //todo delete this background color
            )
        }
    ) { topBarPadding ->
//        if (cardEmpty)
//            CardEmpty(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(topBarPadding)
//            )
//        else
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF))
                    .padding(topBarPadding)
                    .padding(16.dp),
            ) {
                Column() {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Item",
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth(0.7f),
                            fontSize = 16.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Qty", color = Color.White, fontSize = 16.sp)
                            Text(text = "Price", color = Color.White, fontSize = 16.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                            .height(1.dp)
                            .background(color = Color(0xFF393C49))
                    ) //todo delete this divider but don't forget to give it the same padding
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        items(30) {
                            val dismissState = rememberDismissState(
                                initialValue = DismissValue.Default,
                            )
                            SwipeToDismiss(
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .clip(RoundedCornerShape(16.dp)),
                                state = dismissState,
                                background = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .fillMaxHeight()
                                            .background(Color.Red),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row {
                                            IconButton(onClick = { scope.launch { dismissState.reset() } }) {
                                                Icon(
                                                    Icons.Default.Refresh,
                                                    contentDescription = "Refresh"
                                                )
                                            }
                                            if (dismissState.targetValue == DismissValue.DismissedToStart)
                                                IconButton(onClick = {
                                                    //todo delete
                                                }) {
                                                    Icon(
                                                        Icons.Default.Delete,
                                                        contentDescription = "Delete"
                                                    )
                                                }
                                        }
                                    }
                                },
                                directions = setOf(DismissDirection.EndToStart),
                                dismissContent = {
                                    OrderItem(
                                        title = "Spicy seasoned sea...",
                                        singlePrice = 2.29,
                                        qty = 2,
                                        qtyIncrease = {

                                        },
                                        qtyDecrease = {

                                        },
                                        voided = false,
                                        fired = false
                                    )
                                }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.BottomCenter)
                        .background(Color(0xFF1F1D2B)),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                            .height(1.dp)
                            .background(color = Color(0xFF393C49))
                    ) //todo delete this divider but don't forget to give it the same padding
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Items", color = Color.LightGray, fontSize = 14.sp)
                        Text(text = "0", color = Color.White, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))//todo delete this divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total  price", color = Color.LightGray, fontSize = 14.sp)
                        Text(text = "$" + "21,03", color = Color.White, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(42.dp))//todo delete this divider
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA7C69)),
                        onClick = { } //todo click
                    ) {
                        Text(
                            text = "Fire And Print",
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 14.dp)
                        )
                    }
                }
            }
    }
}

@Composable
fun OrderItem(
    title: String,
    singlePrice: Double,
    qty: Int,
    qtyIncrease: () -> Unit,
    qtyDecrease: () -> Unit,
    voided: Boolean,
    fired: Boolean
) {
    val cardColor = if (voided) Color.Blue else if (fired) Color.Red else Color(0xFF1F1D2B)
    Card(
        modifier = Modifier.clickable { },
        colors = CardDefaults.cardColors(containerColor = cardColor),

        ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(
                    if (!voided || !fired) 0.6f else 0.7f
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    painter = painterResource(DrawableResource("dish.png")),
                    contentDescription = "item image"
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$$singlePrice",
                        color = Color(0xFF889898),
                        fontSize = 12.sp
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!voided || !fired) {
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF2D303E)),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEFE3C8))
                                    .clickable {
                                        qtyIncrease.invoke()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                )
                            }

                            Text(
                                text = qty.toString(),
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEFE3C8))
                                    .clickable {
                                        qtyDecrease.invoke()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "-",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                )
                            }
                        }
                    }
                }
                else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF393C49))
                            .background(Color(0xFF2D303E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = qty.toString(),
                            color = Color.White,
                            fontSize = 14.sp,
                        )
                    }
                }

                Text(
                    text = "$" + (singlePrice * qty).toString(),
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
        }
    }
}