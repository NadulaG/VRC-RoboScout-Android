package com.sunkensplashstudios.VRCRoboScout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun TrueSkillView(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("World TrueSkill", fontWeight = FontWeight.Bold)
                }
            )
        }
    ) { padding ->
        var importing by rememberSaveable { mutableStateOf(!API.importedVDA) }

        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.Default).launch {
                while (!API.importedVDA) {
                    continue
                }
                withContext(Dispatchers.Main) {
                    importing = false
                }
            }
        }

        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {

            if (importing) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(50.dp).padding(10.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    Text("Importing Data")
                }
            }
            else {
                Card(modifier = Modifier.padding(10.dp)) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        items(API.vdaCache) { vdaEntry ->

                            var expanded by remember { mutableStateOf(false) }

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.width(130.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("#" + vdaEntry.tsRanking.toString(), fontSize = 18.sp)
                                    if ((vdaEntry.rankingChange ?: 0.0) != 0.0) {
                                        Icon(
                                            imageVector = if ((vdaEntry.rankingChange
                                                    ?: 0.0) > 1.0
                                            ) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                                            contentDescription = "${if ((vdaEntry.rankingChange ?: 0.0) >= 0.0) "Up" else "Down"} ${abs(vdaEntry.rankingChange ?: 0.0).toInt()} places since last update",
                                            tint = if ((vdaEntry.rankingChange
                                                    ?: 0.0) > 1.0
                                            ) Color(0xFF028A0F) else Color.Red,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(abs(vdaEntry.rankingChange?.toInt() ?: 0).toString(), fontSize = 16.sp, color = if ((vdaEntry.rankingChange
                                                ?: 0.0) > 1.0
                                        ) Color(0xFF028A0F) else Color.Red, modifier = Modifier.padding(start = 4.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.weight(1.0f))
                                Text(vdaEntry.teamNumber, fontSize = 18.sp)
                                Spacer(modifier = Modifier.weight(1.0f))
                                Row(
                                    modifier = Modifier.width(130.dp)
                                ) {
                                    Spacer(modifier = Modifier.weight(1.0f))
                                    Text(vdaEntry.trueskill.toString(), fontSize = 18.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable {
                                        expanded = !expanded
                                    })
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("CCWM: ${vdaEntry.ccwm}") },
                                            onClick = { }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Total Wins: ${vdaEntry.totalWins.toInt()}") },
                                            onClick = { }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Total Losses: ${vdaEntry.totalLosses.toInt()}") },
                                            onClick = { }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Total Ties: ${vdaEntry.totalTies.toInt()}") },
                                            onClick = { }
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}