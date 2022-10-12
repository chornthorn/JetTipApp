package dev.bongthorn.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bongthorn.jettipapp.components.InputField
import dev.bongthorn.jettipapp.ui.theme.JetTipAppTheme
import dev.bongthorn.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTipAppTheme {
                MyApp {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        TopHeader()
                        MainContent()
                    }
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
        content = content,
    )
}

@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp)))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Total per person:", style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$%.2f".format(totalPerPerson), style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
            )
        }
    }
}


@Composable
fun MainContent() {

    BillForm() { billAmount ->
        println("onSubmitted: Total amount: $billAmount")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValueSubmitted: (String) -> Unit = {},
) {
    val totalAmount = remember {
        mutableStateOf("")
    }

    val validState = remember(totalAmount.value) {
        totalAmount.value.trim().isNotEmpty()
    }

    val splitState = remember {
        mutableStateOf(1)
    }

    val percentage = remember {
        mutableStateOf(0.0f)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(all = 12.dp)
        ) {

            InputField(
                valueState = totalAmount,
                labelId = "Enter Bill",
                keyboardType = KeyboardType.Number,
                keyboardActions = KeyboardActions {
                    if (!validState) return@KeyboardActions

                    onValueSubmitted(totalAmount.value.trim())

                    keyboardController?.hide()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                totalAmount.value = it
            }

            if (validState) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "Split",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RoundIconButton(imageVector = Icons.Default.Remove) {
                            if (splitState.value > 0) {
                                splitState.value -= 1
                            }
                        }

                        Text(text = "${splitState.value}", style = MaterialTheme.typography.h5)

                        RoundIconButton(imageVector = Icons.Default.Add) {
                            if (splitState.value >= 0) {
                                splitState.value += 1
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                RowSections(label = "Tip", value = "$900")
                Spacer(modifier = Modifier.height(8.dp))
                RowSections(label = "Percentage", value = "%.0f".format(percentage.value * 100)+"%")
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = percentage.value,
                    onValueChange = { percentage.value = it },
                    steps = 5,
                    onValueChangeFinished = {

                    }
                )

            } else {
                Box {

                }
            }

        }
    }
}

@Composable
fun RowSections(
    label: String,
    value: String,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
            )
        )
        Text(
            text = value,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
            )
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        MyApp {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                TopHeader(totalPerPerson = 135.0)
                MainContent()
            }
        }
    }
}