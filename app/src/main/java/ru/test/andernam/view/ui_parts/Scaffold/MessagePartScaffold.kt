package ru.test.andernam.view.ui_parts.Scaffold

import android.net.Uri
import android.view.MotionEvent
import android.view.View
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.test.andernam.view.components.Routes
import ru.test.andernam.view.components.navigateTo
import ru.test.andernam.view.userClass

var opponentImage = mutableStateOf(Uri.EMPTY)
var opponentName = mutableStateOf("")

fun setOpponentData(opponentImageGet: MutableState<Uri>, opponentNameGet: MutableState<String>){
    opponentImage = opponentImageGet
    opponentName = opponentNameGet
}

@Composable
fun TopMessageScaffold() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .size(64.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.clickable {
            navigateTo(Routes.Back)
        })
        AsyncImage(
            model = opponentImage.value, contentDescription = "Image profile",
            modifier = Modifier
                .padding(15.dp)
                .size(54.dp)
                .clip(CircleShape)
        )
        Text(text = opponentName.value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomMessageScaffold() {

    var message by remember{
        mutableStateOf("")
    }
Row(modifier = Modifier
        .height(135.dp)
        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        OutlinedTextField(value = message, onValueChange = {message = it}, modifier = Modifier
            .fillMaxWidth(0.8f)
            .border(1.dp, Color.DarkGray, RoundedCornerShape(35f)))
        Icon(imageVector = Icons.Default.Send, contentDescription = "Send action", modifier = Modifier
            .clickable {
                userClass.sendMessage(message)
                message = ""
            }
            .size(42.dp))
    }
}