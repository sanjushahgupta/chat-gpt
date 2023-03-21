package com.digitalaya.chat.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.digitalaya.chat.R
import com.digitalaya.chat.util.UserPreference
import kotlinx.coroutines.launch

@Composable
fun ChangeKey(changeKey: String, navController: NavController) {
    ChangeApiKey(navController, changeKey)
}

@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation",
    "UnusedMaterialScaffoldPaddingParameter"
)
@Composable
fun ChangeApiKey(navController: NavController, changeKey: String) {
    val saveButtonClicked = remember { mutableStateOf(false) }
    val apiKey = remember { mutableStateOf(changeKey) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = UserPreference(context)
    val focus = LocalFocusManager.current
    val interactionSource = MutableInteractionSource()
   // Scaffold(topBar = { TopAppBar() })
  //  {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { focus.clearFocus() }, Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                //ApiKeyDiagram()
                Spacer(modifier = Modifier.height(20.dp))
                Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "To use this application, you need to enter your OpenAI API key. The key will be saved locally on your device.",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(
                            value = apiKey.value, onValueChange = { apiKey.value = it },
                            label = { Text("OpenAI API key", style = TextStyle(Color.Gray)) },
                            singleLine = false,
                           // placeholder = { Text("Enter an OpenAI API key.") },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Gray,
                                cursorColor = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.padding(top = 15.dp))

                        CreateChatGptApiKeyLink(
                            "https://platform.openai.com",
                            "Click here to create an OpenAI API key"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { saveButtonClicked.value = true },
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                        ) {
                            Text("Save", color = Color.Gray)
                        }
                    }
                }


            }
        }
        if (saveButtonClicked.value && apiKey.value.isEmpty()) {
            Toast.makeText(
                LocalContext.current,
                "Please enter your chat gpt API key.",
                Toast.LENGTH_SHORT
            ).show()
            saveButtonClicked.value = false
        } else if (saveButtonClicked.value && !apiKey.value.isEmpty()) {

            scope.launch {

                dataStore.savePlatformIndexStatus(apiKey.value)
            }
            navController.navigate("Chat")

            saveButtonClicked.value = false

        }

  //  }
    DisclaimerText()
}

@Composable
fun CreateChatGptApiKeyLink(url: String, text: String) {
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = Color.Blue
            ),
        ) {
            append(text)
        }
        pushStringAnnotation(tag = "url", annotation = url)
    }
    val uriHandler = LocalUriHandler.current

    Text(
        text = annotatedText,
        modifier = Modifier.clickable(
            onClick = { ->
                annotatedText.getStringAnnotations(
                    tag = "url",
                    start = 0,
                    end = 50,
                ).firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
            }
        )
    )
}
@Composable
private fun DisclaimerText() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), verticalArrangement = Arrangement.Bottom
    ) {
        Text("Disclaimer: This application is not affiliated with OpenAI.", color = Color.LightGray)
    }

}
@Composable()
fun TopAppBar() {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        backgroundColor = Color.DarkGray

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "ChatAI",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.LogiTint)
                )
                Image(
                    painter = painterResource(id = R.drawable.baseline_api_24),
                    contentDescription = "",
                )
            }

        }
    }
}