package com.digitalaya.chat.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.digitalaya.chat.R
import com.digitalaya.chat.util.UserPreference
import com.digitalaya.chat.viewModel.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint(
    "UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation",
    "CoroutineCreationDuringComposition"
)
@Composable
fun ChatScreen(navController: NavController, viewModel: ChatViewModel) {
    val focus = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStore = UserPreference(context)
    val globalApiKey = remember { mutableStateOf("SAMPLE_KEY") }

    scope.launch {
        dataStore.apiKeyStore.collect {
            scope.launch {
                globalApiKey.value = it.toString()
                delay(700)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarContent(navController, globalApiKey.value)
        }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val enterQuestion = remember {
                mutableStateOf("")
            }
            val isLoading = remember { mutableStateOf(false) }

            viewModel.loadingBoolean.observeAsState().value?.let {
                isLoading.value = it
            }

            val buttonClick = remember { mutableStateOf(false) }
            val interactionSource = MutableInteractionSource()

            ChatScreenBody(
                interactionSource,
                focus,
                isLoading,
                viewModel,
                enterQuestion,
                buttonClick,
                globalApiKey
            )
        }
    }
}

@Composable
private fun ChatScreenBody(
    interactionSource: MutableInteractionSource,
    focus: FocusManager,
    isLoading: MutableState<Boolean>,
    viewModel: ChatViewModel,
    enterQuestion: MutableState<String>,
    buttonClick: MutableState<Boolean>,
    globalApiKey: MutableState<String>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focus.clearFocus()
            },
        verticalArrangement = Arrangement.Top

    ) {
        ChatResponseCardView(isLoading, viewModel, globalApiKey)
        SendMessageTextField(focus, enterQuestion, buttonClick)

        SendButtonClickedResponse(
            buttonClick,
            enterQuestion,
            viewModel,
            globalApiKey.value
        )
    }
}

@Composable
private fun SendButtonClickedResponse(
    buttonClick: MutableState<Boolean>,
    enterQuestion: MutableState<String>,
    viewModel: ChatViewModel,
    storeApiKey: String
) {
    if (buttonClick.value) {
        if (enterQuestion.value.isNullOrEmpty()) {
            Toast.makeText(LocalContext.current, "Field is empty.", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.sendMessage(
                LocalContext.current,
                enterQuestion.value,
                storeApiKey = storeApiKey
            )

            enterQuestion.value = ""
        }
    }
    buttonClick.value = false
}

@Composable
private fun ChatResponseCardView(
    isLoading: MutableState<Boolean>,
    viewModel: ChatViewModel,
    globalApiKey: MutableState<String>
) {
    Card(
        elevation = 1.dp, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
    ) {

        Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
            if (isLoading.value) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                ) {
                    CircularProgressIndicatorLoading()
                }
            }
        }

        ResponseTextView(viewModel, globalApiKey)
        Spacer(modifier = Modifier.padding(top = 20.dp))
    }
}

@Composable
fun TopAppBarContent(navController: NavController, storeApiKey: String) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        backgroundColor = Color.DarkGray
    ) {
        val changeKey = remember {
            mutableStateOf(false)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            ChatAILogo()

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.baseline_key_24),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(end = 8.dp)
                    .clickable {
                        changeKey.value = !changeKey.value
                        navController.navigate("changeKey/${storeApiKey}")
                    }
            )
        }
    }
}

@Composable
fun ChatAILogo() {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(start = 8.dp),
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

@Composable
private fun ResponseTextView(viewModel: ChatViewModel, StoreApi: MutableState<String>) {
    Column {
        val fetchData = viewModel.listOfLiveData.observeAsState().value
        if (fetchData?.size != null) {
            LazyColumn(fetchData)
        } else {
            val initialMessage = if (StoreApi.value == " ") {
                "Hello! It looks like you haven't added your OpenAI API key. You can add it by clicking on the key icon located at the top right corner of the screen."
            } else {
                "Hello! How can I assist you today?"
            }

            Row() {
                Image(
                    painter = painterResource(id = R.drawable.baseline_api_24),
                    contentDescription = "",
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 5.dp,
                        start = 8.dp
                    )
                )

                Text(
                    text = initialMessage,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 5.dp,
                        start = 8.dp,
                        end = 8.dp
                    ),
                    fontSize = 15.sp
                )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun LazyColumn(listOfResponse: MutableList<String>?) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LazyColumn(state = listState) {
        listOfResponse?.let { it1 ->
            items(it1.size - 1) { item ->

                if (item % 2 == 0) {
                    UserQuestion(listOfResponse, item)
                    scope.launch { listState.scrollToItem(index = it1.lastIndex) }
                } else {
                    AIResponse(listOfResponse, item)
                }
            }
        }
    }
}

@Composable
private fun UserQuestion(
    listOfResponse: MutableList<String>,
    item: Int
) {
    Row() {
        Image(
            painter = painterResource(id = R.drawable.baseline_person_24),
            contentDescription = "",
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 8.dp
                )
        )
        Text(
            text = listOfResponse[item],
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 10.dp,
                    start = 3.dp,
                    end = 5.dp,
                    bottom = 10.dp
                ),

            fontSize = 15.sp

        )
    }
    Divider(modifier = Modifier.padding(start = 30.dp, end = 5.dp), thickness = 1.dp)

}


@Composable
private fun AIResponse(
    listOfResponse: MutableList<String>,
    item: Int
) {
    Row {
        Image(
            painter = painterResource(id = R.drawable.baseline_api_24),
            contentDescription = "",
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 8.dp
                )
        )
        Text(
            text = listOfResponse[item].trimStart(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 4.dp,
                    end = 5.dp
                ),
            fontSize = 15.sp
        )
    }
    Divider(modifier = Modifier.padding(start = 30.dp, end = 5.dp), thickness = 1.dp)

}


@Composable
private fun SendMessageTextField(
    focus: FocusManager,
    enterQuestion: MutableState<String>,
    buttonClick: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        focus
        OutlinedTextField(
            value = enterQuestion.value,
            onValueChange = { enterQuestion.value = it },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 1.dp),
            singleLine = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.DarkGray,
                cursorColor = Color.Black
            )
        )
        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send message",
            tint = Color.Gray,
            modifier = Modifier
                .clickable {
                    buttonClick.value = !buttonClick.value
                    focus.clearFocus()
                }
                .padding(end = 8.dp)
                .rotate(290f))
    }
}

@Composable
fun CircularProgressIndicatorLoading() {
    CircularProgressIndicator(
        modifier = Modifier.size(45.dp),
        color = Color.Gray
    )
}
