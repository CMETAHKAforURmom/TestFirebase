package ru.test.andernam.view.components.screens

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.test.andernam.R
import ru.test.andernam.domain.old.User
import ru.test.andernam.view.components.navigateTo
import ru.test.andernam.view.userClass
import javax.inject.Singleton


@Composable
fun BlogComp() {

    Text(text = "HI beach!")

    Row {

        Spacer(modifier = Modifier.width(30.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            User.dialogList?.forEach { elementMassive ->
                CardElementUser(
                    elementMassive[0],
                    elementMassive[1],
                    Uri.parse(elementMassive[2])
                )
            }
        }
    }
}


@Composable
fun CardElementUser(profileLinkL: String, name: String, profileImg: Uri) {


    val message = LocalContext.current.getString(R.string.message_screen)
    Spacer(modifier = Modifier.height(15.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .border(1.dp, Color.DarkGray, RoundedCornerShape(35f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = profileImg, contentDescription = "Image profile",
            modifier = Modifier
                .padding(15.dp)
                .size(64.dp)
                .clip(CircleShape)
        )

        Row(modifier = Modifier.padding(15.dp)) {
            Text(text = name, modifier = Modifier.padding(15.dp))
            Icon(
                Icons.Default.Send,
                contentDescription = "Send",
                modifier = Modifier.clickable {
                    navigateTo(message)
                    userClass.startMessagingWith(profileLinkL)
                    setOpponentData(profileImg, name)
                })
        }
    }
}
