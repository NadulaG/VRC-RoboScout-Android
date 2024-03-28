package com.sunkensplashstudios.VRCRoboScout

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.sunkensplashstudios.VRCRoboScout.ui.theme.*

import io.mhssn.colorpicker.ColorPickerDialog
import io.mhssn.colorpicker.ColorPickerType

var selectedIndex by mutableStateOf(0)
val competitions = listOf("VRC MS", "VRC HS", "VEX U")

val seasons: List<String> = if (selectedIndex == 0 || selectedIndex == 1) {
    API.seasonIdMap[0].values.toList()
} else {
    API.seasonIdMap[1].values.toList()
}
//val seasons = listOf("Over under", "Spin up", "Tipping point", "tt", "ttt")
var expanded by mutableStateOf(false)
var text by mutableStateOf(seasons[0])

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Destination
@Composable
fun SettingsView(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.topContainer,
                    titleContentColor = MaterialTheme.colorScheme.onTopContainer,
                ),
                title = {
                    Text("Settings", fontWeight = FontWeight.Bold)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                Text(
                    "COMPETITION",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Card(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f),
                        disabledContainerColor = Color.Unspecified.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContentColor = Color.Unspecified
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SingleChoiceSegmentedButtonRow {
                            competitions.forEachIndexed { index, label ->
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = index,
                                        count = competitions.size
                                    ),
                                    onClick = { selectedIndex = index },
                                    selected = index == selectedIndex,
                                    modifier = Modifier.width(120.dp)
                                ) {
                                    Text(label)
                                }
                            }
                        }
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                        ) {
                            TextField(
                                // The `menuAnchor` modifier must be passed to the text field for correctness.
                                modifier = Modifier.menuAnchor(),
                                value = text,
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                seasons.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                                        onClick = {
                                            text = option
                                            expanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Text(
                    "APPEARANCE",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Card(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f),
                        disabledContainerColor = Color.Unspecified.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContentColor = Color.Unspecified
                    )
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        val localContext = LocalContext.current
                        val userSettings = UserSettings(localContext)

                        // Top Bar Color (MaterialTheme.colorScheme.topContainer)
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            var showDialog by remember { mutableStateOf(false) }
                            var topContainerColor by remember { mutableStateOf(userSettings.getTopContainerColor()) }
                            var update by remember { mutableStateOf(false) }

                            ColorPickerDialog(
                                show = showDialog,
                                type = ColorPickerType.Circle(
                                    showBrightnessBar = true,
                                    showAlphaBar = false,
                                    lightCenter = true
                                ),
                                onDismissRequest = {
                                    showDialog = false
                                },
                                onPickedColor = {
                                    topContainerColor = it
                                    userSettings.setTopContainerColor(it)
                                    update = true
                                    showDialog = false
                                }
                            )

                            if (update) {
                                MaterialTheme.colorScheme.topContainer = topContainerColor
                                val view = LocalView.current
                                if (!view.isInEditMode && !userSettings.getMinimalisticMode()) {
                                    SideEffect {
                                        val window = (view.context as Activity).window
                                        window.statusBarColor = topContainerColor.toArgb()
                                        window.navigationBarColor = topContainerColor.toArgb()
                                    }
                                }
                                update = false
                            }

                            Text("Top Bar Color", modifier = Modifier.weight(1f))
                            Text("Change",
                                color = if (userSettings.getTopContainerColor().isSpecified) {
                                    userSettings.getTopContainerColor()
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                                modifier = Modifier.clickable {
                                    showDialog = true
                                }
                            )
                        }
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        )
                        // Top Bar Content Color (MaterialTheme.colorScheme.onTopContainer)
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            var showDialog by remember { mutableStateOf(false) }
                            var onTopContainerColor by remember { mutableStateOf(userSettings.getOnTopContainerColor()) }
                            var update by remember { mutableStateOf(false) }

                            ColorPickerDialog(
                                show = showDialog,
                                type = ColorPickerType.Circle(
                                    showBrightnessBar = true,
                                    showAlphaBar = false,
                                    lightCenter = true
                                ),
                                onDismissRequest = {
                                    showDialog = false
                                },
                                onPickedColor = {
                                    onTopContainerColor = it
                                    userSettings.setOnTopContainerColor(it)
                                    update = true
                                    showDialog = false
                                }
                            )

                            if (update) {
                                MaterialTheme.colorScheme.onTopContainer = onTopContainerColor
                                update = false
                            }

                            Text("Top Bar Content Color", modifier = Modifier.weight(1f))
                            Text("Change",
                                color = if (userSettings.getOnTopContainerColor().isSpecified) {
                                    userSettings.getOnTopContainerColor()
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                                modifier = Modifier.clickable {
                                    showDialog = true
                                }
                            )
                        }
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        )
                        // Button Color (MaterialTheme.colorScheme.button)
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            var showDialog by remember { mutableStateOf(false) }
                            var buttonColor by remember { mutableStateOf(userSettings.getButtonColor()) }
                            var update by remember { mutableStateOf(false) }

                            ColorPickerDialog(
                                show = showDialog,
                                type = ColorPickerType.Circle(
                                    showBrightnessBar = true,
                                    showAlphaBar = false,
                                    lightCenter = true
                                ),
                                onDismissRequest = {
                                    showDialog = false
                                },
                                onPickedColor = {
                                    buttonColor = it
                                    userSettings.setButtonColor(it)
                                    update = true
                                    showDialog = false
                                }
                            )

                            if (update) {
                                MaterialTheme.colorScheme.button = buttonColor
                                update = false
                            }

                            Text("Button and Tab Color", modifier = Modifier.weight(1f))
                            Text("Change",
                                color = MaterialTheme.colorScheme.button,
                                modifier = Modifier.clickable {
                                    showDialog = true
                                }
                            )
                        }
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        )
                        var minimalistic by remember { mutableStateOf(userSettings.getMinimalisticMode()) }
                        // Minimalistic Mode (Switch)
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            var update by remember { mutableStateOf(false) }

                            if (minimalistic && update) {
                                val background = MaterialTheme.colorScheme.background
                                MaterialTheme.colorScheme.topContainer = background
                                val view = LocalView.current
                                if (!view.isInEditMode) {
                                    SideEffect {
                                        val window = (view.context as Activity).window
                                        window.statusBarColor = background.toArgb()
                                        window.navigationBarColor = background.toArgb()
                                    }
                                }
                                update = false
                            }
                            else if (!minimalistic && update) {
                                MaterialTheme.colorScheme.topContainer = if (userSettings.getTopContainerColor().isSpecified) {
                                    userSettings.getTopContainerColor()
                                } else {
                                    MaterialTheme.colorScheme.primaryContainer
                                }
                                val view = LocalView.current
                                if (!view.isInEditMode) {
                                    SideEffect {
                                        val window = (view.context as Activity).window
                                        window.statusBarColor = userSettings.getTopContainerColor().toArgb()
                                        window.navigationBarColor = userSettings.getTopContainerColor().toArgb()
                                    }
                                }
                                update = false
                            }

                            Text("Minimalistic", modifier = Modifier.weight(1f))
                            Switch(
                                checked = minimalistic,
                                modifier = Modifier
                                    .size(44.dp, 24.dp)
                                    .padding(end = 10.dp),
                                onCheckedChange = {
                                    minimalistic = it
                                    userSettings.setMinimalisticMode(it)
                                    update = true
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.button,
                                    checkedTrackColor = MaterialTheme.colorScheme.button.copy(alpha = 0.5f),
                                    uncheckedThumbColor = MaterialTheme.colorScheme.button,
                                    uncheckedTrackColor = MaterialTheme.colorScheme.button.copy(alpha = 0f)
                                )
                            )
                        }
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        )
                        // Reset to Default
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            var reset by remember { mutableStateOf(false) }

                            if (reset) {
                                MaterialTheme.colorScheme.topContainer = MaterialTheme.colorScheme.primaryContainer
                                MaterialTheme.colorScheme.onTopContainer = MaterialTheme.colorScheme.onPrimaryContainer
                                MaterialTheme.colorScheme.button = MaterialTheme.colorScheme.primary
                                val view = LocalView.current
                                val colorScheme = MaterialTheme.colorScheme
                                if (!view.isInEditMode) {
                                    SideEffect {
                                        val window = (view.context as Activity).window
                                        window.statusBarColor = colorScheme.primaryContainer.toArgb()
                                        window.navigationBarColor = colorScheme.primaryContainer.toArgb()
                                    }
                                }
                                reset = false
                            }

                            Text("Reset to Default", modifier = Modifier.weight(1f))
                            Text("Reset",
                                color = MaterialTheme.colorScheme.button,
                                modifier = Modifier.clickable {
                                    userSettings.resetColors()
                                    userSettings.setMinimalisticMode(true)
                                    minimalistic = true
                                    reset = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}