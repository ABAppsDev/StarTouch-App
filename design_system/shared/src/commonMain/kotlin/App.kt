package com.beepbeep.designSystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.composable.BpExpandableTextField
import com.beepbeep.designSystem.ui.composable.StButton
import com.beepbeep.designSystem.ui.composable.StChip
import com.beepbeep.designSystem.ui.composable.StNavigationBar
import com.beepbeep.designSystem.ui.composable.StNavigationBarItem
import com.beepbeep.designSystem.ui.composable.StOutlinedButton
import com.beepbeep.designSystem.ui.composable.StSimpleTextField
import com.beepbeep.designSystem.ui.composable.StSnackBar
import com.beepbeep.designSystem.ui.composable.StTextField
import com.beepbeep.designSystem.ui.composable.StToastMessage
import com.beepbeep.designSystem.ui.composable.StTransparentButton
import com.beepbeep.designSystem.ui.theme.StTheme
import com.beepbeep.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun DesignApp() {
    StTheme {
        Box(
            modifier = Modifier.fillMaxSize().background(Theme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            StSnackBar(
                isVisible = true,
                icon = painterResource(DrawableResource("sun.xml")),
                //message = "kamel"
            ){}
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(DrawableResource("background_pattern.png")),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                EnabledButtonsPreview()
                DisabledButtonsPreview()
                Spacer(modifier = Modifier.height(16.dp))
//            PreviewTextField()
//            PreviewChips()
            //    BottomNavigationBarPreview()
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PreviewChips() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        var selected by remember { mutableStateOf(false) }
        var selected1 by remember { mutableStateOf(true) }
        var selected2 by remember { mutableStateOf(true) }
        StChip(
            label = "Click me",
            onClick = { isSelected -> selected = isSelected },
            isSelected = selected,
            painter = painterResource(
                DrawableResource("sort.xml")
            )
        )
        StChip(
            label = "Click me",
            onClick = { isSelected -> selected1 = isSelected },
            isSelected = selected1,
            painter = painterResource(DrawableResource("sun.xml"))
        )
        StChip(
            label = "Click me",
            onClick = { isSelected -> selected2 = isSelected },
            isSelected = selected2,
            painter = painterResource(DrawableResource("moon_stars.xml"))
        )
    }

}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PreviewTextField() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        var text1 by rememberSaveable { mutableStateOf("") }
        var text2 by rememberSaveable { mutableStateOf("") }
        var text3 by rememberSaveable { mutableStateOf("") }
        var text4 by rememberSaveable { mutableStateOf("Ahmed Nasser") }
        var text5 by rememberSaveable { mutableStateOf("") }
        var text6 by rememberSaveable { mutableStateOf("") }

        StSimpleTextField(
            onValueChange = { text5 = it },
            text = text5,
            hint = "Search for users",
            trailingPainter = painterResource(DrawableResource("sort.xml"))
        )
        StTextField(
            onValueChange = { text3 = it },
            text = text3,
            label = "Email",
            hint = "Enter your Email",
        )
        StTextField(
            onValueChange = { text1 = it },
            text = text1,
            label = "Password",
            hint = "Enter your Password",
            keyboardType = KeyboardType.Password
        )
        StTextField(
            onValueChange = { text2 = it },
            text = text2,
            label = "Username",
            hint = "Enter your Username",
            errorMessage = "incorrect username or password",
        )
        StTextField(
            onValueChange = { text4 = it },
            text = text4,
            label = "FullName",
            hint = "Enter your FullName",
            correctValidation = true
        )
        BpExpandableTextField(
            onValueChange = { text6 = it },
            text = text6,
            label = "FullName",
            hint = "Enter your FullName",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun EnabledButtonsPreview() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StButton(
            title = "Button",
            painter = painterResource(DrawableResource("sun.xml")),
            enabled = true,
            onClick = { },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        StOutlinedButton(
            title = "Button",
            enabled = true,
            onClick = { },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        StTransparentButton(
            title = "Button",
            onClick = { },
            contentColor = Theme.colors.primary
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisabledButtonsPreview() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
    ) {
        StButton(
            title = "Button",
            enabled = false,
            onClick = { },
            modifier = Modifier.fillMaxWidth().weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        StOutlinedButton(
            title = "Button",
            enabled = false,
            onClick = {},
            modifier = Modifier.weight(1f).fillMaxWidth()
        )
    }
}


@Composable
fun BottomNavigationBarPreview() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Overview", "Taxis", "Restaurants", "Users")

    StNavigationBar {
        items.forEachIndexed { index, item ->
            StNavigationBarItem(
                icon = { tint ->
                    Icon(Icons.Filled.Favorite, contentDescription = item, tint = tint)
                },
                label = { style ->
                    Text(item, style = style)
                },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}


expect fun getPlatformName(): String