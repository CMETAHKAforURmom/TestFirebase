package ru.test.andernam.view.ui_parts.Scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BottomBarNavigation(actionsAndDescription: Map<String, () -> Unit>){
    Row (horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()){
        actionsAndDescription.forEach { indexMap ->
            BottomNavigationItemBtn(action = indexMap.value, description = indexMap.key)
        }
    }
}

@Composable
fun BottomNavigationItemBtn(action: () -> Unit, description: String){
    IconButton(onClick = (action), Modifier.heightIn(50.dp).widthIn(80.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(findIconByDescription(description = description), contentDescription = description)
            Text(text = description)
        }
    }
}

@Composable
fun findIconByDescription(description: String): ImageVector{
    when(description){
        "Users" -> return Icons.Default.Send
        "Profile" -> return Icons.Default.AccountBox
        else -> return Icons.Default.Build
    }
}